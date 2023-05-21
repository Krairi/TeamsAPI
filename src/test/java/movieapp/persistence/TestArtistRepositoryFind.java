package movieapp.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import movieapp.persistence.entity.Person;
import movieapp.persistence.repository.PersonRepository;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class TestArtistRepositoryFind {

	@Autowired
	PersonRepository artistRepository;
	
	@Autowired
	EntityManager entityManager;
	
	List<Person> artists;
	List<Integer> ids;
	
	@BeforeEach
	void initData() {
		// write data in database (via hibernate entity manager)
		artists = List.of(
				new Person("Steve McQueen", LocalDate.of(1930, 3, 24)),
				new Person("Steve McQueen", LocalDate.of(1969, 10, 9)),
				new Person("Alfred Hitchcock"),
				new Person("Steven R. McQueen"));
		artists.forEach(entityManager::persist);
		entityManager.flush();
		ids = artists.stream()
				// .map(a -> a.getId())
				.map(Person::getId)
				.collect(Collectors.toList());
	}
	
	
	@Test
	void testFindAll() {
		// read data from database (via spring jpa repository)
		var artistsFound1 = artistRepository.findAll();
		assertEquals(artists.size(), artistsFound1.size(), "number of artists");
	}
	
	@Test
	void testFindById() {
		var id = ids.get(0);
		var artistFound = artistRepository.findById(id);
		System.out.println(artists);
		System.out.println(ids);
		System.out.println(artistFound);
		assertTrue(artistFound.isPresent(), "artist found");
		artistFound.ifPresent(
				a -> assertEquals(id, a.getId(), "id artist"));
	}
	
	@Test
	void testFindAllById() {
		var idSelection = List.of(ids.get(0), ids.get(2));
		var artistsFound = artistRepository.findAllById(idSelection);
		assertAll(
				() -> assertEquals(2, artistsFound.size(), "number of artists")
				// TOODO: ids ok
		);
	}
	
	@Test
	void testGetOne() {
		int index = 0;
		var id = ids.get(index);
		var artistsFound3 = artistRepository.getOne(id);
		assertAll(
				() -> assertEquals(id, artistsFound3.getId(), "id"),
				() -> assertEquals(artists.get(index).getName(), artistsFound3.getName(), "name")
		);
	}
	
	@Test
	void testFindByNameIgnoreCase() {
		String name = "Steve McQueen";
		var artists = artistRepository.findByNameIgnoreCase(name);
		assertAll(artists.stream()
			.map(a -> () -> assertEquals(name, a.getName(), "name")));
	}
	
	@Test
	void testFindByNameEndingWithIgnoreCase() {
		String name = "mcqueen";
		var artists = artistRepository.findByNameEndingWithIgnoreCase(name);
		assertAll(artists
				.map(a -> () -> assertTrue(
						a.getName().toLowerCase().endsWith(name),
						"name ending with " + name)));
	}
}




