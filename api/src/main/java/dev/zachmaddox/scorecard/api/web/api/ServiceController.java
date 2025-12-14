package dev.zachmaddox.scorecard.api.web.api;

import java.util.List;

import dev.zachmaddox.scorecard.api.domain.Service;
import dev.zachmaddox.scorecard.api.service.ServiceService;
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
@RequestMapping("/api/v1/service")
@RequiredArgsConstructor
@Tag(name = "Service", description = "Endpoints for managing services")
public class ServiceController {
	
	private final ServiceService serviceService;
	
	@GetMapping(value="/list", produces="application/json")
	@Operation(summary = "List all services", responses = {
			@ApiResponse(responseCode = "200", description = "Services returned", content = @Content(schema = @Schema(implementation = Service.class)))
	})
	public List<Service> getServices() {
		return serviceService.getServices();
	}
	
	@GetMapping(produces="application/json")
	@Operation(summary = "Get service by name", responses = {
			@ApiResponse(responseCode = "200", description = "Service found", content = @Content(schema = @Schema(implementation = Service.class))),
			@ApiResponse(responseCode = "404", description = "Service not found", content = @Content)
	})
	public Service getService(@RequestParam(name="name") String name) {
		return serviceService.getServiceByName(name);
	}
	
	@GetMapping(value="/{service_id}", produces="application/json")
	@Operation(summary = "Get service by id", responses = {
			@ApiResponse(responseCode = "200", description = "Service found", content = @Content(schema = @Schema(implementation = Service.class))),
			@ApiResponse(responseCode = "404", description = "Service not found", content = @Content)
	})
	public Service getService(@PathVariable("service_id") Long serviceId) {
		return serviceService.getService(serviceId);
	}
	
	@PostMapping(produces="application/json", consumes="application/json")
	@Operation(summary = "Create or update a service", responses = {
			@ApiResponse(responseCode = "200", description = "Service saved", content = @Content(schema = @Schema(implementation = Service.class))),
			@ApiResponse(responseCode = "400", description = "Invalid service payload", content = @Content)
	})
	public Service saveService(@RequestBody @Valid Service service, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ScoreCardClientException(ScoreCardErrorCode.SERVICE_INVALID);
		}
		return serviceService.saveService(service);
	}
	
	@DeleteMapping(value="/{service_id}")
	@Operation(summary = "Delete a service", responses = {
			@ApiResponse(responseCode = "204", description = "Deleted"),
			@ApiResponse(responseCode = "400", description = "Cannot delete due to dependency or invalid request", content = @Content)
	})
	public void deleteService(@PathVariable("service_id") Long serviceId) {
		serviceService.delete(serviceId);
	}

}
