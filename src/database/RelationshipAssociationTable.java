package database;

import org.semanticweb.owlapi.model.OWLOntology;

import ORM.RelationshipMapping;
import ORM.RelationshipType;
import OWL.ClassIRI;
import OWL.Individual;
import OWL.ObjectPropertyIRI;
import genericcode.GenericVariable;
import genericcode.GenericClass;

public class RelationshipAssociationTable extends Individual {
	
	RelationshipMapping relationshipMapping;
	String codeName;
	

	public RelationshipAssociationTable(OWLOntology o, RelationshipMapping rm) {
		super(o, "table__" + getRelationshipAssociationTableName(rm));
		this.relationshipMapping = rm;
		this.codeName = getRelationshipAssociationTableName(rm);
		this.classAssertion(ClassIRI.RELATIONSHIP_ASSOCIATION_ENTITY_TABLE);
		
//		if(rm.getRelationshipType() == RelationshipType.MANY_TO_MANY) {
//			rm.setObjectProperty(ObjectPropertyIRI.MANY_TO_MANY_ASSOCIATION_MAPPED_TO, this);
//		}
//		if(rm.getRelationshipType() == RelationshipType.ONE_TO_MANY) {
//			rm.setObjectProperty(ObjectPropertyIRI.ONE_TO_MANY_ASSOCIATION_MAPPED_TO, this);
//		}
		
		
	}
	
	static String getRelationshipAssociationTableName(RelationshipMapping rm) {
		String ret = "";
		GenericVariable gv = rm.getVariable();
		ret += ((GenericClass)gv.getValueType().getType()).getCodeName();
		ret +="_";
//		System.out.println(gv.get_class());
		ret += gv.get_class().getCodeName();
		return ret;
	}

}
