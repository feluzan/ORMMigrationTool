package OWL;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLOntology;

public enum DataPropertyIRI {
	
	TYPE_NAME{
		public String toString() {
			return "#type_Name";
		}
	},
	VARIABLE_NAME{
		public String toString() {
			return "#variable_Name";
		}
	},
	COLUMN_NAME{
		public String toString() {
			return "#column_Name";
		}
	},
	TABLE_NAME{
		public String toString() {
			return "#table_name";
		}
	};
	
	
	public OWLDataProperty getOWLDataProperty(OWLOntology o) {
		IRI oIRI = o.getOntologyID().getOntologyIRI().get();
//		System.out.println(o.getOWLOntologyManager().getOWLDataFactory().getOWLDataProperty(oIRI + this.toString()));
		return o.getOWLOntologyManager().getOWLDataFactory().getOWLDataProperty(oIRI + this.toString());
	}
}
