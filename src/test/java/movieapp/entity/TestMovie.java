package movieapp.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import movieapp.persistence.entity.Movie;

class TestMovie {

	@Test
	void testDefaultConstructor() {
		Movie movie = new Movie();
		// System.out.println(movie);
		assertAll(
				() -> assertNull(movie.getId(), "default movie id"),
				() -> assertNull(movie.getTitle(), "default movie title"),
				() -> assertEquals(null, movie.getYear(), "default movie year"),
				() -> assertEquals(null, movie.getDuration(), "default movie duration"));
	}
	
	@Test
	void testAllArgsConstructor() {
		// given
		String title = "Kabir Singh";
		short year = 2019;
		short duration = 173;
		// when
		Movie movie = new Movie(null, title, year, duration, null, null, null, null, null, null, null);
		// then
		assertAll(
				() -> assertNull(movie.getId(), "default movie id"),
				() -> assertEquals(title, movie.getTitle(), "movie title"),
				() -> assertEquals(year, movie.getYear(), "movie year"),
				() -> assertEquals(duration, movie.getDuration(), "movie duration"));
	}

}
