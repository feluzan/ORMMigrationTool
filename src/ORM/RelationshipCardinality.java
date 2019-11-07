package ORM;


import org.semanticweb.owlapi.model.OWLOntology;

import OWL.ClassIRI;
import OWL.DataPropertyIRI;
import OWL.Individual;
import OWL.ObjectPropertyIRI;
import genericcode.GenericVariable;

public class RelationshipCardinality extends Individual {
	

	public RelationshipCardinality(OWLOntology o, GenericVariable gv) {
		super(o, "relationship_cardinality__" + gv.getCodeName());
		this.classAssertion(ClassIRI.RELATIONSHIP_CARDILANLITY);
		gv.setObjectProperty(ObjectPropertyIRI.HAS_CARDINALITY, this);		
		this.setDataProperty(DataPropertyIRI.RELATIONSHIP_CARDINALITY, gv.getRelationshipType().toString());
		gv.setRelationshipCardinality(this);

	}

}
