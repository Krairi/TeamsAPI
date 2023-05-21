package movieapp.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import movieapp.persistence.entity.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import movieapp.persistence.entity.Person;
import movieapp.persistence.entity.Movie;
import movieapp.persistence.repository.MovieRepository;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class TestMovieRepositoryDirectorFind {

	@Autowired
	TestEntityManager entityManager;

	@Autowired
	MovieRepository movieRepository;

	Movie movieH;
	Movie movieA;

	@BeforeEach
	void initData() {
		// artists
		var clint = new Person("Clint Eastwood", LocalDate.of(1930,5,31));
		var todd = new Person("Todd Phillips", LocalDate.of(1970,12,20));
		var morgan = new Person("Morgan Freeman", LocalDate.of(1937,6,1));
		var bradley = new Person("Bradley Cooper");
		var zach = new Person("Zach Galifianakis");
		Stream.of(clint, todd, morgan, bradley, zach)
			.forEach(entityManager::persist);
		// movies with director and actors
		var movieUnforgiven = Movie.builder()
				.title("Unforgiven")
				.year((short) 1992)
				.build();
		var movieGranTorino = Movie.builder()
				.title("Gran Torino")
				.year((short) 2008)
				.build();
		var movieInvictus = Movie.builder()
				.title("Invictus")
				.year((short) 2009)
				.build();
		var moviesClint = List.of(movieUnforgiven, movieGranTorino, movieInvictus);
		moviesClint.forEach(m -> m.setDirector(clint));
		movieUnforgiven.setActors(Set.of(clint, morgan));
		movieGranTorino.setActors(Set.of(clint));
		movieInvictus.setActors(Set.of(morgan));
		movieH = Movie.builder()
				.title("The Hangover")
				.year((short) 2009)
				.build();
		movieH.setDirector(todd);
		movieH.setActors(Set.of(bradley, zach));
		movieA = Movie.builder()
				.title("Alien")
				.year((short) 1979)
				.build();
		moviesClint.forEach(entityManager::persist); // insert x 3
		entityManager.persist(movieH); // insert
		entityManager.persist(movieA); // insert
		entityManager.flush(); // synchro database
		entityManager.clear();
	}

	@Test
	void testFindMovieWithExistingDirector() {
		int idMovie = movieH.getId();
		var optMovie = movieRepository.findById(idMovie);
		assertTrue(optMovie.isPresent());
		// assertNotNull(optMovie.get().getDirector());
		optMovie.ifPresent(m -> assertNotNull(m.getDirector(), "director present"));
	}

	@Test
	void testFindMovieWithNoDirector() {
		int idMovie = movieA.getId();
		var optMovie = movieRepository.findById(idMovie);
		assertTrue(optMovie.isPresent());
		// assertNotNull(optMovie.get().getDirector());
		optMovie.ifPresent(m -> assertNull(m.getDirector(), "no director"));
	}

	@Test
	void testFindByDirector() {
		// given
		String name = "Clint Eastwood";
		// when
		var moviesFound  = movieRepository.findByDirectorNameOrderByYearDesc(name);
		// assert
		assertEquals(3, moviesFound.size(), "number movies");
		assertAll(
			moviesFound.stream()
				.map(Movie::getDirector)
				.map(Person::getName)
				.map(n -> () -> assertEquals(name, n, "director name")));
	}

	@Test
	void testFindMovieWithActors() {
		int idMovie = movieH.getId();
		// select movie0_.id as id1_0_0_, movie0_.id_director as id_direc5_0_0_, movie0_.duration as duration2_0_0_, movie0_.title as title3_0_0_, movie0_.year as year4_0_0_, artist1_.id as id1_2_1_, artist1_.birthdate as birthdat2_2_1_, artist1_.deathdate as deathdat3_2_1_, artist1_.name as name4_2_1_
		// from movies movie0_ left outer join stars artist1_ on movie0_.id_director=artist1_.id
		// where movie0_.id=?
		var movie = movieRepository.getOne(idMovie);
		// select actors0_.id_movie as id_movie1_1_0_, actors0_.id_actor as id_actor2_1_0_, artist1_.id as id1_2_1_, artist1_.birthdate as birthdat2_2_1_, artist1_.deathdate as deathdat3_2_1_, artist1_.name as name4_2_1_
		// from play actors0_ inner join stars artist1_ on actors0_.id_actor=artist1_.id
		// where actors0_.id_movie=?
		var actors = movie.getActors();
		assertEquals(2, actors.size());
	}

	@Test
	void testFindMovieWithNoActors() {
		int idMovie = movieA.getId();
		var movie = movieRepository.getOne(idMovie);
		var actors = movie.getActors();
		assertEquals(0, actors.size());
	}

	@Test
	void testFindByActor() {
		// given
		String name = "Clint Eastwood";
		// when
		// select movie0_.id as id1_0_, movie0_.id_director as id_direc5_0_, movie0_.duration as duration2_0_, movie0_.title as title3_0_, movie0_.year as year4_0_
		// from movies movie0_ left outer join play actors1_ on movie0_.id=actors1_.id_movie
		//		left outer join stars artist2_ on actors1_.id_actor=artist2_.id
		// where artist2_.name=?
		var moviesFound  = movieRepository.findByActorsNameOrderByYearDesc(name);
		// assert

		// check found 2 movies all in which Clint plays
		assertEquals(2, moviesFound.size(), "number movies");
		for (var m: moviesFound) {
			var actors = m.getActors();
			assertTrue(
					actors.stream()
						.anyMatch(a -> a.getName().equals(name)),
					"at least one actor named clint eastwood for movie " + m.getTitle());
		}
	}

}
