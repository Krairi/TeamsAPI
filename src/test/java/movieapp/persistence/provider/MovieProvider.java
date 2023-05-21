package movieapp.persistence.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.data.util.Pair;

import movieapp.persistence.entity.Movie;

/**
 * Test util class to provide Movie entities
 *
 */
public class MovieProvider {

	public static List<Movie> moviesGoodOnesOneBad(
			List<String> goodTitles, List<Short> goodYears,
			String otherTitle, short otherYear) {

		var res = IntStream.range(0, goodTitles.size())
				.mapToObj(i -> Movie.builder()
						.title(goodTitles.get(i))
						.year(goodYears.get(i))
						.build())
				.collect(Collectors.toCollection(ArrayList::new));
		res.add(Movie.builder()
				.title(otherTitle)
				.year(otherYear)
				.build());
		return res;
	}

	public static List<Movie> moviesGoodOnesBadOnes(
			List<String> goodTitles, List<Integer> goodYears,
			List<String> badTitles, List<Integer> badYears) {
		// TODO
		return null;

	}

}
