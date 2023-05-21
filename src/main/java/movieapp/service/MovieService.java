package movieapp.service;

import java.util.List;
import java.util.Optional;

import movieapp.dto.IMovieStatistics;
import movieapp.dto.IMovieYearCount;
import movieapp.dto.MovieDetail;
import movieapp.dto.MovieDetailDirectorActors;
import movieapp.dto.MovieSimple;

public interface MovieService {
	// CREATE
	MovieDetail add(MovieDetail movie);
	// UPDATE
	Optional<MovieDetail> update(MovieDetail movie);
	Optional<MovieDetailDirectorActors> setDirector(int idMovie, int idDirector);
	Optional<MovieDetailDirectorActors> setActors(int idMovie, List<Integer> idActors);
	// DELETE
	Optional<MovieDetail> deleteMovieById(int id);
	// READ
	Optional<MovieDetailDirectorActors> getById(int id);
	List<MovieSimple> getAll();
	List<MovieSimple> getByTitle(String title);
	List<MovieSimple> getByTitleYear(String title, short year);
	List<MovieSimple> getByYearRange(short minYear, short maxYear);
	List<MovieSimple> getByYearLess(short maxYear);
	List<MovieSimple> getByYearGreater(short minYear);
	IMovieStatistics getStatistics();
	List<IMovieYearCount> getCountMovieByYear(short yearMin, int CountMin);
}
