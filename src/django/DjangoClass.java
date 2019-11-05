package django;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

import ORM.InheritanceMapping;
import ORM.InheritanceStrategy;
import genericcode.GenericClass;
import genericcode.GenericVariable;

public class DjangoClass extends GenericClass {

	public DjangoClass(OWLOntology o, OWLNamedIndividual i) {
		super(o, i);
//		i.
	}

	@Override
	public String getSuperclassName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InheritanceStrategy getCodeInheritanceStrategy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCodeTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String toCode(InheritanceMapping im) {
		String ret = "";
		
		String superclass = "models.Model";
		
		String metaClass = "\tclass Meta:\n";
		
		if(this.isSubclass()) {
			superclass = this.getSuperclass().getCodeName();
			
			
			switch(im.getInheritanceStrategy()) {
				case TABLE_PER_CONCRETE_CLASS:
					String tableName = this.getClassMapping().getTable().getTableName();
					if(!this.getCodeName().equals(tableName)) {
						metaClass += "\t\tdb_table = '" + tableName + "'\n";	
					}
					break;
				default:
					System.out.println("[INFO]\tEstrategia de herança " + im.getInheritanceStrategy() + " não suportada.");
					System.out.println("\tPadrão do Django utilizada: Table per Concrete Class.");
					metaClass += "\t\tdb_table = '" + this.getCodeName() + "'\n";
					break;
			}
		}

		ret += "class " + this.getCodeName() + "(" + superclass + "):\n";

		
		for(GenericVariable v : this.getVariables()) {
			if(v.isMapped()) ret += "\t" + ((DjangoVariable)v).toCode();
			
		}
		
		metaClass+="\t\tpass\n";
		ret+="\n"+metaClass;
		ret +="\n\n";
		return ret;
	}

	@Override
	public String toCode() {
		return this.toCode(null);

	}
}
