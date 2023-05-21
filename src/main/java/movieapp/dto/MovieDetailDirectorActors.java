package movieapp.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter @Setter
@SuperBuilder
@ToString(callSuper = true)
public class MovieDetailDirectorActors extends MovieDetail {
	private PersonSimple director;
	private List<PersonSimple> actors;
}
