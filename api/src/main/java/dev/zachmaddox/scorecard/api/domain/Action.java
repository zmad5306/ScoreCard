package dev.zachmaddox.scorecard.api.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.zachmaddox.scorecard.common.domain.Method;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
@Table(name="ACTION", schema="SCORE_CARD",
		uniqueConstraints= {
			@UniqueConstraint(columnNames= {"SERVICE_ID", "NAME"})
		},
		indexes = {
			@Index(columnList="NAME"),
			@Index(columnList="SERVICE_ID,NAME", unique=true)
		}
	)
@Schema(description = "Action definition belonging to a service")
public class Action implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@JsonProperty("action_id")
    @EqualsAndHashCode.Include
	@Schema(description = "Action identifier")
	private Long actionId;
	@NotNull
	@NotEmpty
	@Schema(description = "Action name, unique within the service")
	private String name;
	@ManyToOne
	@JoinColumn(name="SERVICE_ID")
	@NotNull
	@Schema(description = "Owning service")
	private Service service;
	@NotNull
	@NotEmpty
	@Schema(description = "Relative path for the action")
	private String path;
	@Enumerated(EnumType.STRING)
	@NotNull
	@Schema(description = "HTTP method to invoke")
	private Method method;

}
