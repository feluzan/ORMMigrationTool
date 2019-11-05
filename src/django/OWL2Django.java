package django;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

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
import database.Table;
import database.TableType;
import genericcode.GenericClass;
import genericcode.GenericVariable;
import genericcode.PrimitiveType;
import genericcode.Type;
import genericcode.ValueType;

public class OWL2Django {
	OWLOntology o;
	OWLOntologyManager manager;
	OWLDataFactory factory;
	IRI iri;
	OWLReasonerFactory reasonerFactory;
	OWLReasoner reasoner;
	
	private Map<OWLIndividual, Table> tables = new HashMap<OWLIndividual, Table>();
	private Map<OWLIndividual, Column> columns = new HashMap<OWLIndividual, Column>();
	
	private Map<OWLIndividual, GenericClass> classes = new HashMap<OWLIndividual, GenericClass>();
	private Map<OWLIndividual, InheritanceMapping> inheritanceMappings = new HashMap<OWLIndividual,InheritanceMapping>();
	private Map<OWLIndividual, GenericVariable> variables = new HashMap<OWLIndividual,GenericVariable>();
	private Map<OWLIndividual, VariableMapping> variableMappings = new HashMap<OWLIndividual,VariableMapping>();
	private Map<OWLIndividual, ValueType> valueTypes = new HashMap<OWLIndividual, ValueType>();
	
	private Map<OWLIndividual, PrimitiveType> primitiveTypes = new HashMap<OWLIndividual, PrimitiveType>();
	private Map<OWLIndividual, RelationshipMapping> relationshipMappings = new HashMap<OWLIndividual, RelationshipMapping>();
	
	public OWL2Django(String OWLPath) {
		
		
		this.manager = OWLManager.createOWLOntologyManager();
		File ORMFOFile = new File(OWLPath);
		this.o=null;
		this.factory = manager.getOWLDataFactory();
		try {
			this.o = manager.loadOntologyFromOntologyDocument(ORMFOFile);
			this.iri = this.o.getOntologyID().getOntologyIRI().get();
		} catch (OWLOntologyCreationException e) {
			System.out.println("[ERROR] Houve algum problema ao carregar a ontologia.");
			System.out.println("\tO programa será encerrado.");
			e.printStackTrace();
			System.exit(1);
		}
		
		this.reasonerFactory = new StructuralReasonerFactory();
		this.reasoner = reasonerFactory.createReasoner(this.o);

		this.retrievePrimitiveTypes();
		this.retrieveTables();
		this.retrieveColumns();
		this.retrieveClasses();
		this.retrieveSuperclasses();
		
		this.retrieveValueTypes();
		
		this.retrieveFields();
		
	}


