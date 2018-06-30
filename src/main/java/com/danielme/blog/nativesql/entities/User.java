package com.danielme.blog.nativesql.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedNativeQueries;
import javax.persistence.Table;

import com.danielme.blog.nativesql.UserDetail;

import javax.persistence.SqlResultSetMapping;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;


@SqlResultSetMapping(
        name = "DetailMapping",
        classes = @ConstructorResult(
        		targetClass = UserDetail.class,
                columns = {
                	@ColumnResult(name = "id", type=Long.class),
                    @ColumnResult(name = "concat")
                    }))
@NamedNativeQueries({
    @NamedNativeQuery(name = "selectFindById", query = "SELECT id, name, email FROM user WHERE id = ?", resultClass = User.class),
    @NamedNativeQuery(name = "selectFindAllDetail", query = "SELECT id, CONCAT(name, '-', email) AS concat FROM user", resultSetMapping = "DetailMapping")
})
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;    
    
    
	public User() {
		super();
	}

	public User(Long id, String name, String email) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
	}

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
  

    
}
