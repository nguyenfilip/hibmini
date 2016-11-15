package cz.nguyen.entity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Employee {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	

	@Column(unique=true)
	private String name;
	
	/*
	 * The day this item has been added to the eshop
	 */
	@Temporal(TemporalType.DATE)
	private java.util.Date addedDate;
	
	@ManyToMany
	private Set<Company> categories = new HashSet<Company>();



	
	public void setId(Long id){
		this.id = id;
	}
	
	public void removeCategory(Company category)
	{
		this.categories.remove(category);
	}

	public Set<Company> getCategories() {
		return Collections.unmodifiableSet(categories);
	}
	
	public void addCategory(Company c) {
		categories.add(c);
		c.addProduct(this);
	}


	public java.util.Date getAddedDate() {
		return addedDate;
	}
	public void setAddedDate(java.util.Date addedDate) {
		this.addedDate = addedDate;
	}
	public Employee(Long productId) {
		this.id = productId;
	}
	public Employee() {
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public Long getId() {
		return id;
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
		if (!(obj instanceof Employee))
			return false;
		Employee other = (Employee) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.getName()))
			return false;
		return true;
	}
	
}