	public void printFile(String filePath) {
		File outfile = new File(filePath);
		
		FileWriter fileWriter;
		
		try {
			fileWriter = new FileWriter(outfile);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			
			String importString = "from django.db import models\n\n";
			printWriter.print(importString);
			
			//imprimir todas as classes que não são subclasses
			for(GenericClass c : classes.values()) {
				if(c.isSubclass()) continue;
				printWriter.print(c.toCode());
				
			}
			
			for(GenericClass c : classes.values()) {
				if(!c.isSubclass()) continue;
				InheritanceMapping im = null;
				for(InheritanceMapping inh : inheritanceMappings.values()) {
					if(inh.getSubclass().equals(c)) im = inh;
				}
				printWriter.print(c.toCode(im));
				
			}
			
			printWriter.close();
			
			
		} catch (Exception e) {
			System.out.println("[ERROR] Erro ao imprimir no arquivo " + filePath);
			System.out.println("\tO programa será encerrado.");
//			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private void retrieveTables() {
		
		OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(this.o);

		OWLClass c = ClassIRI.ENTITY_TABLE.getOWLClass(o);
		NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(c,false);
		Stream<OWLNamedIndividual> individuals = individualsNodeSet.entities();

		Iterator<OWLNamedIndividual> individualsAsIterator = individuals.iterator();

		while(individualsAsIterator.hasNext()) {
			OWLNamedIndividual i = individualsAsIterator.next();
			Table t = new Table(this.o, i);
			tables.put(i, t);
			
			Stream<OWLClass> allClassesStream = reasoner.getTypes(i).entities();
			Set<OWLClass> allClasses = allClassesStream.collect(Collectors.toSet());
			
			if(allClasses.contains(ClassIRI.MULTIPLE_ENTITY_TABLE.getOWLClass(o))) {
				t.setTableType(TableType.MULTIPLE_ENTITIES_TABLE);
			}
			if(allClasses.contains(ClassIRI.SINGLE_ENTITY_TABLE.getOWLClass(o))) {
				t.setTableType(TableType.SINGLE_ENTITY_TABLE);
			}

		}
		
	}
	
	private void retrievePrimitiveTypes() {
		OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(this.o);

		OWLClass c = ClassIRI.PRIMITIVE_TYPE.getOWLClass(o);
		NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(c,false);
		Stream<OWLNamedIndividual> individuals = individualsNodeSet.entities();

		Iterator<OWLNamedIndividual> individualsAsIterator = individuals.iterator();
		while(individualsAsIterator.hasNext()) {
			OWLNamedIndividual i = individualsAsIterator.next();
			PrimitiveType pt = new PrimitiveType(this.o, i);
			primitiveTypes.put(i, pt);
			
			Set<OWLLiteral> dataPropertySet;
			Iterator<OWLLiteral> dataPropertyIterator;
			dataPropertySet = reasoner.getDataPropertyValues(pt.getIndividual(), DataPropertyIRI.TYPE_NAME.getOWLDataProperty(this.o));
			dataPropertyIterator = dataPropertySet.iterator();

			if(dataPropertyIterator.hasNext()) {
				String name =dataPropertyIterator.next().getLiteral();
				pt.setTypeName(name);
			}else {
				System.out.println("[ERROR] Erro ao identificar o nome do tipo primitivo.");
				System.out.println("\tO programa será encerrado.");
				System.exit(1);
			}
		}
	}
	
	private void retrieveValueTypes() {
		OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(this.o);

		OWLClass c = ClassIRI.VALUE_TYPE.getOWLClass(o);
		NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(c,false);
		Stream<OWLNamedIndividual> individuals = individualsNodeSet.entities();

		Iterator<OWLNamedIndividual> individualsAsIterator = individuals.iterator();
		
		Stream<OWLNamedIndividual> rangesStream;
		Set<OWLNamedIndividual> rangesSet;

		while(individualsAsIterator.hasNext()) {
			OWLNamedIndividual i = individualsAsIterator.next();
			ValueType vt = new ValueType(this.o, i);
			valueTypes.put(i, vt);
			
			rangesStream = reasoner.getObjectPropertyValues(vt.getIndividual(), ObjectPropertyIRI.REFERS_TO.getOWLObjectProperty(this.o)).entities();
			rangesSet = rangesStream.collect(Collectors.toSet());
			
			Type t;
			OWLNamedIndividual typeI = rangesSet.iterator().next();
			t = primitiveTypes.get(typeI);
			if(t==null) {
				t = classes.get(typeI);
			}
			if(t==null) {
				System.out.println("[ERROR] Problema na identificação de Type.");
				System.out.println("\tO programa será encerrado.");
				System.exit(1);
			}
			vt.setType(t);
		}
		
	}
	
	private void retrieveColumns() {
		OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(this.o);

		OWLClass c = ClassIRI.COLUMN.getOWLClass(o);
		NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(c,false);
		Stream<OWLNamedIndividual> individuals = individualsNodeSet.entities();

		Iterator<OWLNamedIndividual> individualsAsIterator = individuals.iterator();

		while(individualsAsIterator.hasNext()) {
			OWLNamedIndividual i = individualsAsIterator.next();
			Column col = new Column(this.o, i);
			columns.put(i, col);
			
//			Stream<OWLClass> allClassesStream = reasoner.getTypes(i).entities();
//			Set<OWLClass> allClasses = allClassesStream.collect(Collectors.toSet());
			
//			if(allClasses.contains(ClassIRI.MULTIPLE_ENTITY_TABLE.getOWLClass(o))) {
//				t.setTableType(TableType.MULTIPLE_ENTITIES_TABLE);
//			}
//			if(allClasses.contains(ClassIRI.SINGLE_ENTITY_TABLE.getOWLClass(o))) {
//				t.setTableType(TableType.SINGLE_ENTITY_TABLE);
//			}

		}
	}
	
	private void retrieveClassMappings(GenericClass gc) {
		Stream<OWLNamedIndividual> rangesStream;
		Set<OWLNamedIndividual> rangesSet;
		
		rangesStream = reasoner.getObjectPropertyValues(gc.getIndividual(), ObjectPropertyIRI.ENTITY_CLASS_MAPPED_BY.getOWLObjectProperty(this.o)).entities();
		rangesSet = rangesStream.collect(Collectors.toSet());
		
		if(rangesSet.size()>0) {
			ClassMapping cm = new ClassMapping(this.o, rangesSet.iterator().next());
			gc.setClassMapping(cm);
			cm.set_class(gc);
			rangesStream = reasoner.getObjectPropertyValues(cm.getIndividual(), ObjectPropertyIRI.ENTITY_CLASS_MAPPED_TO.getOWLObjectProperty(this.o)).entities();
			rangesSet = rangesStream.collect(Collectors.toSet());
			Iterator<OWLNamedIndividual> tableIterator = rangesSet.iterator();
			while(tableIterator.hasNext()) {
				Table t = tables.get(tableIterator.next());
				cm.setTable(t);
//				System.out.println(t.getTableName());
			}
		}
		
	}
	
	private void retrieveInheritanceMapping(GenericClass subclass) {
		Stream<OWLNamedIndividual> rangesStream;
		Set<OWLNamedIndividual> rangesSet;
		
		rangesStream = reasoner.getObjectPropertyValues(subclass.getIndividual(), ObjectPropertyIRI.SUBCLASS_MAPPED_BY.getOWLObjectProperty(this.o)).entities();
		rangesSet = rangesStream.collect(Collectors.toSet());

		InheritanceMapping im = new InheritanceMapping(this.o, rangesSet.iterator().next());
		inheritanceMappings.put(im.getIndividual(),im);
		im.setSubclass(subclass);
		
		Stream<OWLClass> allClassesStream = reasoner.getTypes(im.getIndividual()).entities();
		Set<OWLClass> allClasses = allClassesStream.collect(Collectors.toSet());
		
		if(allClasses.contains(ClassIRI.SINGLE_TABLE_INHERITANCE_MAPPING.getOWLClass(o))) {
			im.setInheritanceStrategy(InheritanceStrategy.SINGLE_TABLE);
		}
		if(allClasses.contains(ClassIRI.TABLE_PER_CLASS_INHERITANCE_MAPPING.getOWLClass(o))) {
			im.setInheritanceStrategy(InheritanceStrategy.TABLE_PER_CLASS);
		}
		if(allClasses.contains(ClassIRI.TABLE_PER_CONCRETE_CLASS_INHERITANCE_MAPPING.getOWLClass(o))) {
			im.setInheritanceStrategy(InheritanceStrategy.TABLE_PER_CONCRETE_CLASS);
		}
	}
	
	private void retrieveSuperclasses() {
		
		OWLClass c = ClassIRI.ENTITY_SUPERCLASS.getOWLClass(o);

		NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(c,false);
		Stream<OWLNamedIndividual> individuals = individualsNodeSet.entities();
//		System.out.println(individuals.count() + " -------");

		Stream<OWLNamedIndividual> rangesStream;
		Set<OWLNamedIndividual> rangesSet;
		
		
		Iterator<OWLNamedIndividual> individualsAsIterator = individuals.iterator();

		while(individualsAsIterator.hasNext()) {
			OWLNamedIndividual i = individualsAsIterator.next();
			GenericClass superclass = classes.get(i);
			rangesStream = reasoner.getObjectPropertyValues(superclass.getIndividual(), ObjectPropertyIRI.SUPERCLASS_MAPPED_BY.getOWLObjectProperty(this.o)).entities();
//			rangesSet = rangesStream.collect(Collectors.toSet());
			
			Iterator<OWLNamedIndividual> rangesSetIterator = rangesStream.iterator();
			while(rangesSetIterator.hasNext()) {
				InheritanceMapping im = inheritanceMappings.get(rangesSetIterator.next());
				im.setSuperclass(superclass);
				superclass.addSubclass(im.getSubclass());
				im.getSubclass().setSuperclass(superclass);
			}
			
		}
		
		
		
	}
	
	private void retrieveClasses() {

		OWLClass c = ClassIRI.CLASS.getOWLClass(o);

		NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(c,false);
		Stream<OWLNamedIndividual> individuals = individualsNodeSet.entities();

		Iterator<OWLNamedIndividual> individualsAsIterator = individuals.iterator();

		while(individualsAsIterator.hasNext()) {
			OWLNamedIndividual i = individualsAsIterator.next();
			DjangoClass dc = new DjangoClass(this.o,i);

			classes.put(i, dc);

			Stream<OWLClass> allClassesStream = reasoner.getTypes(i).entities();
			Set<OWLClass> allClasses = allClassesStream.collect(Collectors.toSet());
			
			if(allClasses.contains(ClassIRI.ABSTRACT_CLASS.getOWLClass(o))) {
				dc.set_abstract(true);
			}
			
			
			if(allClasses.contains(ClassIRI.ENTITY_CLASS.getOWLClass(o))) {
				dc.setEntity(true);
				this.retrieveClassMappings(dc);
			}
			
			if(allClasses.contains(ClassIRI.ENTITY_SUBCLASS.getOWLClass(o))) {
				this.retrieveInheritanceMapping(dc);
			}
			
			
		}
	}
	
	private void retrieveVariableMappings(GenericVariable gv) {
		Stream<OWLNamedIndividual> rangesStream;
		Set<OWLNamedIndividual> rangesSet;
		rangesStream = reasoner.getObjectPropertyValues(gv.getIndividual(), ObjectPropertyIRI.VARIABLE_MAPPED_BY.getOWLObjectProperty(this.o)).entities();
		rangesSet = rangesStream.collect(Collectors.toSet());
		
		if(rangesSet.size()>0) {
			VariableMapping vm = new VariableMapping(this.o, rangesSet.iterator().next());
			vm.setVariable(gv);
			variableMappings.put(vm.getIndividual(), vm);
			gv.setVariableMapping(vm);
			
			rangesStream = reasoner.getObjectPropertyValues(vm.getIndividual(), ObjectPropertyIRI.VARIABLE_MAPPED_TO.getOWLObjectProperty(this.o)).entities();
			rangesSet = rangesStream.collect(Collectors.toSet());
			Column col = columns.get(rangesSet.iterator().next());
			col.setVariableMapping(vm);
			vm.setColumn(col);
		}
	}
	
	private void retrieveFields() {
		
		OWLClass c = ClassIRI.INSTANCE_VARIABLE.getOWLClass(o);

		NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(c,false);
		Stream<OWLNamedIndividual> individuals = individualsNodeSet.entities();
		Iterator<OWLNamedIndividual> individualsAsIterator = individuals.iterator();
		
		Stream<OWLNamedIndividual> rangesStream;
		Set<OWLNamedIndividual> rangesSet;
		Iterator<OWLNamedIndividual> rangesSetIterator;

		

		while(individualsAsIterator.hasNext()) {
			OWLNamedIndividual i = individualsAsIterator.next();
			DjangoVariable dv = new DjangoVariable(this.o, i);
			variables.put(i, dv);
			
			Stream<OWLClass> allClassesStream = reasoner.getTypes(i).entities();
			Set<OWLClass> allClasses = allClassesStream.collect(Collectors.toSet());
			
			if(allClasses.contains(ClassIRI.MAPPED_VARIABLE.getOWLClass(o))) {
				dv.setMapped(true);
			}
			if(allClasses.contains(ClassIRI.MAPPED_PRIMARY_KEY.getOWLClass(o))) {
				dv.setPk(true);
			}
			if(allClasses.contains(ClassIRI.MAPPED_FOREIGN_KEY.getOWLClass(o))) {
				dv.setFk(true);
			}
			this.retrieveVariableMappings(dv);
			
			
			Set<OWLLiteral> dataPropertySet;
			Iterator<OWLLiteral> dataPropertyIterator;
			dataPropertySet = reasoner.getDataPropertyValues(dv.getIndividual(), DataPropertyIRI.VARIABLE_NAME.getOWLDataProperty(this.o));
			dataPropertyIterator = dataPropertySet.iterator();
			if(dataPropertyIterator.hasNext()) {
				String name =dataPropertyIterator.next().getLiteral();
				dv.setCodeName(name);
			}else {
				System.out.println("[ERROR] Erro ao identificar o nome da variavel.");
				System.out.println("\tO programa será encerrado.");
				System.exit(1);
			}
//			
			rangesStream = reasoner.getObjectPropertyValues(dv.getIndividual(), ObjectPropertyIRI.BELONGS_TO.getOWLObjectProperty(this.o)).entities();
			rangesSet = rangesStream.collect(Collectors.toSet());
			rangesSetIterator = rangesSet.iterator();
			if(rangesSetIterator.hasNext()) {
				GenericClass gc = classes.get(rangesSetIterator.next());
				gc.addVariable(dv);
				dv.set_class(gc);
			}else {
				System.out.println("[ERROR] Classe no qual a variável pertence não foi identificada.");
				System.out.println("\tO programa será encerrado.");
				System.exit(1);
			}
			
			rangesStream = reasoner.getObjectPropertyValues(dv.getIndividual(), ObjectPropertyIRI.IS_TYPE_OF.getOWLObjectProperty(this.o)).entities();
			rangesSet = rangesStream.collect(Collectors.toSet());
			rangesSetIterator = rangesSet.iterator();
			if(rangesSetIterator.hasNext()) {
				ValueType vt = valueTypes.get(rangesSetIterator.next());
				dv.setValueType(vt);
			}else {
				System.out.println("[ERROR] O ValueType da variável não foi identificado.");
				System.out.println("\tO programa será encerrado.");
				System.exit(1);
			}
			
			rangesStream = reasoner.getObjectPropertyValues(dv.getIndividual(), ObjectPropertyIRI.REPRESENTS_RELATIONSHIP.getOWLObjectProperty(this.o)).entities();
			rangesSet = rangesStream.collect(Collectors.toSet());
			rangesSetIterator = rangesSet.iterator();
			if(rangesSetIterator.hasNext()) {
				RelationshipMapping rm = new RelationshipMapping(this.o, rangesSetIterator.next());
				relationshipMappings.put(rm.getIndividual(), rm);
				rm.setVariable(dv);
				dv.setRelationshipMapping(rm);
				Stream<OWLClass> rmAllClassesStream = reasoner.getTypes(rm.getIndividual()).entities();
				Set<OWLClass> rmAllClasses = rmAllClassesStream.collect(Collectors.toSet());
				
//				if(rmAllClasses.contains(ClassIRI.ONE_TO_ONE_RELATIONSHIP_MAPPING.getOWLClass(o))) {
//					rm.setRelationshipType(RelationshipType.ONE_TO_ONE);
//				}else {
//					if(rmAllClasses.contains(ClassIRI.ONE_TO_MANY_RELATIONSHIP_MAPPING.getOWLClass(o))) {
//						rm.setRelationshipType(RelationshipType.ONE_TO_MANY);
//					}else {
//						if(rmAllClasses.contains(ClassIRI.MANY_TO_ONE_RELATIONSHIP_MAPPING.getOWLClass(o))) {
//							rm.setRelationshipType(RelationshipType.MANY_TO_ONE);
//						}else {
//							if(rmAllClasses.contains(ClassIRI.MANY_TO_MANY_RELATIONSHIP_MAPPING.getOWLClass(o))) {
//								rm.setRelationshipType(RelationshipType.MANY_TO_MANY);
//							}else {
//								System.out.println("[ERROR] Erro ao identificar o Tipo de Relacionamento.");
//								System.out.println("\tO programa será encerrado.");
//								System.exit(1);
//							}
//						}
//						
//					}
//					
//				}
				
			}
		}
		
	}
	
	
	
	
}
