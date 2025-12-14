package dev.zachmaddox.scorecard.api.domain;

import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public abstract class DomainObject {
	
	@Transient
	private String type;
	
	public DomainObject(String type) {
		this.type = type;
	}

}
