package us.zacharymaddox.scorecard.core.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="SERVICE")
public class Service extends DomainObject implements Serializable {
	
	private static final long serialVersionUID = 4081098177527594840L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonProperty("service_id")
	private Long serviceId;
	@Column(unique=true)
	private String name;
	@Enumerated(EnumType.STRING)
	private Transport transport;
	private String path;
	@OneToMany(mappedBy="service")
	@JsonIgnore
	private List<Action> actions;
	
	public Service() {
		super("service");
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof Service) {
			Service other = (Service) obj;
			return new EqualsBuilder().append(this.serviceId, other.getServiceId()).build();
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(serviceId).build();
	}
	
	public Long getServiceId() {
		return serviceId;
	}
	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Transport getTransport() {
		return transport;
	}
	public void setTransport(Transport transport) {
		this.transport = transport;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}
	
}
