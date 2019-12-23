package model;

import javax.persistence.*;

@Entity
public class Studant extends Person {
	
	private int registry;
	
	@ManyToOne
	private Class studantClass;

	public int getRegistry() {
		return registry;
	}

	public void setRegistry(int registry) {
		this.registry = registry;
	}

	public Class getStudantClass() {
		return studantClass;
	}

	public void setStudantClass(Class studantClass) {
		this.studantClass = studantClass;
	}
}
