package movieapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

// DTO: Data Transfert Object

@AllArgsConstructor
@ToString
public class MovieStatistics {
	@Getter private long count;
	@Getter private Short minYear;
	@Getter private Short maxYear;
	@Getter private long totalDuration;
	@Getter private Double averageDuration;
	@Getter private Short minDuration;
	@Getter private Short maxDuration;
	@Getter private Integer minTitleLength;
	@Getter private Integer maxTitleLength;
}
