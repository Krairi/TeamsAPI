package movieapp.controller;


import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import movieapp.dto.MovieDetail;
import movieapp.dto.MovieDetailDirectorActors;
import movieapp.dto.MovieSimple;
import movieapp.service.MovieService;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
	
	@Autowired
	private MovieService movieService;
	
	// GET requests
	
	/**
	 * path /api/movies
	 * @return list of all movies
	 */
	@GetMapping 
	@ResponseBody
	public List<MovieSimple> movies() {
		return movieService.getAll();
	}
	
	/**
	 * path /api/movies/1
	 * @param id id of the movie to find
	 * @return movie with this id or Optional empty if not found
	 */
	@GetMapping("/{id}")
	@ResponseBody
	public MovieDetailDirectorActors movie(@PathVariable("id") int id) {

		return movieService.getById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						String.format("Movie not found with id <%d>",
								id)));
	}
	
	/**
	 * path /api/movies/byTitle?t=Spectre
	 * @param title
	 * @return
	 */
	@GetMapping("/byTitle")
	public List<MovieSimple> moviesByTitle(@RequestParam("t") String title) {
		return movieService.getByTitle(title);
	}
	
	/**
	 * path /api/movies/byTitleYear?t=Spectre&y=2015
	 * @param title
	 * @param year
	 * @return
	 */
	@GetMapping("/byTitleYear")
	public List<MovieSimple> moviesByTitleYear(
			@RequestParam("t") String title, 
			// @RequestParam(value="y", defaultValue = "2020") short year)
			@RequestParam(value="y", required=false) Short year)
	{
		if (Objects.isNull(year)) {
			return movieService.getByTitle(title);
		} else {
			return movieService.getByTitleYear(title, year);
		}
	}
	
	/**
	 * paths 
	 * 	/api/movies/byYearRange?mi=1950&ma=1980
	 *  /api/movies/byYearRange?mi=1950
	 *  /api/movies/byYearRange?ma=1980
	 *  @return list of movies within range, empty list if unbounded range
	 */
	@GetMapping("/byYearRange")
	@ResponseBody
	public List<MovieSimple> moviesByYearRange(
			@RequestParam(value="mi", required=false) Short minYear,
			@RequestParam(value="ma", required=false) Short maxYear)
	{
		if (Objects.nonNull(minYear)) {
			if (Objects.nonNull(maxYear)) {
				return movieService.getByYearRange(minYear, maxYear);
			} else {
				return movieService.getByYearGreater(minYear);
			}
		} else if (Objects.nonNull(maxYear)) {
			return movieService.getByYearLess(maxYear);
		} else {
			   throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"no range year provided");
		}
	}

	// POST request
	
	/**
	 * path /api/movies
	 * @param movie movie to add
	 * @return movie added and completed (id, default values)
	 */
	@PostMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public MovieDetail addMovie(@RequestBody @Valid MovieDetail movie) {
		return movieService.add(movie); 
	}

	// PUT request
	
	/**
	 * path /api/movies
	 * @param movie
	 * @return
	 */
	@PutMapping
	public MovieDetail updateMovie(@RequestBody @Valid MovieDetail movie) {
		return movieService.update(movie)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						String.format("Movie not found with id <%d>",
								movie.getId())));
	}
	
	/**
	 * path /api/movies/1/director/3
	 * @param idMovie
	 * @param idDirector
	 * @return
	 */
	@PatchMapping("/{mid}/director/{did}")
	public MovieDetailDirectorActors setDirector(
			@PathVariable("mid") int idMovie,
			@PathVariable("did") int idDirector)
	{
		return movieService.setDirector(idMovie, idDirector)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						String.format("Movie <%d> or director <%d> not found",
								idMovie, idDirector)));
	}
	
	/**
	 * path /api/movies/1/actors
	 * body json : [33, 4, 42]
	 * @param idMovie
	 * @param idActors
	 * @return
	 */
	@PatchMapping("/{mid}/actors")
	public MovieDetailDirectorActors setActors(
			@PathVariable("mid") int idMovie,
			@RequestBody List<Integer> idActors) 
	{
		return movieService.setActors(idMovie, idActors)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						String.format("Movie <%d> or actors <%s> not found",
								idMovie,
								idActors.stream().map(id -> "" + id).collect(Collectors.joining(", "))
						)
				));
	}

	// DELETE requests
	
	/**
	 * path /api/movies
	 * @param movie movie to delete according to its id
	 * @return movie deleted
	 */
	@DeleteMapping("/one")
	public MovieDetail deleteMovie(@RequestBody MovieDetail movie) {
		int id = movie.getId();
		return deleteMovieById(id);
	}
	
	/**
	 * url /api/movies/1
	 * @param id id of movie to delete
	 * @return movie deleted
	 */
	@DeleteMapping("/{id}")
	public MovieDetail deleteMovieById(@PathVariable("id") int id) {
		return movieService.deleteMovieById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						String.format("Movie not found with id <%d>",
								id)));
	}
	
}
