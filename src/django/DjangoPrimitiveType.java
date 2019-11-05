package django;

public enum DjangoPrimitiveType {
	
	AUTOFIELD{
		public String toString(){
						return "models.AutoField";
		}
		public String toIRI(){
			return "";
		}	
	},
	BIGAUTOFIELD{
		public String toString(){
			return "models.BigAutoField";
		}
		public String toIRI(){
			return "";
		}	
	},
	BIGINTEGERFIELD{
		public String toString(){
			return "models.BigIntegerField";
		}
		public String toIRI(){
			return "";
		}	
	},
	BINARYFIELD{
		public String toString(){
			return "models.BinaryField";
		}
		public String toIRI(){
			return "";
		}	
	},
	BOOLEANFIELD{
		public String toString(){
			return "models.BooleanField";
		}
		public String toIRI(){
			return "";
		}	
	},
	CHARFIELD{
		public String toString(){
			return "models.CharField";
		}
		public String toIRI(){
			return "";
		}	
	},
	DATEFIELD{
		public String toString(){
			return "models.DateField";
		}
		public String toIRI(){
			return "";
		}	
	},
	DATETIMEFIELD{
		public String toString(){
			return "models.DateTimeField";
		}
		public String toIRI(){
			return "";
		}	
	},
	DECIMALFIELD{
		public String toString(){
			return "models.DecimalField";
		}
		public String toIRI(){
			return "";
		}	
	},
	DURATIONFIELD{
		public String toString(){
			return "models.DurationField";
		}
		public String toIRI(){
			return "";
		}	
	},
	EMAILFIELD{
		public String toString(){
			return "models.EmailField";
		}
		public String toIRI(){
			return "";
		}	
	},
	FILEFIELD{
		public String toString(){
			return "models.FileField";
		}
		public String toIRI(){
			return "";
		}	
	},
	FIELDFILE{
		public String toString(){
			return "models.FieldFile";
		}
		public String toIRI(){
			return "";
		}	
	},
	FILEPATHFIELD{
		public String toString(){
			return "models.FilePathField";
		}
		public String toIRI(){
			return "";
		}	
	},
	FLOATFIELD{
		public String toString(){
			return "models.FloatField";
		}
		public String toIRI(){
			return "";
		}	
	},
	IMAGEFIELD{
		public String toString(){
			return "models.ImageField";
		}
		public String toIRI(){
			return "";
		}	
	},
	INTEGERFIELD{
		public String toString(){
			return "models.IntegerField";
		}
		public String toIRI(){
			return "";
		}	
	},
	GENERICIPADDRESSFIELD{
		public String toString(){
			return "models.GenericIPAddressField";
		}
		public String toIRI(){
			return "";
		}	
	},
	NULLBOOLEANFIELD{
		public String toString(){
			return "models.NullBooleanField";
		}
		public String toIRI(){
			return "";
		}	
	},
	POSITIVEINTEGERFIELD{
		public String toString(){
			return "models.PositiveIntegerField";
		}
		public String toIRI(){
			return "";
		}	
	},
	POSITIVESMALLINTEGERFIELD{
		public String toString(){
			return "models.PositiveSmallIntegerField";
		}
		public String toIRI(){
			return "";
		}	
	},
	SLUGFIELD{
		public String toString(){
			return "models.SlugField";
		}
		public String toIRI(){
			return "";
		}	
	},
	SMALLINTEGERFIELD{
		public String toString(){
			return "models.SmallIntegerField";
		}
		public String toIRI(){
			return "";
		}	
	},
	TEXTFIELD{
		public String toString(){
			return "models.TextField";
		}
		public String toIRI(){
			return "";
		}	
	},
	TIMEFIELD{
		public String toString(){
			return "models.TimeField";
		}
		public String toIRI(){
			return "";
		}	
	},
	URLFIELD{
		public String toString(){
			return "models.URLField";
		}
		public String toIRI(){
			return "";
		}	
	},
	UUIDFIELD{
		public String toString(){
			return "models.UUIDField";
		}
		public String toIRI(){
			return "";
		}	
	};

}
