package dev.zachmaddox.scorecard.api.web.api;

import java.util.List;

import dev.zachmaddox.scorecard.api.domain.Action;
import dev.zachmaddox.scorecard.api.service.ActionService;
import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardClientException;
import dev.zachmaddox.scorecard.common.domain.exception.ScoreCardErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/action")
@RequiredArgsConstructor
@Tag(name = "Action", description = "Endpoints for managing actions belonging to services")
public class ActionController {
	
	private final ActionService actionService;
	
	@GetMapping(produces="application/json")
	@Operation(summary = "Get action by name", responses = {
			@ApiResponse(responseCode = "200", description = "Action found", content = @Content(schema = @Schema(implementation = Action.class))),
			@ApiResponse(responseCode = "404", description = "Action not found", content = @Content)
	})
	public Action getAction(@RequestParam(name="name") String name) {
		return actionService.getActionByName(name);
	}
	
	@GetMapping(value="/list", produces="application/json")
	@Operation(summary = "List all actions", responses = {
			@ApiResponse(responseCode = "200", description = "Actions returned", content = @Content(schema = @Schema(implementation = Action.class)))
	})
	public List<Action> getActions() {
		return actionService.getAllActions();
	}
	
	@GetMapping(value="/list/{service_id}", produces="application/json")
	@Operation(summary = "List actions for a service", responses = {
			@ApiResponse(responseCode = "200", description = "Actions returned", content = @Content(schema = @Schema(implementation = Action.class)))
	})
	public List<Action> getActions(@PathVariable("service_id") Long serviceId) {
		return actionService.getActions(serviceId);
	}
	
	@GetMapping(value="/{action_id}", produces="application/json")
	@Operation(summary = "Get action by id", responses = {
			@ApiResponse(responseCode = "200", description = "Action found", content = @Content(schema = @Schema(implementation = Action.class))),
			@ApiResponse(responseCode = "404", description = "Action not found", content = @Content)
	})
	public Action getAction(@PathVariable("action_id") Long actionId) {
		return actionService.getAction(actionId);
	}
	
	@PostMapping(consumes="application/json", produces="application/json")
	@Operation(summary = "Create or update an action", responses = {
			@ApiResponse(responseCode = "200", description = "Action saved", content = @Content(schema = @Schema(implementation = Action.class))),
			@ApiResponse(responseCode = "400", description = "Invalid action payload", content = @Content)
	})
	public Action saveAction(@RequestBody @Valid Action action, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ScoreCardClientException(ScoreCardErrorCode.ACTION_INVALID);
		} else {
			return actionService.saveAction(action);
		}
	}
	
	@DeleteMapping(value="/{action_id}")
	@Operation(summary = "Delete an action", responses = {
			@ApiResponse(responseCode = "204", description = "Deleted"),
			@ApiResponse(responseCode = "400", description = "Cannot delete due to dependency or invalid request", content = @Content)
	})
	public void deleteAction(@PathVariable("action_id") Long actionId) {
		actionService.delete(actionId);
	}

}
