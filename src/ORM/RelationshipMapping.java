package ORM;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

import OWL.ClassIRI;
import OWL.Individual;
import OWL.ObjectPropertyIRI;
import genericcode.GenericVariable;
import genericcode.GenericClass;

public class RelationshipMapping extends Individual {
	
	private GenericVariable variable;
	private RelationshipType relationshipType;
//	private Table relationshipAssociationTable;
	private RelationshipMapping reverse;

	public RelationshipMapping(OWLOntology o, GenericVariable gv) {
		super(o, "relationship_mapping__" + gv.getCodeName() + "__" + ((GenericClass)gv.getValueType().getType()).getCodeName());
		
		this.relationshipType = gv.getRelationshipType();
//		switch(gv.getRelationshipType()) {
//			case MANY_TO_MANY:
//				this.classAssertion(ClassIRI.MANY_TO_MANY_RELATIONSHIP_MAPPING);
//				break;
//				
//			case MANY_TO_ONE:
//				this.classAssertion(ClassIRI.MANY_TO_ONE_RELATIONSHIP_MAPPING);
//				break;
//				
//			case ONE_TO_MANY:
//				this.classAssertion(ClassIRI.ONE_TO_MANY_RELATIONSHIP_MAPPING);
//				break;
//				
//			case ONE_TO_ONE:
//				this.classAssertion(ClassIRI.ONE_TO_ONE_RELATIONSHIP_MAPPING);
//				break;
//				
//			default:
//				break;
//			
//		}
		gv.setRelationshipMapping(this);
		
		this.variable = gv;
		gv.setObjectProperty(ObjectPropertyIRI.REPRESENTS_RELATIONSHIP, this);
	}

	
	public RelationshipMapping(OWLOntology o,OWLNamedIndividual i) {
		super(o, i);
	}
	public RelationshipMapping getReverse() {
		return reverse;
	}
	public void setReverse(RelationshipMapping reverse) {
		this.reverse = reverse;
		this.setObjectProperty(ObjectPropertyIRI.RELATIONSHIP_REVERSE_OF, reverse);
	}

	public GenericVariable getVariable() {
		return this.variable;
	}
	public void setVariable(GenericVariable gv) {
		this.variable = gv;
	}
	
	public RelationshipType getRelationshipType() {
		return relationshipType;
	}
	public void setRelationshipType(RelationshipType rt) {
		this.relationshipType = rt;
	}

}
