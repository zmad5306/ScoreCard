package dev.zachmaddox.scorecard.api.domain;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.zachmaddox.scorecard.common.domain.Transport;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Setter
@Getter
@Entity
@Table(name="SERVICE", schema="SCORE_CARD", indexes= {
		@Index(columnList="NAME", unique=true)
})
@Schema(description = "Service definition")
public class Service extends DomainObject implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@JsonProperty("service_id")
    @EqualsAndHashCode.Include
	@Schema(description = "Service identifier")
	private Long serviceId;
	@Column(unique=true)
	@NotNull
	@NotEmpty
	@Schema(description = "Service name (unique)")
	private String name;
	@Enumerated(EnumType.STRING)
	@NotNull
	@Schema(description = "Transport type")
	private Transport transport;
	@NotNull
	@NotEmpty
	@Schema(description = "Base path for the service")
	private String path;
	@OneToMany(mappedBy="service", fetch=FetchType.EAGER)
	@JsonSerialize(using=ServiceActionListSerializer.class)
	@Schema(description = "Actions belonging to the service")
	private List<Action> actions;
	
	public Service() {
		super("service");
	}
	
}
