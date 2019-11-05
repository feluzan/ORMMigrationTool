package genericcode;

import java.util.ArrayList;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;

import ORM.ClassMapping;
import ORM.InheritanceMapping;
import ORM.InheritanceStrategy;
import OWL.ClassIRI;
import OWL.DataPropertyIRI;
import OWL.ObjectPropertyIRI;

//import ORM.InheritanceMapping;

public abstract class GenericClass extends Type{
	


	private String codeName;
	private boolean _abstract;
	private boolean entity;
	private GenericClass superclass;
	private ArrayList<GenericClass> subclasses;
	private ClassMapping classMapping;
	private ArrayList<GenericVariable> variables;
	
	public abstract String toCode();
	public abstract String toCode(InheritanceMapping im);

	public GenericClass(OWLOntology o, String iri) {
		super(o, iri);
		this.subclasses = new ArrayList<GenericClass>();
		this.variables = new ArrayList<GenericVariable>();
	}
	
	public GenericClass(OWLOntology o,OWLNamedIndividual i) {
		super(o, i);
		this._abstract = false;
		this.entity = false;
		this.superclass=null;
		this.setCodeNameFromIndividual();
		this.subclasses = new ArrayList<GenericClass>();
		this.variables = new ArrayList<GenericVariable>();
	}
	
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
		
	}
	
	
	public boolean is_abstract() {
		return _abstract;
	}
	public void set_abstract(boolean _abstract) {
		this._abstract = _abstract;
	}
	
	
	public boolean isEntity() {
		return entity;
	}
	public void setEntity(boolean entity) {
		this.entity = entity;
	}
	
	
	public GenericClass getSuperclass() {
		return superclass;
	}
	public void setSuperclass(GenericClass superclass) {
		this.superclass = superclass;
		if(this.isEntity()) {
			this.classAssertion(ClassIRI.ENTITY_SUBCLASS);
		}else {
			this.classAssertion(ClassIRI.SUBCLASS);
		}
	}
	public boolean isSubclass() {
		if(this.superclass==null) return false;
		return true;
	}
	
	
	public abstract String getSuperclassName();
	public abstract InheritanceStrategy getCodeInheritanceStrategy();
	public abstract String getCodeTableName();
	
	
	public boolean isSuperclass() {
		if(this.subclasses.size()==0) return false;
		return true;
	}
	public void addSubclass(GenericClass gc) {
		this.subclasses.add(gc);
	}
	public ArrayList<GenericClass> getSubclasses(){
		return this.subclasses;
	}
	
	
	public void setClassMapping(ClassMapping cm) {
		this.classMapping = cm;
		this.setObjectProperty(ObjectPropertyIRI.ENTITY_CLASS_MAPPED_BY, cm);
	}
	public ClassMapping getClassMapping() {
		return this.classMapping;
	}

	public void addVariable(GenericVariable gv) {
		variables.add(gv);
	}
	public ArrayList<GenericVariable> getVariables(){
		return this.variables;
	}
	
	private void setCodeNameFromIndividual() {
		String name = this.getIndividual().getIRI().toString();
//		System.out.println(name);
		name = name.split("__")[1];
		this.setCodeName(name);
	}
	

}
