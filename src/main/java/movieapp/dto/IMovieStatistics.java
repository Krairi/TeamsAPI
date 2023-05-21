package movieapp.dto;

public interface IMovieStatistics {
	long getCount();
	Short getMinYear();
	Short getMaxYear();
	int getTotalDuration();
	Double getAverageDuration();
	Short getMinDuration();
	Short getMaxDuration();
	Integer getMinTitleLength();
	Integer getMaxTitleLength();
}
