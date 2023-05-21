package movieapp.service.impl.jpa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import movieapp.persistence.repository.PersonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import movieapp.dto.PersonSimple;
import movieapp.persistence.entity.Person;
import movieapp.service.PersonService;

@Service
@Transactional
public class PersonServiceJpa implements PersonService {

	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public Optional<PersonSimple> getById(int id) {
		return personRepository.findById(id) // fetch opt entity artist
			.map(personEntity -> modelMapper.map(personEntity, PersonSimple.class)); // convert entity->dto
	}

	@Override
	public PersonSimple add(PersonSimple artist) {
		Person personEntityFromRepo = personRepository.save(
				// convert dto param to entity
				modelMapper.map(artist, Person.class));
		// convert dto param to entity
		return modelMapper.map(personEntityFromRepo, PersonSimple.class); // convert entity to dto result
	}

	@Override
	public List<PersonSimple> getByName(String name) {
		return personRepository.findByNameEndingWithIgnoreCase(name)
				.map(ae-> modelMapper.map(ae, PersonSimple.class))
				.collect(Collectors.toList());
	}

	@Override
	public Optional<PersonSimple> update(PersonSimple artist) {
		return personRepository.findById(artist.getId())
				.map(ae -> {
					modelMapper.map(artist, ae);
					return modelMapper.map(ae, PersonSimple.class);
				});
	}

	@Override
	public Optional<PersonSimple> delete(int id) {
		return personRepository.findById(id)
				.map(ae -> {
					personRepository.deleteById(id);
					return modelMapper.map(ae, PersonSimple.class);
				});
	}
}









