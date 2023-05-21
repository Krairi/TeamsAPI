package movieapp.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import movieapp.dto.IMovieStatistics;
import movieapp.dto.IMovieYearCount;
import movieapp.dto.MovieStatistics;
import movieapp.persistence.entity.Movie;

// paramètres de généricité :
//	T = Movie : objets gérés par le répository
//	ID= Integer : type de la clé primaire
// By Default use database in Memory H2
//
// Query auto generated by Spring according to following rules:
// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
public interface MovieRepository extends JpaRepository<Movie, Integer>{
	// gifts : save/findAll/findById/...
	
	// select movie0_.id as id1_0_, movie0_.duration as duration2_0_, movie0_.title as title3_0_, movie0_.year as year4_0_ 
	// from movie movie0_ 
	// where movie0_.title=?
	Stream<Movie> findByTitle(String title);
	
	// select movie0_.id as id1_0_, movie0_.duration as duration2_0_, movie0_.title as title3_0_, movie0_.year as year4_0_ 
	// from movie movie0_ 
	// where upper(movie0_.title) like upper(?) escape ?
	Stream<Movie> findByTitleContainingIgnoreCase(String title);
	
	// where year = 2000
	Stream<Movie> findByYearOrderByTitle(short year);
	
	// where year >= 2000
	Stream<Movie> findByYearGreaterThanEqual(short yearMin);
	
	// where year <= 2000
	Stream<Movie> findByYearLessThanEqual(short yearMax);
	
	// where year between 2000 and 2009 + sort
	Stream<Movie> findByYearBetweenOrderByYear(short yearMin, short yearMax);
	Stream<Movie> findByYearBetween(short yearMin, short yearMax, Sort sort);
	
	// where title = 'The Lion King' and year = 1994 + sort
	Stream<Movie> findByTitleAndYearOrderByYear(String title, short year);
	
	// where duration is NULL
	Stream<Movie> findByDurationNull();
	
	// by director name (filmography)
	List<Movie> findByDirectorNameOrderByYearDesc(String name);

	// by actor name (filmography)
	List<Movie> findByActorsNameOrderByYearDesc(String name);	
	
	// total duration during year range
	@Query("select coalesce(sum(m.duration),0) from Movie m "
			+ "where m.year between ?1 and ?2")
	long totalDuration(short yearMin, short yearMax);
	
	// average duration during year range
	@Query("select avg(m.duration) from Movie m "
			+ "where m.year between :yearMin and :yearMax")
	Optional<Double> averageDuration(short yearMin, short yearMax);
	
	// global statistics with DTO object
	@Query("select new movieapp.dto.MovieStatistics("
			+ "COUNT(*), MIN(m.year), MAX(m.year), "
			+ "COALESCE(SUM(duration),0), "
			+ "AVG(duration), MIN(duration), MAX(duration), "
			+ "MIN(LENGTH(title)), MAX(LENGTH(title))) "
			+ "from Movie m")
	MovieStatistics statisticsDto();
	
	// global statistics with DTO interface
	@Query("select "
			+ "COUNT(*), MIN(m.year), MAX(m.year), "
			+ "COALESCE(SUM(duration),0), "
			+ "AVG(duration), MIN(duration), MAX(duration), "
			+ "MIN(LENGTH(title)), MAX(LENGTH(title)) "
			+ "from Movie m")
	IMovieStatistics statisticsDtoI();
	
	@Query("select m.year as year, count(*) as countMovie "
			+ "from Movie m "
			+ "where m.year >= :yearMin group by m.year "
			+ "having count(*) >= :countMin order by m.year")
	Stream<IMovieYearCount> countMovieByYear(short yearMin, int countMin);
}