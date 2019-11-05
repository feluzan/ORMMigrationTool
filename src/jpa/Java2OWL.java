package jpa;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;

import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;

import ORM.ClassMapping;
import ORM.InheritanceMapping;
import ORM.InheritanceStrategy;
import ORM.RelationshipMapping;
import ORM.RelationshipType;
import ORM.VariableMapping;
import OWL.ClassIRI;
import OWL.DataPropertyIRI;
import OWL.ObjectPropertyIRI;
import database.Column;
import database.RelationshipAssociationTable;
import database.Table;
import database.TableType;
import genericcode.GenericClass;
import genericcode.GenericVariable;
import genericcode.PrimitiveType;
import genericcode.Type;
import genericcode.ValueType;

public class Java2OWL {
	OWLOntology ormfo;
	OWLOntologyManager manager;
	OWLDataFactory factory;
	
	public static Map<String, PrimitiveType> primitiveTypes = new HashMap<String, PrimitiveType>();
	public static Map<String, GenericClass> classes = new HashMap<String, GenericClass>();
	public static Map<GenericClass, InheritanceMapping> inheritanceMappings = new HashMap<GenericClass, InheritanceMapping>();
	
	public static Map<GenericClass, ClassMapping> classMappings = new HashMap<GenericClass, ClassMapping>();
	
	public static Map<GenericClass, Table> tables = new HashMap<GenericClass, Table>();

	
	public Java2OWL(File folder, OWLOntology ormfo) {
		this.ormfo = ormfo;
		this.manager = ormfo.getOWLOntologyManager();
		this.factory = this.manager.getOWLDataFactory();
		this.processPrimitiveTypes();
		
		CompilationUnit compilationUnit = null;
		for (File f : folder.listFiles()) {
			if(!f.isFile()) continue;
			try {
				System.out.println("\t[+] " + f.getName());
				compilationUnit = JavaParser.parse(f);
			} catch (FileNotFoundException e) {
				System.out.println("[ERROR] Arquivo " + f.getName() + " não encontrado.");
				continue;
			}
			List<Node> nodeList = compilationUnit.getChildNodes();

			for (Node n : nodeList) {
				if(n instanceof ClassOrInterfaceDeclaration) {
					processClassNode(n);
				}
			}
		}

		processInheritance();
		processClassMappings();
		processTables();
//		
//		processFields();
//		processVariableMappings();
//		processColumns();
//		
//		processRelationships();

//		processRelationshipAssociationTables();
		
	}
	
	private void processPrimitiveTypes() {
		PrimitiveType pt;
		for(JavaPrimitiveType jpt : JavaPrimitiveType.values()) {
			pt = new PrimitiveType(this.ormfo, jpt.toIRI());
			pt.setDataProperty(DataPropertyIRI.TYPE_NAME, jpt.toString());
			pt.setTypeName(jpt.toString());
			primitiveTypes.put(jpt.toString(),pt);
		}
}
	
	private void processClassNode(Node node) {
		String className = ((NodeWithSimpleName<ClassOrInterfaceDeclaration>) node).getNameAsString();
		JavaClass jc = new JavaClass(this.ormfo, (ClassOrInterfaceDeclaration) node);
		classes.put(className, jc);
		
	}
	
	private void processInheritance() {
		for(GenericClass jc : classes.values()) {
			String superclassName = ((JavaClass)jc).getSuperclassName();
			if(superclassName!=null) {
				GenericClass superclass = classes.get(superclassName);
				jc.setSuperclass(superclass);
				superclass.addSubclass(jc);
				superclass.classAssertion(ClassIRI.ENTITY_SUPERCLASS);
				
				
				InheritanceMapping im = new InheritanceMapping(this.ormfo, jc);
				superclass.setObjectProperty(ObjectPropertyIRI.SUPERCLASS_MAPPED_BY, im);
				jc.setObjectProperty(ObjectPropertyIRI.SUBCLASS_MAPPED_BY, im);
				inheritanceMappings.put(jc, im);
			}
		}
	}
	
	private void processClassMappings() {
		//Somente uma EntityClass abstrata cujo a herança seja do tipo TABLE PER CONCRETE CLASS não está ssociada a um Class Mapping
		for(GenericClass gc : classes.values()) {
			if(gc.isEntity()) {
				if(gc.is_abstract() && (gc.getCodeInheritanceStrategy()==InheritanceStrategy.TABLE_PER_CONCRETE_CLASS)) {
					continue;
				}
				ClassMapping cm = new ClassMapping(this.ormfo,gc);
				classMappings.put(gc, cm);
			}
		}
	}
	
	public void processTables() {
		
		//PASSO 1: Criando tabelas para classes que nao sao subclasses
		for(GenericClass gc : classes.values()) {
			if(!gc.isEntity()) continue;
			if(!gc.isSubclass()) {
				
				if(gc.isSuperclass()) {
					Table t;
					switch(gc.getCodeInheritanceStrategy()) {
					case SINGLE_TABLE:
						t = new Table(this.ormfo, gc, TableType.MULTIPLE_ENTITIES_TABLE);
						tables.put(gc, t);
						if(gc.is_abstract()) break;
						break;
						
					case TABLE_PER_CLASS:
						t = new Table(this.ormfo, gc, TableType.SINGLE_ENTITY_TABLE);
						tables.put(gc, t);
						break;
						
					case TABLE_PER_CONCRETE_CLASS:
						if(gc.is_abstract()) break;
						t = new Table(this.ormfo, gc, TableType.SINGLE_ENTITY_TABLE);
						tables.put(gc, t);
						break;
						
					default:
						System.out.println("[ERROR] Process Tables...");
						break;
					
					}
				}else {
					Table t = new Table(this.ormfo, gc, TableType.ENTITY_TABLE);
					tables.put(gc, t);
				}
				
				//Passo 2: percorrendo recursivamente a lista de subclasses desta classe
				for(GenericClass subclass : gc.getSubclasses()) {
					processSubclassTable(subclass);
				}
				
			}
			
		}
	}
	
