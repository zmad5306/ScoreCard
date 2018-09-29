package us.zacharymaddox.scorecard.core.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import us.zacharymaddox.scorecard.domain.Transport;

@Entity
@Table(name="SERVICE", schema="SCORE_CARD", indexes= {
		@Index(columnList="NAME", unique=true)
})
public class Service extends DomainObject implements Serializable {
	
	private static final long serialVersionUID = 4081098177527594840L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonProperty("service_id")
	private Long serviceId;
	@Column(unique=true)
	@NotNull
	@NotEmpty
	private String name;
	@Enumerated(EnumType.STRING)
	@NotNull
	private Transport transport;
	@NotNull
	@NotEmpty
	private String path;
	@OneToMany(mappedBy="service", fetch=FetchType.EAGER)
	@JsonSerialize(using=ServiceActionListSerializer.class)
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
