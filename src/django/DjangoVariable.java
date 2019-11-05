package django;

import java.util.ArrayList;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

import ORM.RelationshipType;
import genericcode.GenericClass;
import genericcode.GenericVariable;
import genericcode.PrimitiveType;
import genericcode.Type;
import jpa.JavaPrimitiveType;

public class DjangoVariable extends GenericVariable {

	public DjangoVariable(OWLOntology o, String iri) {
		super(o, iri);
		// TODO Auto-generated constructor stub
	}
	
	public DjangoVariable(OWLOntology o,OWLNamedIndividual i) {
		super(o, i);
		// TODO Auto-generated constructor stub
	}

	public String toCode() {
		
		if(!this.isMapped()) return "";

//		System.out.println("Gerando código de variável...");
		String ret = "";
		ArrayList<String> parameters = new ArrayList<String>();
		
		if(this.isPk()) {
			parameters.add("primary_key=True");
		}
		
		Type type = this.getValueType().getType();
		
		String codeType;
		
		if(type instanceof PrimitiveType) {
			ret+=this.getCodeName() + " = ";
			codeType = (JavaPrimitiveType.getJavaPrimitiveType(((PrimitiveType)type).getTypeName())).toDjango();
			ret+=codeType;
			if(codeType.equals("models.CharField")) parameters.add("max_lenght=255");
			
		}else {
			if(type instanceof GenericClass) {
				
				GenericClass gc = (GenericClass)type;
				ret+=this.getCodeName() + " = ";
				String classCodeName = gc.getCodeName();
//				System.out.println("Classe: " + classCodeName);
				RelationshipType rt = this.getRelationshipMapping().getRelationshipType();
//				System.out.println(rt);
				switch(rt) {
					case MANY_TO_MANY:
						ret += "models.ManyToManyField";
						parameters.add("'" + gc.getCodeName() + "'");
						break;
					case MANY_TO_ONE:
						ret += "models.ForeignKey";
						parameters.add("'" + gc.getCodeName() + "'");
						parameters.add("on_delete=models.CASCADE");
						break;
					case ONE_TO_MANY:
						ret = "models.IntegerField"; 
			   			System.out.println("[WARN] Tipo " + gc.getCodeName() + " mapeado para Integer.");
						break;
					case ONE_TO_ONE:
						ret += "models.OneToOneField";
						parameters.add("'" + gc.getCodeName() + "'");
						parameters.add("on_delete=models.CASCADE");
						break;
					default:
//						System.out.println("Defaaaault............................");
							
				}
			}else {
				System.out.println("[ERROR] Problema ao identificar o tipo da variável.");
				System.out.println("\tO programa será encerrado.");
				System.exit(1);
			}
		}
		
		ret += "(" + String.join(", ", parameters) + ")";
		ret += "\n";
		return ret;
	}
}
