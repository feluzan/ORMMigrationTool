package OWL;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;

public enum ObjectPropertyIRI {
	
	ENTITY_CLASS_MAPPED_TO{
		public String toString() {
			return "#entity_class_mapped_to";
		}
	},
	
	ENTITY_CLASS_MAPPED_BY{
		public String toString() {
			return "#entity_class_mapped_by";
		}
	},
	
	SUBCLASS_MAPPED_BY{
		public String toString() {
			return "#subclass_mapped_by";
		}
	},
	
	SUPERCLASS_MAPPED_BY{
		public String toString() {
			return "#superclass_mapped_by";
		}
	},
	
	SINGLE_TABLE_INHERITANCE_MAPPED_TO{
		public String toString() {
			return "#single_table_inheritance_mapped_to";
		}
	},
	
	TABLE_PER_CLASS_INHERITANCE_MAPPED_TO{
		public String toString() {
			return "#table_per_class_inheritance_mapped_to";
		}
	},
	
	TABLE_PER_CONCRETE_CLASS_INHERITANCE_MAPPED_TO{
		public String toString() {
			return "#table_per_concrete_class_inheritance_mapped_to";
		}
	},
	
	REPRESENTS_RELATIONSHIP{
		public String toString() {
			return "#represents_relationship";
		}
	},
	
	RELATIONSHIP_REVERSE_OF{
		public String toString() {
			return "#relationship_reverse_of";
		}
	},
	
	VARIABLE_MAPPED_TO{
		public String toString() {
			return "#variable_mapped_to";
		}
	},
	
	PK_MAPPED_TO{
		public String toString() {
			return "#pk_mapped_to";
		}
	},
	
	FK_MAPPED_TO{
		public String toString() {
			return "#fk_mapped_to";
		}
	},
	
	VARIABLE_MAPPED_BY{
		public String toString() {
			return "#variable_mapped_by";
		}
	},
	
	PK_MAPPED_BY{
		public String toString() {
			return "#pk_mapped_by";
		}
	},
	
	FK_MAPPED_BY{
		public String toString() {
			return "#fk_mapped_by";
		}
	},
	
	IS_TYPE_OF{
		public String toString() {
			return "#is_type_of";
		}
	},
	
	REFERS_TO{
		public String toString() {
			return "#refers_to";
		}
	},
	
	DIRECTLY_RELATED_TO{
		public String toString() {
			return "#directly_related_to";
		}
	},
	
	HAS_CARDINALITY{
		public String toString() {
			return "#has_cardinality";
		}
	},
	
	MAPPED_BY{
		public String toString() {
			return "#mapped_by";
		}
	},
	
	BELONGS_TO{
		public String toString() {
			return "#belongs_to";
		}
	};
	
	public OWLObjectProperty getOWLObjectProperty(OWLOntology o) {
		IRI oIRI = o.getOntologyID().getOntologyIRI().get();
//		System.out.println(o.getOWLOntologyManager().getOWLDataFactory().getOWLDataProperty(oIRI + this.toString()));
		return o.getOWLOntologyManager().getOWLDataFactory().getOWLObjectProperty(oIRI + this.toString());
	}
	
	
}
