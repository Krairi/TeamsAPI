package movieapp.persistence.entity;

import java.time.LocalDate;
//import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "persons")
public class Person {
	private Integer id;
	private String name; // required 

	// Date, Calendar, LocalDate (J8)
	private LocalDate birthdate;
	
	public Person() {
		super();
	}
	
	public Person(String name) {
		this(null, name, null);
	}

	public Person(String name, LocalDate birthdate) {
		this(null, name, birthdate);
	}

	public Person(Integer id, String name, LocalDate birthdate) {
		this.id = id;
		this.name = name;
		this.birthdate = birthdate;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(nullable=false, length=100)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable=true)
	public LocalDate getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		return builder.append(name)
			.append("(")
			.append(birthdate)
			.append(")#")
			.append(id)
			.toString();
	}

}
