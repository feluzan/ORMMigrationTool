package model;

import javax.persistence.*;

@Entity
public class Student extends Person {
	
	private int registry;
	
	@ManyToOne
	private Class studentClass;

	public int getRegistry() {
		return registry;
	}

	public void setRegistry(int registry) {
		this.registry = registry;
	}

	public Class getStudentClass() {
		return studentClass;
	}

	public void setStudentClass(Class studentClass) {
		this.studentClass = studentClass;
	}
}
