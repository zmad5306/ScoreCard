package us.zacharymaddox.scorecard.domain;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ActionSerializer extends StdSerializer<Action>{

	public ActionSerializer() {
		this(null);
	}
	
	public ActionSerializer(Class<Action> t) {
		super(t);
	}

	private static final long serialVersionUID = 2809965674514059273L;

	@Override
	public void serialize(Action value, JsonGenerator gen, SerializerProvider provider)
			throws IOException {
		if (null == value) {
			gen.writeNull();
		} else {
			gen.writeNumber(value.getActionId());
		}
	}

}
