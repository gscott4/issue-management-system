package org.jee8ng.issues.entity;

import java.io.Serializable;
import java.util.List;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="user_detail")
public class User implements Serializable {
	
	@Id
	private Long id;
	
	@NotNull
	@Size(min=2, max=20)
	private String name;
	
	@JsonbTransient
	@OneToMany(mappedBy = "assignedTo")
	private List<Issue> issues;
	
	public User(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public User() {}

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

	public List<Issue> getIssues() {
		return issues;
	}

	public void setIssues(List<Issue> issues) {
		this.issues = issues;
	}
	
	
}
