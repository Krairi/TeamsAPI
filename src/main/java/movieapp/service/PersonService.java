package movieapp.service;

import java.util.List;
import java.util.Optional;

import movieapp.dto.PersonSimple;

public interface PersonService {
	// READ
	Optional<PersonSimple> getById(int id);
	List<PersonSimple> getByName(String name);
	// CREATE
	PersonSimple add(PersonSimple artist);
	// UPDATE
	Optional<PersonSimple> update(PersonSimple artist);
	// DELETE
	Optional<PersonSimple> delete(int id);
}
