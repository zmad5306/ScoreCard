package us.zacharymaddox.scorecard.core.domain;

import java.io.IOException;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class DependencySerializer extends StdSerializer<Set<ScoreCardAction>>{

	private static final long serialVersionUID = 2809965674514059273L;
	
	public DependencySerializer() {
		this(null);
	}
	
	public DependencySerializer(Class<Set<ScoreCardAction>> t) {
		super(t);
	}

	@Override
	public void serialize(Set<ScoreCardAction> value, JsonGenerator gen, SerializerProvider provider)
			throws IOException {
		if (null == value) {
			gen.writeNull();
		} else {
			Long[] values = value.stream().map(v -> v.getAction().getActionId()).toArray(Long[]::new);
			gen.writeArray(ArrayUtils.toPrimitive(values), 0, values.length);
		}
	}

}
