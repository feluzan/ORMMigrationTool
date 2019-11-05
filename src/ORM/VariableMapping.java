package ORM;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

import OWL.ClassIRI;
import OWL.Individual;
import OWL.ObjectPropertyIRI;
import database.Column;
import genericcode.GenericVariable;

public class VariableMapping extends Individual {
	
	private GenericVariable variable;
	private Column column;
	
	public VariableMapping(OWLOntology o, GenericVariable gv) {
		super(o, "variable_mapping__" + gv.getCodeName());
		
		this.variable = gv;
		gv.setVariableMapping(this);
		this.classAssertion(ClassIRI.VARIABLE_MAPPING);
		if(gv.isPk()) this.classAssertion(ClassIRI.PRIMARY_KEY_MAPPING);
		if(gv.isFk()) this.classAssertion(ClassIRI.FOREIGN_KEY_MAPPING);
	}

	public VariableMapping(OWLOntology o,OWLNamedIndividual i) {
		super(o,i);
	}
	
	public GenericVariable getVariable() {
		return variable;
	}
	public void setVariable(GenericVariable variable) {
		this.variable = variable;
	}

	public Column getColumn() {
		return column;
	}
	public void setColumn(Column column) {
		this.column = column;
		this.setObjectProperty(ObjectPropertyIRI.VARIABLE_MAPPED_TO, column);
		if(this.variable.isPk()) {
			this.setObjectProperty(ObjectPropertyIRI.PK_MAPPED_TO, column);
		}
		if(this.variable.isFk()) {
			this.setObjectProperty(ObjectPropertyIRI.FK_MAPPED_TO, column);
		}
	}
	
	

}
