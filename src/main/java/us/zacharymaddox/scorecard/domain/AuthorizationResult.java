package us.zacharymaddox.scorecard.domain;

import java.io.Serializable;

public class AuthorizationResult implements Serializable {
	
	private static final long serialVersionUID = -517157038160346192L;
	private ScoreCardAction action;
	private Authorization authorization;
	
	public AuthorizationResult(ScoreCardAction action, Authorization authorization) {
		super();
		this.action = action;
		this.authorization = authorization;
	}
	
	public ScoreCardAction getAction() {
		return action;
	}
	public Authorization getAuthorization() {
		return authorization;
	}
	
}
