package us.zacharymaddox.scorecard.domain;

import java.io.Serializable;
import java.util.List;

public class TransactionAction implements Serializable {

	private static final long serialVersionUID = -561620117019671908L;
	private String actionId;
	private List<String> dependencies;
	
	public String getActionId() {
		return actionId;
	}
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}
	public List<String> getDependencies() {
		return dependencies;
	}
	public void setDependencies(List<String> dependencies) {
		this.dependencies = dependencies;
	}

}
