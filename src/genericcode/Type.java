package genericcode;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

import OWL.Individual;

//import owlcode.OWLClass;
//import org.semanticweb.owlapi.apibinding.OWLManager;
//import org.semanticweb.owlapi.model.IRI;
//import org.semanticweb.owlapi.model.OWLClass;
//import org.semanticweb.owlapi.model.OWLDataFactory;
//import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
//import org.semanticweb.owlapi.model.OWLOntology;
//import org.semanticweb.owlapi.model.OWLOntologyCreationException;
//import org.semanticweb.owlapi.model.OWLOntologyManager;

public abstract class Type extends Individual{
		
	public Type(OWLOntology o, String iri) {
		super(o,iri);

	}
	
	public Type(OWLOntology o,OWLNamedIndividual i) {
		super(o, i);
	}


}
