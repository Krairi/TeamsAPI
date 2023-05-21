package movieapp.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import movieapp.persistence.entity.Person;

class TestArtist {

	// TODO : unit tests of the java bean
	
	@Test
	void test() {
		var artists = List.of(
				new Person("Steve McQueen", LocalDate.of(1930, 3, 24)),
				new Person("Steve McQueen", LocalDate.of(1969, 10, 9)),
				new Person("Alfred Hitchcock"),
				new Person());
		// System.out.println(artists);
		// TODO
	}

}
