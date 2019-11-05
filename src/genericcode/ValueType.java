package genericcode;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

import OWL.ClassIRI;
import OWL.DataPropertyIRI;
import OWL.Individual;
import OWL.ObjectPropertyIRI;

public class ValueType extends Individual {
	
	Type type;
	GenericVariable variable;

	public ValueType(OWLOntology o, GenericVariable gv, Type t) {
		super(o, "value_type__" + gv.getCodeName());

		gv.setValueType(this);
		this.variable = gv;
		this.classAssertion(ClassIRI.VALUE_TYPE);
		
		this.setType(t);
		this.setObjectProperty(ObjectPropertyIRI.REFERS_TO, t);
		
		
	}

	public ValueType(OWLOntology o, OWLNamedIndividual i) {
		super(o,i);
		// TODO Auto-generated constructor stub
	}

	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
		this.setObjectProperty(ObjectPropertyIRI.REFERS_TO, type);
	}

	public GenericVariable getVariable() {
		return variable;
	}
	public void setVariable(GenericVariable variable) {
		this.variable = variable;
	}

	
}
