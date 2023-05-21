package movieapp.persistence;

import static org.junit.jupiter.api.Assertions.*;
import static testing.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;

import movieapp.persistence.entity.Movie;
import movieapp.persistence.provider.MovieProvider;
import movieapp.persistence.repository.MovieRepository;
import testing.persistence.DatabaseUtils;

@DataJpaTest // active Spring Data avec sa couche JPA Hibernate
@AutoConfigureTestDatabase(replace = Replace.NONE) // deactivate H2 +
@ActiveProfiles("test") // + DB from application-test.properties
class TestMovieRepository {

	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private TestEntityManager entityManager;

	@Test
	void testCount() {
		// TODO: write data in DB
		long nb_movies = movieRepository.count();
		// System.out.println(nb_movies);
		// TODO: assert nb_movies is good
	}
	
	@Test
	void testFindByTitle() {
		// giving
		// 1 - a title of movies to read in the test  
		var title = "The Man Who Knew Too Much";
		var otherTitle = "The Man Who Knew Too Little";
		var goodTitles = List.of(title, title);
		var goodYears = List.of((short) 1934, (short) 1956);
		// 2 - writing data in database via the entity manager
		var moviesDatabase = MovieProvider.moviesGoodOnesOneBad(
				goodTitles, goodYears, otherTitle, (short) 1997);
		DatabaseUtils.insertDataFlushAndClearCache(entityManager, moviesDatabase);
		// when : read from the repository
		var moviesFound = movieRepository.findByTitle(title)
				.collect(Collectors.toList());
		var movieTitlesFound = moviesFound.stream()
				.map(Movie::getTitle)
				.collect(Collectors.toList());
		var movieYearsFound = moviesFound.stream()
				.map(Movie::getYear)
				.collect(Collectors.toList());
		// then 
		assertSizeEquals(goodTitles, movieTitlesFound, "number of titles");
		assertAllEquals(title, movieTitlesFound, "title");
		assertCollectionUniqueElementEquals(goodYears, movieYearsFound, "year");
	}
	
	@Test
	void testFindByTitleContainingIgnoreCase() {
		// giving
		// 1 - a title of movies to find in the database  
		var titlePart = "mAn";
		// 2 - writing data in database via the entity manager
		var titlesContaingWord = List.of(
				"The Man Who Knew Too Much",
				"The Invisible Man", 
				"Wonder Woman 1984");
		var moviesDatabase = MovieProvider.moviesGoodOnesOneBad(
				titlesContaingWord,
				List.of((short) 1934, (short) 2020, (short) 2020),
				"Men In Black", (short) 1997);
		DatabaseUtils.insertDataFlushAndClearCache(entityManager, moviesDatabase);
		// when : read from the repository
		var movieTitlesFound = movieRepository.findByTitleContainingIgnoreCase(titlePart)
				.map(Movie::getTitle)
				.collect(Collectors.toList());
		// then 
		var titlePartLowerCase = titlePart.toLowerCase();
		assertSizeEquals(titlesContaingWord, movieTitlesFound, "number of movies");
		assertAllTrue(
				movieTitlesFound, 
				t -> t.toLowerCase().contains(titlePartLowerCase),
				t -> titlePartLowerCase + " not in title " + t);
	}
	
	@Test
	void testFindByYearBetween() {
		// giving
		// 1 - a title of movies to read int the test  
		short yearMin = 1977;
		short yearMax = 1995;
		// 2 - writing data in database via the entity manager
		var goodTitles = List.of(
				"GoldenEye", "Licence To Kill", "The Spy Who Loved Me"); 
		var moviesDatabase = List.of(
				Movie.builder()
						.title("Dr No")
						.year((short) 1962)
						.build(), // nok
				Movie.builder()
						.title("Licence To Kill")
						.year((short) 1989)
						.build(), // ok
				Movie.builder()
						.title("The Spy Who Loved Me")
						.year((short) 1977)
						.build(), // ok
				Movie.builder()
						.title("GoldenEye")
						.year((short) 1995)
						.build(), // ok
				Movie.builder()
						.title("Spectre")
						.year((short) 2015)
						.build()
		); // nok
		DatabaseUtils.insertDataFlushAndClearCache(entityManager, moviesDatabase);
		// when : read from the repository
		var moviesFound = movieRepository.findByYearBetweenOrderByYear(yearMin, yearMax)
				.collect(Collectors.toList());
		// then 
		assertSizeEquals(goodTitles, moviesFound, "number of movies");
		assertAllTrue(moviesFound,
			m -> (m.getYear() >= yearMin) && (m.getYear() <= yearMax),
			m -> "year " + m.getYear() + " not in interval ["
					 + yearMin + "-" +yearMax +"]");
	}
	
