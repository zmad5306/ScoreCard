package dev.zachmaddox.scorecard.lib.domain;

import java.io.Serializable;
import java.util.List;

import dev.zachmaddox.scorecard.lib.domain.exception.ActionNotFoundException;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transaction implements Serializable {
	
	@JsonProperty("transaction_id")
	private Long transactionId;
	private String type;
	@NotEmpty
	@NotNull
	private String name;
	private List<Action> actions;

    public Action getAction(String name) {
		return actions.stream().filter(a -> name.equalsIgnoreCase(a.getName())).findFirst().orElseThrow(ActionNotFoundException::new);
	}

    public Action getAction(Long id) {
		return actions.stream().filter(a -> a.getActionId().equals(id)).findFirst().orElseThrow(ActionNotFoundException::new);
	}


    @SuppressWarnings("unused") // used in handler
    public Integer getActionCount() {
        return actions.size();
    }

}
