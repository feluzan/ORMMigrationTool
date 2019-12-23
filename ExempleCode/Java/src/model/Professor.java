package model;

import java.util.List;

import javax.persistence.*;

@Entity
public class Professor extends Person {
	
	private float salary;
	
	@ManyToMany
	private List<Class> professorClasses;

	public float getSalary() {
		return salary;
	}

	public void setSalary(float salary) {
		this.salary = salary;
	}

	public List<Class> getProfessorClasses() {
		return professorClasses;
	}

	public void setProfessorClasses(List<Class> professorClasses) {
		this.professorClasses = professorClasses;
	}

}
