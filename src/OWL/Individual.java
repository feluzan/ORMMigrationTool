package OWL;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

public class Individual {
	private OWLOntology ontology;
	private OWLNamedIndividual individual;
	
	public Individual(OWLOntology o, String iri) {
		this.ontology=o;
		OWLDataFactory df = o.getOWLOntologyManager().getOWLDataFactory();
		this.individual = df.getOWLNamedIndividual(IRI.create("#"+iri));
		o.add(df.getOWLDeclarationAxiom(this.individual));
//		this.assertion = df.getOWLClassAssertionAxiom(c, this.individual);
	}
	
	public Individual(OWLOntology o, OWLNamedIndividual i) {
		this.ontology = o;
		this.individual = i;
	}
	
	public OWLNamedIndividual getIndividual() {
		return this.individual;
	}
	
	public void classAssertion(OWLClass c) {
		OWLDataFactory df = this.ontology.getOWLOntologyManager().getOWLDataFactory();
		OWLClassAssertionAxiom ca = df.getOWLClassAssertionAxiom(c, this.individual);
		this.ontology.add(ca);
	}
	
	public void classAssertion(ClassIRI classIRI) {
//		System.out.println("Class Assertion: " + classIRI);
		OWLDataFactory df = this.ontology.getOWLOntologyManager().getOWLDataFactory();
		OWLClass c = df.getOWLClass(classIRI.toString());
		OWLClassAssertionAxiom ca = df.getOWLClassAssertionAxiom(c, this.individual);
		this.ontology.add(ca);
//		System.out.println(ca.toString());
	}
	
	public void setObjectProperty(ObjectPropertyIRI propertyIRI, Individual range) {
		OWLDataFactory df = this.ontology.getOWLOntologyManager().getOWLDataFactory();
		OWLObjectProperty property = df.getOWLObjectProperty(propertyIRI.toString());
		OWLObjectPropertyAssertionAxiom pa = df.getOWLObjectPropertyAssertionAxiom(property, this.individual, range.getIndividual());
		this.ontology.add(pa);
	}
	
	public void setDataProperty(DataPropertyIRI dataPropertyIRI, String value) {
		OWLDataFactory df = this.ontology.getOWLOntologyManager().getOWLDataFactory();
		OWLDataProperty property = df.getOWLDataProperty(dataPropertyIRI.toString());
		OWLDataPropertyAssertionAxiom pa = df.getOWLDataPropertyAssertionAxiom(property, this.individual, value);
		this.ontology.add(pa);
	}

	public OWLOntology getOntology() {
		return ontology;
	}

}