	private void processSubclassTable(GenericClass gc) {
		InheritanceMapping im = inheritanceMappings.get(gc);
		Table t;
		GenericClass superclass;
		switch(im.getInheritanceStrategy()) {
			case SINGLE_TABLE:
				if(gc.is_abstract() || !gc.isEntity()) break;
				GenericClass rootclass = gc.getSuperclass();
				while(rootclass.isSubclass()) {
					rootclass = rootclass.getSuperclass();
				}
				t = tables.get(rootclass);
				im.addTable(t);
				break;
				
			case TABLE_PER_CLASS:
				t = new Table(this.ormfo, gc, TableType.SINGLE_ENTITY_TABLE);
				tables.put(gc, t);
				if(gc.is_abstract()) break;
				superclass = gc.getSuperclass();
				im.addTable(tables.get(superclass));
				while(superclass.isSubclass()) {
					superclass = superclass.getSuperclass();
					im.addTable(tables.get(superclass));
				}
				break;
				
			case TABLE_PER_CONCRETE_CLASS:
				if(gc.is_abstract()) break;
				t = new Table(this.ormfo, gc, TableType.MULTIPLE_ENTITIES_TABLE);
				tables.put(gc, t);
				im.addTable(t);
				superclass = gc.getSuperclass();
				break;
				
			default:
				break;
			
		}
		for(GenericClass subclass : gc.getSubclasses()) {
			processSubclassTable(subclass);
		}
	}

	
	
	
	
	
//	public void printFile(String filePath) {
//		System.out.print("[INFO] Iniciando escrita no arquivo " + filePath + "...");
//		File outfile = new File(filePath);
//		try {
//			this.manager.saveOntology(this.ormfo, new OWLXMLDocumentFormat(),new FileOutputStream(outfile));
//		} catch (OWLOntologyStorageException e) {
//			e.printStackTrace();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		System.out.println("OK!");
//	}
//	

//

//	

//	

//
//	
//		
//	private void processFields() {
//		
//		for(GenericClass gc : classes.values()) {
//			for(FieldDeclaration field : ((JavaClass)gc).getFields()) {
//				JavaVariable jv = new JavaVariable(this.ormfo,gc,field);
//				jv.setDataProperty(DataPropertyIRI.VARIABLE_NAME, jv.getCodeName());
//				variables.put(jv.get_class().getCodeName() + "." + jv.getCodeName(), jv);
//				
//				
//				//VALUE TYPE
//				String codeType = jv.getCodeType();
//				int i = codeType.indexOf("<");
//				if(i>0){
//					codeType = codeType.substring(codeType.indexOf("<")+1);
//					codeType = codeType.substring(0,codeType.indexOf(">"));
//				}
//				Type type = classes.get(codeType);
//				if(type==null) {
//					type = primitiveTypes.get(JavaPrimitiveType.getJavaPrimitiveType(codeType).toString());
//				}
//				if(type==null) {
//					System.out.println("[ERROR] Type não encontado. O programa será encerrado.");
//					System.exit(1);
//				}
//				ValueType vt = new ValueType(this.ormfo,jv,type);
//			}
//		}
//	}
//
//	private void processVariableMappings() {
//		
//		for(GenericVariable gv : variables.values()) {
//			if(gv.isMapped()) {
//				VariableMapping vm = new VariableMapping(this.ormfo, gv);
//				variableMappings.put(gv, vm);
//				
//				}
//		}
//	}
//	
//	private void processColumns() {
//		for(VariableMapping vm : variableMappings.values()) {
//			Column c = new Column(this.ormfo,vm);
//		}
//	}
//
//	private void processRelationships() {
//		
//		for(GenericVariable gv : variables.values()) {
//			if(gv.isFk()) {
//				RelationshipMapping rm = new RelationshipMapping(this.ormfo, gv);
//				relationshipMappings.put(gv, rm);
//			}
//		}
//		
//		for(RelationshipMapping rm : relationshipMappings.values()) {
//			GenericVariable gv = rm.getVariable();
//			String mappedBy = ((JavaVariable)gv).getMappedBy();
//			if(mappedBy==null) continue;
//
//			String targetClass = ((GenericClass)gv.getValueType().getType()).getCodeName();
//			GenericVariable rv = variables.get(targetClass + "." + mappedBy);
//			RelationshipMapping reverse = relationshipMappings.get(rv);
//			rm.setReverse(reverse);
//		}
//		
//		for (RelationshipMapping rm : relationshipMappings.values()) {
//			RelationshipType rt = rm.getRelationshipType();
//			if(rt == RelationshipType.MANY_TO_MANY) {
//				RelationshipAssociationTable rat = new RelationshipAssociationTable(this.ormfo,rm);
//			}
//		}
//	}

	
}
