package database;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

import ORM.VariableMapping;
import OWL.ClassIRI;
import OWL.DataPropertyIRI;
import OWL.Individual;

public class Column extends Individual {
	
	private String codeName;
	private VariableMapping variableMapping;

	public Column(OWLOntology o, VariableMapping vm) {
		super(o, "column__" + vm.getVariable().getCodeName());
		this.setDataProperty(DataPropertyIRI.COLUMN_NAME, vm.getVariable().getCodeName());
		this.variableMapping = vm;
		vm.setColumn(this);
		
		this.classAssertion(ClassIRI.COLUMN);
		if(vm.getVariable().isPk()) this.classAssertion(ClassIRI.PRIMARY_KEY_COLUMN);
		if(vm.getVariable().isFk()) this.classAssertion(ClassIRI.FOREIGN_KEY_COLUMN);
	}

	public Column(OWLOntology o,OWLNamedIndividual i) {
		super(o, i);
		this.setCodeNameFromIndividual();
	}
	
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public VariableMapping getVariableMapping() {
		return variableMapping;
	}
	public void setVariableMapping(VariableMapping variableMapping) {
		this.variableMapping = variableMapping;
	}

	public void setCodeNameFromIndividual() {
		String name = this.getIndividual().getIRI().toString();
//		System.out.println(name);
		name = name.split("__")[1];
		this.setCodeName(name);
	}
	
}