	@Test
	void testFindByYearOrderByTitle() {
		short year = 2020;
		List<Movie> moviesDatabase = List.of(
				Movie.builder()
						.title("Dr No")
						.year((short) 1962)
						.build(),
				Movie.builder()
						.title("The Invisible Man")
						.year((short) 2020)
						.build(),
				Movie.builder()
						.title("Wonder Woman 1984")
						.year((short) 2020)
						.build(),
				Movie.builder()
						.title("Tyler Rake")
						.year((short) 2020)
						.build(),
				Movie.builder()
						.title("Tenet")
						.year((short) 2020)
						.build(),
				Movie.builder()
						.title("Outside the Wire")
						.year((short) 2021)
						.build()
		);
		moviesDatabase.forEach(entityManager::persist); // SQL : insert for each movie
		entityManager.flush();
		// when
		var movies = movieRepository.findByYearOrderByTitle(year);
		// TODO: assert
	}
	
	@Test
	void testFindByYearBetweenSort() {
		List<Movie> moviesDatabase = List.of(
				Movie.builder()
						.title("Dr No")
						.year((short) 1962)
						.duration((short) 110)
						.build(),
				Movie.builder()
						.title("Licence To Kill")
						.year((short) 1989)
						.duration((short) 133)
						.build(),
				Movie.builder()
						.title("The Spy Who Loved Me")
						.year((short) 1977)
						.duration((short) 125)
						.build(),
				Movie.builder()
						.title("GoldenEye")
						.year((short) 1995)
						.duration((short) 130)
						.build(),
				Movie.builder()
						.title("Spectre")
						.year((short) 2015)
						.duration((short) 148)
						.build(),
				Movie.builder()
						.title("Octopussy")
						.year((short) 1983)
						.duration((short) 131)
						.build(),
				Movie.builder()
						.title("Never Say Never Again")
						.year((short) 1983)
						.duration((short) 130)
						.build()
		);
		moviesDatabase.forEach(entityManager::persist); 
		entityManager.flush();

		// when
		// order by movie0_.year asc
		var moviesByYear = movieRepository.findByYearBetween((short) 1960, (short) 2020, Sort.by("year"));
		// order by movie0_.duration desc
		var moviesByDurationDesc = movieRepository.findByYearBetween((short) 1960, (short) 2020,
				Sort.by(Direction.DESC, "duration"));
		// order by movie0_.duration asc, movie0_.title asc
		var moviesByDurationTitle = movieRepository.findByYearBetween((short) 1960, (short) 2020,
				Sort.by("year", "title"));

		// TODO: split tests and make assertions
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"Z", 
			"Blade Runner", 
			"Night of the Day of the Dawn of the Son of the Bride of the Return of the Revenge of the Terror of the Attack of the Evil Mutant Hellbound Flesh Eating Crawling Alien Zombified Subhumanoid Living Dead, Part 5"})
	void testSaveTitle(String title) {
		// given
		short year = 1982;
		short duration = 173;
		// when + then
		saveAssertMovie(title, year, duration);
	}
			
	@Test
	void testSaveTitleEmptyNOK() {
		String title = null;
		short year = 1982;
		short duration = 173;
		assertThrows(DataIntegrityViolationException.class, 
				() -> saveAssertMovie(title, year, duration));
	}
	
	@ParameterizedTest
	@ValueSource(shorts = { 1888, 1982, Short.MAX_VALUE })
	void testSaveYear(short year) {
		// given
		String title = "Blade Runner";
		short duration = 173;
		// when + then
		saveAssertMovie(title, year, duration);
	}
	
	@ParameterizedTest
	@ValueSource(shorts = { 1, 120, Short.MAX_VALUE })
	@NullSource
	void testSaveDuration(Short duration) {
		// given
		String title = "Blade Runner";
		short year = 1982;
		// when + then
		saveAssertMovie(title, year, duration);
	}
	
	
	@Test
	void testSaveYearNullNOK() {
		// given
		String title = "Blade Runner";
		Short year = null;
		short duration = 173;
		// when + then
		assertThrows(DataIntegrityViolationException.class,
				() -> saveAssertMovie(title, year, duration));
	}

	private void saveAssertMovie(String title, Short year, Short duration) {
		Movie movie = Movie.builder()
				.title(title)
				.year(year)
				.duration(duration)
				.build();
		// when
		movieRepository.save(movie);
		// then
		var idMovie = movie.getId();
		assertNotNull(idMovie, "id generated by database");
		// NB : following test only checks that object read is the same as object written (cache)
		movieRepository.findById(idMovie)
			.ifPresent(m -> assertEquals(movie, m));
	}

}
