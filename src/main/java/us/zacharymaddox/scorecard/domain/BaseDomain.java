package us.zacharymaddox.scorecard.domain;

public abstract class BaseDomain {
	
	private String type;
	
	public BaseDomain(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
