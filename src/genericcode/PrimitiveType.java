package genericcode;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

import OWL.ClassIRI;

public class PrimitiveType extends Type {
	
	private String typeName;

	public PrimitiveType(OWLOntology o, String iri) {
		super(o, iri);
		
		this.classAssertion(ClassIRI.PRIMITIVE_TYPE);
		// TODO Auto-generated constructor stub
	}

	
	public PrimitiveType(OWLOntology o, OWLNamedIndividual i) {
		super(o,i);
	}


	public String getTypeName() {
		return typeName;
	}


	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	
	
//	public String java2Django() {
//		
//	}

}
