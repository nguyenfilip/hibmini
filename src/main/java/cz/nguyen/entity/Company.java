package cz.nguyen.entity;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

@Entity
public class Company {
	
	@Id
	@GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;
		
	private String name;
	
	
	@OneToMany(mappedBy = "company")
	private Set<Employee> employees = new HashSet<>();
	
//    @OneToMany()
//    private List<Building> buildings = new ArrayList<>();


//    public void addBuilding(Building b) {
//        buildings.add(b);
//    }

    
	public Set<Employee> getEmployees() {
        return employees;
    }
	
    public void addEmployee(Employee e) {
        employees.add(e);
        e.setCompany(this);
    }

	public String getId() {
        return id;
    }
	
	public void setId(String id) {
        this.id = id;
    }

	public Company() {
	}

	public Company(String name) {
        this.name = name;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (! (obj instanceof Company))
			return false;
		Company other = (Company) obj;
		if (name == null) {
			if (other.getName() != null)
				return false;
		} else if (!name.equals(other.getName()))
			return false;
		return true;
	}

    @Override
    public String toString() {
        return "Company [name=" + name + "]";
    }


	
	
	
}
