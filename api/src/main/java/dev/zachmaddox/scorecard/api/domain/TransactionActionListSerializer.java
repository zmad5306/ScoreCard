package dev.zachmaddox.scorecard.api.domain;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class TransactionActionListSerializer extends StdSerializer<List<TransactionAction>> {

	protected TransactionActionListSerializer() {
		super((Class<List<TransactionAction>>) null);
	}

    @Override
	public void serialize(List<TransactionAction> value, JsonGenerator gen, SerializerProvider provider)
			throws IOException {
		if (null == value) {
			gen.writeNull();
		} else {
			gen.writeStartArray();
			for (TransactionAction a : value) {
				gen.writeStartObject();
				gen.writeStringField("type", a.getType());
				gen.writeStringField("name", a.getAction().getName());
				gen.writeObjectFieldStart("service");
				gen.writeNumberField("service_id", a.getAction().getService().getServiceId());
				gen.writeStringField("path", a.getAction().getService().getPath());
				gen.writeStringField("transport", a.getAction().getService().getTransport().name());
				gen.writeEndObject();
				gen.writeStringField("path", a.getAction().getPath());
				gen.writeStringField("method", a.getAction().getMethod().name());
				gen.writeNumberField("action_id", a.getAction().getActionId());
				gen.writeArrayFieldStart("depends_on");
				if (null != a.getDependsOn()) {
					for (TransactionAction dep : a.getDependsOn()) {
						gen.writeNumber(dep.getAction().getActionId());
					}
				}
				gen.writeEndArray();
				gen.writeEndObject();
			}
			gen.writeEndArray();
		}
	}

}
