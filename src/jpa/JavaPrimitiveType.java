package jpa;

import genericcode.PrimitiveType;

public enum JavaPrimitiveType {
//	primitiveTypes.put("long",new PrimitiveType(this.ormfo,"primitive_type__long"));
//	primitiveTypes.put("Long",new PrimitiveType(this.ormfo,"primitive_type__Long"));
//	primitiveTypes.put("short",new PrimitiveType(this.ormfo,"primitive_type__short"));
//	primitiveTypes.put("String",new PrimitiveType(this.ormfo,"primitive_type__String"));
	
	
	LONG{
		public String toString(){
			return "long";
		}
		public String toIRI() {
			return "primitive_type__long";
		}
		public String toDjango() {
			return "models.BigIntegerField";
		}
	},
	STRING{
		public String toString(){
			return "String";
		}
		public String toIRI() {
			return "primitive_type__string";
		}
		public String toDjango() {
			return "models.CharField";
		}
	},
	SHORT{
		public String toString(){
			return "short";
		}
		public String toIRI() {
			return "primitive_type__short";
		}
		public String toDjango() {
			return "models.SmallIntegerField";
		}
	},
	BYTE{
		public String toString(){
			return "byte";
		}
		public String toIRI() {
			return "primitive_type__byte";
		}
		public String toDjango() {
			return "models.integerField";
		}
	},
	CHAR{
		public String toString(){
			return "char";
		}
		public String toIRI() {
			return "primitive_type__char";
		}
		public String toDjango() {
			return "models.CharField";
		}
	},
	DOUBLE{
		public String toString(){
			return "double";
		}
		public String toIRI() {
			return "primitive_type__double";
		}
		public String toDjango() {
			return "models.FloatField";
		}
	},
	FLOAT{
		public String toString(){
			return "float";
		}
		public String toIRI() {
			return "primitive_type__float";
		}
		public String toDjango() {
			return "models.FloatField";
		}
	},
	INT{
		public String toString(){
			return "int";
		}
		public String toIRI() {
			return "primitive_type__int";
		}
		public String toDjango() {
			return "models.IntegerField";
		}
	},
	BOOL{
		public String toString(){
			return "boolean";
		}
		public String toIRI() {
			return "primitive_type__boolean";
		}
		public String toDjango() {
			return "models.BooleanField";
		}
	};
	
	public static JavaPrimitiveType getJavaPrimitiveType(String codeType) {
		switch(codeType) {
			case "boolean":
				return BOOL;
			case "long":
				return LONG;
			case "Long":
				return LONG;
			case "byte":
				return BYTE;
			case "int":
				return INT;
			case "float":
				return FLOAT;
			case "String":
				return STRING;
			case "short":
				return SHORT;
			case "double":
				return DOUBLE;
			case "char":
				return CHAR;
			default:
				System.out.println("[WARN] Erro ao identificar o tipo primitivo (" + codeType + ")...");
		}
		return null;
		
	}
	
	public String toDjango() {
		return this.toDjango();
	}

	public String toIRI() {
		return this.toIRI();
	}

}
