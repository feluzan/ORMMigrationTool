package database;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

import OWL.ClassIRI;
import OWL.DataPropertyIRI;
import OWL.Individual;
import OWL.ObjectPropertyIRI;
import genericcode.GenericClass;
public class Table extends Individual{
	
	private String tableName;
	private TableType tableType;

	public Table(OWLOntology o, GenericClass gc, TableType tableType) {
		super(o, "table__" + gc.getCodeTableName());
		this.setDataProperty(DataPropertyIRI.TABLE_NAME, gc.getCodeTableName());
		if(!gc.is_abstract()) gc.getClassMapping().addTable(this);
		this.setTableType(tableType);
		gc.getClassMapping().setTable(this);
		this.setObjectProperty(ObjectPropertyIRI.DIRECTLY_RELATED_TO, gc);
		
		this.tableName = gc.getCodeTableName();

	}

	public Table(OWLOntology o,OWLNamedIndividual i) {
		super(o, i);
		this.setTableNameFromIndividual();
	}
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setTableNameFromIndividual() {
		String name = this.getIndividual().getIRI().toString();
//		System.out.println(name);
		name = name.split("__")[1];
		this.setTableName(name);
	}
	
//	public ArrayList<GenericClass> getClasses() {
//		return classes;
//	}
//
//	public void setClasses(ArrayList<GenericClass> classes) {
//		this.classes = classes;
//	}
//
//	public void addClass(GenericClass gc) {
//		this.classes.add(gc);
//	}
//	
	public TableType getTableType() {
		return this.tableType;
	}
	
	public void setTableType(TableType tableType) {
		this.tableType=tableType;
		switch (tableType){
		case SINGLE_ENTITY_TABLE:
			this.classAssertion(ClassIRI.SINGLE_ENTITY_TABLE);
			break;
		case MULTIPLE_ENTITIES_TABLE:
			this.classAssertion(ClassIRI.MULTIPLE_ENTITY_TABLE);
			break;
		case ENTITY_TABLE:
			this.classAssertion(ClassIRI.ENTITY_TABLE);
			break;
		default:
			System.out.println("[ERROR]");
		}
	}
}
