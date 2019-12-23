from django.db import models

class Institution(models.Model):
	id = models.BigIntegerField(primary_key=True)
	models.IntegerField()
	name = models.CharField(max_lenght=255)
	address = models.OneToOneField('BusinessAddress', on_delete=models.CASCADE)

	class Meta:
		pass


class Person(models.Model):
	id = models.BigIntegerField(primary_key=True)
	name = models.CharField(max_lenght=255)
	address = models.OneToOneField('BusinessAddress', on_delete=models.CASCADE)

	class Meta:
		pass


class Class(models.Model):
	institution = models.ForeignKey('Institution', on_delete=models.CASCADE)
	id = models.BigIntegerField(primary_key=True)
	professors = models.ManyToManyField('Professor')
	models.IntegerField()
	year = models.IntegerField()

	class Meta:
		pass


class Address(models.Model):
	number = models.IntegerField()
	street = models.CharField(max_lenght=255)
	id = models.BigIntegerField(primary_key=True)
	city = models.CharField(max_lenght=255)

	class Meta:
		pass


class University(Institution):

	class Meta:
		db_table = 'University'
		pass


class BusinessAddress(Address):
	postalCode = models.IntegerField()

	class Meta:
		pass


class Professor(Person):
	professorClasses = models.ManyToManyField('Class')
	salary = models.FloatField()

	class Meta:
		db_table = 'Professor'
		pass


class Studant(Person):
	registry = models.IntegerField()
	studantClass = models.ForeignKey('Class', on_delete=models.CASCADE)

	class Meta:
		db_table = 'Studant'
		pass


class Highschool(Institution):

	class Meta:
		db_table = 'Highschool'
		pass


class PersonalAddress(Address):

	class Meta:
		pass


