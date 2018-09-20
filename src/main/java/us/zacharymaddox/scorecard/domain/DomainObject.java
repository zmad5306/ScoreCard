package us.zacharymaddox.scorecard.domain;

public abstract class DomainObject {
	
	private String type;
	
	public DomainObject(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
