package model;

import java.util.List;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Institution {
	
	@Id
	private Long id;
	private String name;
	
	@OneToOne
	private BusinessAddress address;
	
	@OneToMany
	private List<Class> institutionClasses;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BusinessAddress getAddress() {
		return address;
	}

	public void setAddress(BusinessAddress address) {
		this.address = address;
	}

	public List<Class> getInstitutionClasses() {
		return institutionClasses;
	}

	public void setInstitutionClasses(List<Class> institutionClasses) {
		this.institutionClasses = institutionClasses;
	}

}
