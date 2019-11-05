package jpa;

import java.util.List;

import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import ORM.InheritanceMapping;
import ORM.InheritanceStrategy;
import OWL.ClassIRI;
import OWL.DataPropertyIRI;
import genericcode.GenericClass;

public class JavaClass extends GenericClass{
	
	private List<AnnotationExpr> annotations = null;
	private NodeList<Modifier> modifiers = null;
	NodeList<ClassOrInterfaceType> extendeds = null;
	private List<FieldDeclaration> fields;
	
	
	public JavaClass(OWLOntology o, ClassOrInterfaceDeclaration node) {
		super(o, "class__" + node.getNameAsString());
		this.setCodeName(node.getNameAsString());
		this.setDataProperty(DataPropertyIRI.TYPE_NAME, node.getNameAsString());
		
//		super(((NodeWithSimpleName<ClassOrInterfaceDeclaration>) node).getNameAsString());
		
		this.annotations = ((BodyDeclaration<ClassOrInterfaceDeclaration>) node).getAnnotations();	
		this.modifiers = ((TypeDeclaration<ClassOrInterfaceDeclaration>) node).getModifiers();
		this.fields = node.findAll(FieldDeclaration.class);
		this.extendeds = node.getExtendedTypes();
		this.setIsAbstract();
		this.setIsEntity();
		
		this.classAssertion(ClassIRI.CLASS);
		if(this.is_abstract()) {
			this.classAssertion(ClassIRI.ABSTRACT_CLASS);
		}
		if(this.isEntity()) {
			this.classAssertion(ClassIRI.ENTITY_CLASS);
		}

		
	}
	
	public List<FieldDeclaration> getFields(){
		return this.fields;
	}
	
	public void setIsEntity() {
		this.setEntity(false);
		for (AnnotationExpr ann : this.annotations) {
			if (ann.getNameAsString().equals("Entity")) {
				this.setEntity(true);
			}
		}
	}
	
	public void setIsAbstract() {
		this.set_abstract(false);
		for(Modifier m : this.modifiers) {
			if(m.getKeyword().asString().equals("abstract")) {
				this.set_abstract(true);
			}
		}
	}
	
	@Override
	public String getSuperclassName() {
		for(Node n : this.extendeds) {
			return ((NodeWithSimpleName<ClassOrInterfaceDeclaration>) n).getNameAsString();
		}
		return null;
	}
	
	@Override
	public InheritanceStrategy getCodeInheritanceStrategy() {
		
		AnnotationExpr ann = this.getAnnotation("Inheritance");
		if (ann==null) return InheritanceStrategy.SINGLE_TABLE;
		List<MemberValuePair> members = ann.findAll(MemberValuePair.class);
		for(MemberValuePair m : members) {
			if(m.getName().toString().equals("strategy")) {
				String value = m.getValue().toString().replace("\"", "").toLowerCase();
				value = value.replace("inheritancetype.", "");
				switch(value) {
				case "joined":
					return InheritanceStrategy.TABLE_PER_CLASS;

				case "table_per_class":
					return InheritanceStrategy.TABLE_PER_CONCRETE_CLASS;

				case "single_table":
					return InheritanceStrategy.SINGLE_TABLE;

				default:
					System.out.println("[WARN] Estratégia de herança não reconhecida.");
					System.out.println("\tTable per Concrete Class utilizado");
					return InheritanceStrategy.TABLE_PER_CONCRETE_CLASS;
				}

			}
		}
		return InheritanceStrategy.SINGLE_TABLE;
	}
	
	public AnnotationExpr getAnnotation(String annotation) {
		for(AnnotationExpr ann : this.annotations) {
			if(ann.getNameAsString().equals(annotation)) return ann;
		}
		return null;
	}
	
	
	@Override
	public String getCodeTableName() {
		AnnotationExpr ann = this.getAnnotation("Table");
		if(ann==null) return this.getCodeName();
		
		List<MemberValuePair> members = ann.findAll(MemberValuePair.class);
		for(MemberValuePair m : members) {
			if(m.getName().toString().equals("name")) {
				return m.getValue().toString().replace("\"", "");
			}
		}
		return this.getCodeName();
	}

	@Override
	public String toCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toCode(InheritanceMapping im) {
		// TODO Auto-generated method stub
		return null;
	}
	

//	public boolean hasAnnotation(String annotation) {
//		for(AnnotationExpr ann : this.annotations) {
//			if(ann.getNameAsString().equals(annotation)) return true;
//		}
//		return false;
//	}
//	
//	@Override
//	public String getTableName() {
//			
//		for (AnnotationExpr ann : this.getAnnotations()) {
//			if (ann.getNameAsString().equals("Table")) {
//				List<MemberValuePair> members = ann.findAll(MemberValuePair.class);
//				for(MemberValuePair m : members) {
//					if(m.getName().toString().equals("name")) {
//						return m.getValue().toString().replace("\"", "");
//					}
//				}
//			}
//		}
//		
//		return this.codeName;
//	}
//	
//


//
//	@Override
//	public void setInheritanceStrategy(GenericClass supremeMother) {
//		if(supremeMother.getInheritanceStrategy()==null) {
//			supremeMother.setInheritanceStragegy(supremeMother.getCodeInheritanceStrategy());
//		}
//		this.setInheritanceStragegy(supremeMother.getInheritanceStrategy());
//		// TODO Auto-generated method stub
//		
//	}

}
