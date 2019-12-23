package model;

import java.util.List;

import javax.persistence.*;

@Entity
public class Class {
	
	@Id
	private Long id;
	
	private int year;
	
	@OneToMany
	private List<Studant> studants;
	
	@ManyToMany
	private List<Professor> professors;
	
	@ManyToOne
	private Institution institution;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public List<Studant> getStudants() {
		return studants;
	}

	public void setStudants(List<Studant> studants) {
		this.studants = studants;
	}

	public List<Professor> getProfessors() {
		return professors;
	}

	public void setProfessors(List<Professor> professors) {
		this.professors = professors;
	}

	public Institution getInstitution() {
		return institution;
	}

	public void setInstitution(Institution institution) {
		this.institution = institution;
	}
}
