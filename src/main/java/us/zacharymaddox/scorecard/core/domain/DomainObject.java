package us.zacharymaddox.scorecard.core.domain;

import javax.persistence.Transient;

public abstract class DomainObject {
	
	@Transient
	private String type;
	
	public DomainObject(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
