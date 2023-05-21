package movieapp.dto;

import java.time.LocalDate;

import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Builder
@Getter
@Setter
@ToString
public class PersonSimple {
	private Integer id;

	@NotBlank
	private String name;

	private LocalDate birthdate;
}
