package movieapp.persistence.entity;

import lombok.*;
import movieapp.enums.ColorEnum;
import movieapp.enums.PgEnum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "movies")
public class Movie {

	@Id // primary key (unique + not null)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, length = 300)
	private String title;

	@Column(nullable = false)
	private Short year;

	private Short duration;

	@Column(length = 4000)
	private String synopsis;

	@Column(length = 300)
	private String posterUri;

	@Enumerated(EnumType.STRING)
	private ColorEnum color;

	@Enumerated(EnumType.STRING)
	private PgEnum pg;

	@Builder.Default
	@ElementCollection
	@CollectionTable(name="have_genre",
			joinColumns = @JoinColumn(name = "movie_id")
	)
	@Column(name="genre", nullable = false, length = 20)
	private Set<String> genres = new HashSet<>();

	@ManyToOne
	@JoinColumn(name="director_id", nullable=true)
	private Person director;

	@Builder.Default
	@ManyToMany
	@JoinTable(
			name="play",
			joinColumns = @JoinColumn(name="movie_id"), // FK to this entity (Movie)
			inverseJoinColumns = @JoinColumn(name="actor_id")) // FK to the other entity (Artist)
	private Set<Person> actors = new HashSet<>();

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		return builder.append(title)
			.append("(")
			.append(year)
			.append(")#")
			.append(id)
			.toString();  // finalize String result
	}
}
