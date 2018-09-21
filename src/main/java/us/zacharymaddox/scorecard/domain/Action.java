package us.zacharymaddox.scorecard.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="ACTION")
public class Action extends DomainObject implements Serializable {

	private static final long serialVersionUID = -3686654230565046107L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonProperty("action_id")
	private Long actionId;
	private String name;
	@ManyToOne
	@JoinColumn(name="SERVICE_ID")
	private Service service;
	private String path;
	@Enumerated(EnumType.STRING)
	private Method method;

	public Action() {
		super("action");
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof Action) {
			Action other = (Action) obj;
			return new EqualsBuilder().append(this.actionId, other.getActionId()).build();
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(actionId).build();
	}

	public Long getActionId() {
		return actionId;
	}

	public void setActionId(Long actionId) {
		this.actionId = actionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}
	
}
