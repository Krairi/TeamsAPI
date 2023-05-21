package movieapp.persistence.repository;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import movieapp.dto.IPersonStatistics;
import movieapp.dto.INameYearTitle;
import movieapp.persistence.entity.Person;

// NB: queries with projection
// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#projections
public interface PersonRepository extends JpaRepository<Person, Integer>{
	
	Set<Person> findByNameIgnoreCase(String name);
	Stream<Person> findByNameEndingWithIgnoreCase(String name);
	
	@Query("select a from Person a where extract(year from a.birthdate) = :year")
	Stream<Person> findByBirthdateYear(int year);
	
	Stream<Person> findByBirthdate(LocalDate birthdate);
	
	@Query("select a.name as name, m.year as year, m.title as title "
			+ "from Movie m join m.actors a "
			+ "where a.name like %:name "
			+ "order by m.year")
	Stream<INameYearTitle> filmographyActor(String name);
	
	@Query("select a.id as artistId, a.name as artistName, "
			+ "count(*) as count, min(year) as minYear, max(year) as maxYear "
			+ "from Movie m join m.director a "
			+ "group by a "
			+ "having count(*) >= :countMin "
			+ "order by count(*) desc")
    Stream<IPersonStatistics> statisticsByDirector(long countMin);
	
	@Query("select a.id as artistId, a.name as artistName, "
			+ "count(*) as count, min(year) as minYear, max(year) as maxYear "
			+ "from Movie m join m.actors a "
			+ "group by a "
			+ "having count(*) >= :countMin "
			+ "order by count(*) desc")
    Stream<IPersonStatistics> statisticsByActor(long countMin);
}
