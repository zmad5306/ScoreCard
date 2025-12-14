package dev.zachmaddox.scorecard.api.domain;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ServiceActionListSerializer extends StdSerializer<List<Action>> {

	public ServiceActionListSerializer() {
		super((Class<List<Action>>) null);
	}

	@Override
	public void serialize(List<Action> value, JsonGenerator gen, SerializerProvider provider)
			throws IOException {
		if (null == value) {
			gen.writeNull();
		} else {
			gen.writeStartArray();
			for (Action a : value) {
				gen.writeStartObject();
				gen.writeNumberField("action_id", a.getActionId());
				gen.writeStringField("name", a.getName());
				gen.writeObjectFieldStart("service");
				gen.writeNumberField("service_id", a.getService().getServiceId());
				gen.writeEndObject();
				gen.writeStringField("path", a.getPath());
				gen.writeStringField("method", a.getMethod().name());
				gen.writeEndObject();
			}
			gen.writeEndArray();
		}
	}

}
