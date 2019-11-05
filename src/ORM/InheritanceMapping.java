package ORM;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.model.OWLClass;

import OWL.ClassIRI;
import OWL.Individual;
import OWL.ObjectPropertyIRI;
import genericcode.GenericClass;
import database.Table;


public class InheritanceMapping extends Individual{
	
	private GenericClass superclass;
	private GenericClass subclass;
	private InheritanceStrategy inheritanceStrategy;
	private ArrayList<Table> tables;// = new ArrayList<Table();
	
	public InheritanceMapping(OWLOntology o, GenericClass subclass) {
		super(o, "inheritance_mapping__" + subclass.getSuperclass().getCodeName() + "_" + subclass.getCodeName());
		this.tables = new ArrayList<Table>();
		GenericClass rootClass = subclass.getSuperclass();
		while(rootClass.isSubclass()) {
			rootClass = rootClass.getSuperclass();
		}
		
		this.inheritanceStrategy = rootClass.getCodeInheritanceStrategy();
		switch(this.inheritanceStrategy) {
		case SINGLE_TABLE:
			this.classAssertion(ClassIRI.SINGLE_TABLE_INHERITANCE_MAPPING);
			break;
			
		case TABLE_PER_CLASS:
			this.classAssertion(ClassIRI.TABLE_PER_CLASS_INHERITANCE_MAPPING);
			break;
			
		case TABLE_PER_CONCRETE_CLASS:
			this.classAssertion(ClassIRI.TABLE_PER_CONCRETE_CLASS_INHERITANCE_MAPPING);
			break;
			
		default:
			System.out.println("[ERROR] InheritanceMapping");
		}
		
		subclass.setObjectProperty(ObjectPropertyIRI.SUBCLASS_MAPPED_BY, this);
		subclass.getSuperclass().setObjectProperty(ObjectPropertyIRI.SUPERCLASS_MAPPED_BY, this);
	}
	
	public InheritanceMapping(OWLOntology o,OWLNamedIndividual i) {
		super(o, i);
		this.setInheritanceStrategyFromIndividual();
	}

	public InheritanceStrategy getInheritanceStrategy() {
		return inheritanceStrategy;
	}
	public void setInheritanceStrategy(InheritanceStrategy inheritanceStrategy) {
		this.inheritanceStrategy = inheritanceStrategy;
	}
	public GenericClass getSuperclass() {
		return superclass;
	}
	public void setSuperclass(GenericClass superclass) {
		this.superclass = superclass;
	}

	public GenericClass getSubclass() {
		return subclass;
	}
	public void setSubclass(GenericClass subclass) {
		this.subclass = subclass;
	}

	
	public void addTable(Table t) {
		this.tables.add(t);
		ObjectPropertyIRI pIRI = null;
		String iri = "#";
		switch(this.inheritanceStrategy) {
		case SINGLE_TABLE:
			pIRI = ObjectPropertyIRI.SINGLE_TABLE_INHERITANCE_MAPPED_TO;
			break;
		case TABLE_PER_CLASS:
			pIRI = ObjectPropertyIRI.TABLE_PER_CLASS_INHERITANCE_MAPPED_TO;
			break;
		case TABLE_PER_CONCRETE_CLASS:
			pIRI = ObjectPropertyIRI.TABLE_PER_CONCRETE_CLASS_INHERITANCE_MAPPED_TO;
			break;
		default:
			break;
		
		}
		this.setObjectProperty(pIRI, t);
	}

	public ArrayList<Table> getTables() {
		return this.tables;
	}

	private void setInheritanceStrategyFromIndividual() {
		OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(this.getOntology());
		Stream<OWLClass> allClassesStream = reasoner.getTypes(this.getIndividual()).entities();
		Set<OWLClass> allClasses = allClassesStream.collect(Collectors.toSet());
		if(allClasses.contains(ClassIRI.TABLE_PER_CLASS_INHERITANCE_MAPPING.getOWLClass(this.getOntology()))) {
			this.setInheritanceStrategy(InheritanceStrategy.TABLE_PER_CLASS);
		}
		if(allClasses.contains(ClassIRI.TABLE_PER_CONCRETE_CLASS_INHERITANCE_MAPPING.getOWLClass(this.getOntology()))) {
			this.setInheritanceStrategy(InheritanceStrategy.TABLE_PER_CONCRETE_CLASS);
		}
		if(allClasses.contains(ClassIRI.SINGLE_TABLE_INHERITANCE_MAPPING.getOWLClass(this.getOntology()))) {
			this.setInheritanceStrategy(InheritanceStrategy.SINGLE_TABLE);
		}
	}
}
