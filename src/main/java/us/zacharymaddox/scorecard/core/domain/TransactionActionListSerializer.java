package us.zacharymaddox.scorecard.core.domain;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class TransactionActionListSerializer extends StdSerializer<List<TransactionAction>>{

	private static final long serialVersionUID = -1697675233289744523L;

	public TransactionActionListSerializer() {
		this(null);
	}
	
	public TransactionActionListSerializer(Class<List<TransactionAction>> t) {
		super(t);
	}

	@Override
	public void serialize(List<TransactionAction> value, JsonGenerator gen, SerializerProvider provider)
			throws IOException {
		if (null == value) {
			gen.writeNull();
		} else {
			gen.writeStartArray();
			for (TransactionAction a : value) {
				gen.writeObject(a.getAction());
			}
			gen.writeEndArray();
		}
	}

}
