package movieapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import movieapp.enums.ColorEnum;
import movieapp.enums.PgEnum;

import javax.validation.constraints.Min;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@SuperBuilder
@Getter @Setter
@ToString(callSuper = true)
public class MovieDetail extends MovieSimple {
	@Min(45)
	private Short duration;
	private String synopsis;
	private String posterUri;
	private ColorEnum color;
	private PgEnum pg;
	private Set<String> genres;
}
