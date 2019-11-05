package ORM;

public enum RelationshipType {
	ONE_TO_ONE{
		public String toString() {
			return "One To One";
		}
	}, ONE_TO_MANY{
		public String toString() {
			return "One To Many";
		}
	}, MANY_TO_ONE{
		public String toString() {
			return "Many To One";
		}
	}, MANY_TO_MANY{
		public String toString() {
			return "Many To Many";
		}
	};

}
