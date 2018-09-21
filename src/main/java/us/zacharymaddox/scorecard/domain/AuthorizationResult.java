package us.zacharymaddox.scorecard.domain;

import java.io.Serializable;

public class AuthorizationResult implements Serializable {
	
	private static final long serialVersionUID = -517157038160346192L;
	private Authorization authorization;
	
	public AuthorizationResult() {
		super();
	}

	public AuthorizationResult(Authorization authorization) {
		super();
		this.authorization = authorization;
	}
	
	public Authorization getAuthorization() {
		return authorization;
	}
	
}
