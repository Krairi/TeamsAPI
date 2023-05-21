package modelmapper;

import static org.junit.jupiter.api.Assertions.*;


import java.util.Comparator;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import movieapp.dto.MovieSimple;
import movieapp.persistence.entity.Movie;

class TestModelMapper {
	static ModelMapper modelMapper;
	
	@BeforeAll
	static void initModelMapper() {
		modelMapper = new ModelMapper();
	}

	@Test
	void testEntityToDto() {
		// entity
		Movie movieEntity = Movie.builder()
				.title("Blade Runner")
				.year((short) 1982)
				.duration((short) 117)
				.build();
		movieEntity.setId(1);
		// convert to dto
		MovieSimple movieDto = modelMapper.map(movieEntity, MovieSimple.class);
		// is it ok
		System.out.println(movieDto.getTitle());
		assertEquals(movieEntity.getId(), movieDto.getId());
		assertEquals(movieEntity.getTitle(), movieDto.getTitle());
		assertEquals(movieEntity.getYear(), movieDto.getYear());
	}
	
	@Test
	void testDtoToEntity() {
		// DTO
		MovieSimple movieDto = MovieSimple.builder()
				.title("Blade Runner")
				.year((short) 1982)
				.build();
		// convert to entity
		Movie movieEntity = modelMapper.map(movieDto, Movie.class);
		// is it ok
		assertEquals(movieDto.getId(), movieEntity.getId());
		assertEquals(movieDto.getTitle(), movieEntity.getTitle());
		assertEquals(movieDto.getYear(), movieEntity.getYear());
		assertNull(movieEntity.getDirector());
		assertTrue(movieEntity.getActors().isEmpty());
	}
	
	@Test
	void testDtoIntoEntity() {
		// DTO
		MovieSimple movieDto = MovieSimple.builder()
				.id(1)
				.title("Blade Runner (Director's cut)")
				.year((short) 1982)
				.build();
		// Entity
		Movie movieEntity = Movie.builder()
				.title("Blade Runner")
				.year((short) 1982)
				.duration((short) 117)
				.build();
		movieEntity.setId(1);
		// update entity with dto
		modelMapper.map(movieDto, movieEntity);
		// is it ok
		assertEquals(movieDto.getId(), movieEntity.getId());
		assertEquals(movieDto.getTitle(), movieEntity.getTitle());
		assertEquals(movieDto.getYear(), movieEntity.getYear());
		assertEquals((short) 117, movieEntity.getDuration()); // property not in dto
	}
	
	@Test
	void testEntitiesToDtos() {
		// entities
		Stream<Movie> entitySource = Stream.of(
				Movie.builder()
						.title("Blade Runner")
						.year((short) 1982)
						.duration((short) 117)
						.build(),
				Movie.builder()
						.title("The Man Who Knew Too Much")
						.year((short) 1934)
						.build(),
				Movie.builder()
						.title("The Invisible Man")
						.year((short) 2020)
						.build(),
				Movie.builder()
						.title("Wonder Woman 1984")
						.year((short) 2020)
						.build()
		);
		// convert to Dtos
		var res = entitySource.map(me -> modelMapper.map(me, MovieSimple.class))
			.collect(Collectors.toCollection(
					() -> new TreeSet<>(Comparator.comparing(MovieSimple::getTitle))
					));
		// TODO : asserts
	}

}
