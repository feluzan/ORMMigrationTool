package jpa;

import java.util.List;

import org.semanticweb.owlapi.model.OWLOntology;


import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;

import ORM.RelationshipType;
import OWL.ClassIRI;
import OWL.ObjectPropertyIRI;
import genericcode.GenericClass;
import genericcode.GenericVariable;

public class JavaVariable extends GenericVariable {
	
	private List<AnnotationExpr> annotations = null;
	private String codeType;
	private AnnotationExpr relationshipAnnotation;
//	private NodeList<Modifier> modifiers = null;
	
	public JavaVariable(OWLOntology o, GenericClass c, FieldDeclaration node) {
		super(o, "variable__" + c.getCodeName() + "." + getFieldNodeStringName(node));
		
		VariableDeclarator variable = ((FieldDeclaration) node).getVariables().get(0);
		this.setCodeName(variable.getNameAsString());
		
		this.annotations = ((BodyDeclaration<FieldDeclaration>) node).getAnnotations();
		
		this.setIsMapped();
		this.setIsPk();
		this.setIsFk();
		this.classAssertion(ClassIRI.INSTANCE_VARIABLE);
		if(this.isMapped()) {
			this.classAssertion(ClassIRI.MAPPED_VARIABLE);
		}
		if(this.isPk()) {
			this.classAssertion(ClassIRI.MAPPED_PRIMARY_KEY);
		}
		if(this.isFk()) {
			this.classAssertion(ClassIRI.MAPPED_FOREIGN_KEY);
		}
		
		this.codeType = variable.getTypeAsString();
		this.set_class(c);
		this.setObjectProperty(ObjectPropertyIRI.BELONGS_TO, c);

	}
	
	static String getFieldNodeStringName(FieldDeclaration node) {
		String ret = "";
		VariableDeclarator variable = ((FieldDeclaration) node).getVariables().get(0);
		ret += variable.getNameAsString();
		return ret;
		
	}
	
	private void setIsMapped() {
		this.setMapped(true);
		for (AnnotationExpr ann : this.annotations) {
			if (ann.getNameAsString().equals("Transient")) {
				this.setMapped(false);
			}
		}
		
	}

	private void setIsPk() {
		this.setPk(false);
		for (AnnotationExpr ann : this.annotations) {
			if (ann.getNameAsString().equals("Id")) {
				this.setPk(true);
			}
		}
		
	}
	
	private void setIsFk() {
		this.setFk(false);
		for (AnnotationExpr ann : this.annotations) {
			switch(ann.getNameAsString()) {
			case "OneToOne":
				this.setFk(true);
				this.setRelationshipType(RelationshipType.ONE_TO_ONE);
				this.relationshipAnnotation = ann;
				break;
			case "OneToMany":
				this.setFk(true);
				this.setRelationshipType(RelationshipType.ONE_TO_MANY);
				this.relationshipAnnotation = ann;
				break;
			case "ManyToOne":
				this.setFk(true);
				this.setRelationshipType(RelationshipType.MANY_TO_ONE);
				this.relationshipAnnotation = ann;
				break;
			case "ManyToMany":
				this.setFk(true);
				this.setRelationshipType(RelationshipType.MANY_TO_MANY);
				this.relationshipAnnotation = ann;
				break;
			default:
				break;
			}
		}
		
	}
	
	public String getMappedBy() {
		
//		System.out.println(this.getCodeName() + " ---------");
		
		List<MemberValuePair> members = this.relationshipAnnotation.findAll(MemberValuePair.class);
		for(MemberValuePair m : members) {
			if(m.getName().toString().equals("mappedBy")) {
				return m.getValue().toString().replace("\"", "");
			}
		}
		return null;
	}
	
	public String getCodeType() {
		return this.codeType;
	}

	@Override
	public String toCode() {
		// TODO Auto-generated method stub
		return null;
	}
}
