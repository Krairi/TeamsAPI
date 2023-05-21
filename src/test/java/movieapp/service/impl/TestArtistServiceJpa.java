package movieapp.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.any;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import movieapp.dto.PersonSimple;
import movieapp.persistence.entity.Person;
import movieapp.persistence.repository.PersonRepository;
import movieapp.service.PersonService;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
class TestArtistServiceJpa {

	// layer to mock
	// @Mock : pure mockito
	@MockBean // mock with spring IOC
	PersonRepository artistRepository;
	
	// layer to test using layer mocked
	// @InjectMocks : pure mockito
	@Autowired
	PersonService artistService;
	
	@Test
	void testGetByIdPresent() {
		// 1. given
		int id = 1;
		String name = "Will Smith";
		LocalDate birthdate = LocalDate.of(1968, 9, 25);
		// perfect answer from mock
		Person personEntity = new Person(name, birthdate);
		personEntity.setId(id);
		given(artistRepository.findById(id))
			.willReturn(Optional.of(personEntity));
		// 2. when
		Optional<PersonSimple> optArtistSimpleDto = artistService.getById(id);
		// 3. then
		// check mock has been called
		then(artistRepository)
			.should()
			.findById(eq(id));
		// check answer
		assertTrue(optArtistSimpleDto.isPresent());
		optArtistSimpleDto.ifPresent(
				artistSimpleDto -> assertAll(
						() -> assertEquals(id, artistSimpleDto.getId()),
						() -> assertEquals(name, artistSimpleDto.getName()),
						() -> assertEquals(birthdate, artistSimpleDto.getBirthdate())));
	}

	@Test
	void testGetByIdAbsent() {
		// 1. given : id with no corresponding data in repository
		int id = 0;
		// perfect answer from mock
		given(artistRepository.findById(id))
			.willReturn(Optional.empty());
		// 2. when
		Optional<PersonSimple> optArtistSimpleDto = artistService.getById(id);
		// 3. then
		// check mock has been called
		then(artistRepository)
			.should()
			.findById(eq(id));
		// check answer
		assertTrue(optArtistSimpleDto.isEmpty());
	}
	
	@Test
	void testAdd() {
		// 1. given
		// DTO to add
		String name = "Will Smith";
		LocalDate birthdate = LocalDate.of(1968, 9, 25);
		PersonSimple personSimpleDtoIn = PersonSimple.of(null, name, birthdate);
		// Entity response from mock repository
		int id = 1;
		Person artistEntity = new Person(name,birthdate);
		artistEntity.setId(id);
		given(artistRepository.save(any()))
			.willReturn(artistEntity);
		// 2. when
		PersonSimple personSimpleDtoOut = artistService.add(personSimpleDtoIn);
		// 3. then
		then(artistRepository)
			.should()
			.save(any());
		assertNotNull(personSimpleDtoOut.getId());
		assertEquals(id, personSimpleDtoOut.getId()); // from repo response
		assertEquals(name, personSimpleDtoOut.getName());
		assertEquals(birthdate, personSimpleDtoOut.getBirthdate());
	}
	
//	@Test
//	void testGetByName() {
//		fail("Not Implemented yet");
//	}
}








