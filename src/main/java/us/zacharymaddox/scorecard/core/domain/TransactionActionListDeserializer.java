package us.zacharymaddox.scorecard.core.domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Configurable;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Configurable
public class TransactionActionListDeserializer extends StdDeserializer<List<TransactionAction>> {
	
	private static final long serialVersionUID = 5605002878074543315L;

	public TransactionActionListDeserializer() {
		this(null);
	}
	
	public TransactionActionListDeserializer(Class<?> vc) { 
        super(vc); 
    }

	@Override
	public List<TransactionAction> deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		ArrayNode node = p.getCodec().readTree(p);
		Iterator<JsonNode> elements = node.elements();
		List<TransactionAction> actions = new ArrayList<>();
		Transaction transaction = (Transaction) p.getCurrentValue();
		while (elements.hasNext()) {
			JsonNode nd = elements.next();
			Action action = new Action();
			action.setActionId(nd.get("action_id").asLong());
			TransactionAction ta = new TransactionAction();
			ta.setAction(action);
			ta.setTransaction(transaction);
			// TODO
			// ta.setDependsOn(dependsOn);
			actions.add(ta);
		}
		return actions;
	}

}
