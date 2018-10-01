package us.zacharymaddox.scorecard.core.domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
		Map<Long, List<Long>> dependsOn = new HashMap<>();
		while (elements.hasNext()) {
			JsonNode nd = elements.next();
			Action action = new Action();
			action.setActionId(nd.get("action_id").asLong());
			TransactionAction ta = new TransactionAction();
			ta.setAction(action);
			ta.setTransaction(transaction);
			ArrayNode dpo = (ArrayNode) nd.get("depends_on");
			dpo.forEach(s -> {
				if (!dependsOn.containsKey(action.getActionId())) {
					dependsOn.put(action.getActionId(), new ArrayList<>());
				}
				dependsOn.get(action.getActionId()).add(s.asLong());
			});
			actions.add(ta);
		}

		for (TransactionAction transAction : actions) {
			Set<TransactionAction> dependsOnSet = new HashSet<>();
			List<Long> deps = dependsOn.get(transAction.getAction().getActionId());
			if (null != deps) {
				for (Long depActionId : deps) {
					Optional<TransactionAction> dep = actions.stream().filter(d -> d.getAction().getActionId() == depActionId).findFirst();
					if (dep.isPresent()) {
						dependsOnSet.add(dep.get());
					}
				}
			}
			transAction.setDependsOn(dependsOnSet);
		}
		
		return actions;
	}

}
