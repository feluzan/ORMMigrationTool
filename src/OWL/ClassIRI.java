package OWL;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;

public enum ClassIRI {
	//============= OOC-O =================
	ABSTRACT_CLASS{
		public String toString() {
			return "#OOC-O::Abstract_Class";
		}
	},
	
	CLASS{
		public String toString() {
			return "#OOC-O::Class";
		}
		
	},
	
	INSTANCE_VARIABLE{
		public String toString() {
			return "#OOC-O::Instance_Variable";
		}
	},
	
	SUBCLASS{
		public String toString() {
			return "#OOC-O::Subclass";
		}
	},
	
	SUPERCLASS{
		public String toString() {
			return "#OOC-O::Superclass";
		}
	},
	
	//============= RDBS-O =============
	
	PRIMITIVE_TYPE{
		public String toString() {
			return "#OOC-O::Primitive_Type";
		}
	},
	
	VALUE_TYPE{
		public String toString() {
			return "#OOC-O::Value_Type";
		}
	},
	
	
	// ============ RDBS-O =================
	COLUMN{
		public String toString() {
			return "#RDBS-O::Column";
		}
	},
	
	PRIMARY_KEY_COLUMN{
		public String toString() {
			return "#RDBS-O::Primary_Key_Column";
		}
	},
	
	FOREIGN_KEY_COLUMN{
		public String toString() {
			return "#RDBS-O::Foreign_Key_Column";
		}
	},
	
	//============= ORMO-Class =============
	ENTITY_CLASS{
		public String toString() {
			return "#ORM-O::Entity_Class";
		}
	},
	
	ENTITY_TABLE{
		public String toString() {
			return "#ORM-O::Entity_Table";
		}
	},
	
	CLASS_MAPPING{
		public String toString() {
			return "#ORM-O::Class_Mapping";
		}
	},
	
	
	
	//============= ORM-O Variable =============
	MAPPED_VARIABLE{
		public String toString() {
			return "#ORM-O::Mapped_Variable";
		}
	},
	
	MAPPED_PRIMARY_KEY{
		public String toString() {
			return "#ORM-O::Mapped_Primary_Key";
		}
	},
	
	MAPPED_FOREIGN_KEY{
		public String toString() {
			return "#ORM-O::Mapped_Foreign_Key";
		}
	},
	
	VARIABLE_MAPPING{
		public String toString() {
			return "#ORM-O::Variable_Mapping";
		}
	},
	
	PRIMARY_KEY_MAPPING{
		public String toString() {
			return "#ORM-O::Primary_Key_Mapping";
		}
	},
	
	FOREIGN_KEY_MAPPING{
		public String toString() {
			return "#ORM-O::Foreign_Key_Mapping";
		}
	},
	
	
	//============= ORM-O Relationship =============
	ENTITY_CLASS_VALUE_TYPE{
		public String toString() {
			return "#ORM-O::Entity_Class_Value_Type";
		}
	},
	
	RELATIONSHIP_CARDILANLITY{
		public String toString() {
			return "#ORM-O::Relationship_Cardinality";
		}
	},
	
	
	//============= ORMO-O Inheritance =============
	ENTITY_SUBCLASS{
		public String toString() {
			return "#ORM-O::Entity_Subclass";
		}
	},
		
	ENTITY_SUPERCLASS{
		public String toString() {
			return "#ORM-O::Entity_Superclass";
		}
	},
	
	INHERITANCE_MAPPING{
		public String toString() {
			return "#ORM-O::Inheritance_Mapping";
		}
	},
	
	SINGLE_TABLE_INHERITANCE_MAPPING{
		public String toString() {
			return "#ORM-O::Single_Table_Inheritance_Mapping";
		}
	},
	
	TABLE_PER_CLASS_INHERITANCE_MAPPING{
		public String toString() {
			return "#ORM-O::Table_per_Class_Inheritance_Mapping";
		}
	},
	
	TABLE_PER_CONCRETE_CLASS_INHERITANCE_MAPPING{
		public String toString() {
			return "#ORM-O::Table_per_Concrete_Class_Inheritance_Mapping";
		}
	},

	SINGLE_ENTITY_TABLE{
		public String toString() {
			return "#ORM-O::Single_Entity_Table";
		}
	},
	
	MULTIPLE_ENTITY_TABLE{
		public String toString() {
			return "#ORM-O::Multiple_Entities_Table";
		}
	},
	
	RELATIONSHIP_ASSOCIATION_ENTITY_TABLE{
		public String toString() {
			return "#ORM-O::Relationship_Association_Table";
		}
	};
	
	
	
	public OWLClass getOWLClass(OWLOntology o) {
		IRI oIRI = o.getOntologyID().getOntologyIRI().get();
		return o.getOWLOntologyManager().getOWLDataFactory().getOWLClass(oIRI + this.toString());
	}
	
}
