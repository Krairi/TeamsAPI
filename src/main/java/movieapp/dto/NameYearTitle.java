package movieapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class NameYearTitle {
	@Getter private String name;
	@Getter private Short year;
	@Getter private String title;
}
