package ORM;

import java.util.ArrayList;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

import OWL.ClassIRI;
import OWL.Individual;
import OWL.ObjectPropertyIRI;
import database.Table;
import genericcode.GenericClass;

public class ClassMapping extends Individual{
	
	private GenericClass _class;
	private ArrayList<Table> tables = new ArrayList<Table>();
	private Table table;
//	private OWLObjectProperty 
	
	public ClassMapping(OWLOntology o, GenericClass gc) {
		super(o, "class_mapping__"+ gc.getCodeName());
		
		this._class = gc;
		gc.setClassMapping(this);
		this.classAssertion(ClassIRI.CLASS_MAPPING);
	}
	
	public ClassMapping(OWLOntology o,OWLNamedIndividual i) {
		super(o, i);
	}
	
	public void addTable(Table t) {
		this.tables.add(t);
		this.setObjectProperty(ObjectPropertyIRI.ENTITY_CLASS_MAPPED_TO, t);
	}
	
	public void set_class(GenericClass c) {
		this._class = c;
	}

	public Table getTable() {
		return table;
	}
	public void setTable(Table t) {
		this.table = t;
	}
}
