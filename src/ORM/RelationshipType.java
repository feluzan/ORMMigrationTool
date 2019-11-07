package ORM;

public enum RelationshipType {
	ONE_TO_ONE{
		public String toString() {
			return "One_To_One";
		}
	},
	
	ONE_TO_MANY{
		public String toString() {
			return "One_To_Many";
		}
	},
	
	MANY_TO_ONE{
		public String toString() {
			return "Many_To_One";
		}
	},
	
	MANY_TO_MANY{
		public String toString() {
			return "Many_To_Many";
		}
	};

}
