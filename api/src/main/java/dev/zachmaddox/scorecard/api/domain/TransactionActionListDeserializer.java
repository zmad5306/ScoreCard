package dev.zachmaddox.scorecard.api.domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class TransactionActionListDeserializer extends StdDeserializer<List<TransactionAction>> {
	
	public TransactionActionListDeserializer() {
        super((Class<List<TransactionAction>>) null);
    }

	@Override
	public List<TransactionAction> deserialize(JsonParser p, DeserializationContext context)
			throws IOException {
		ArrayNode node = p.getCodec().readTree(p);
		Iterator<JsonNode> elements = node.elements();
		List<TransactionAction> actions = new ArrayList<>();
		Transaction transaction = (Transaction) p.currentValue();
		Map<Long, List<Long>> dependsOn = new HashMap<>();
		while (elements.hasNext()) {
			JsonNode nd = elements.next();
			Action action = new Action();
			action.setActionId(nd.get("action_id").asLong());
			TransactionAction ta = new TransactionAction();
			ta.setAction(action);
			ta.setTransaction(transaction);
            if (nd.hasNonNull("depends_on")) {
                ArrayNode dpo = (ArrayNode) nd.get("depends_on");
                dpo.forEach(s -> {
                    if (!dependsOn.containsKey(action.getActionId())) {
                        dependsOn.put(action.getActionId(), new ArrayList<>());
                    }
                    dependsOn.get(action.getActionId()).add(s.asLong());
                });
            }
			actions.add(ta);
		}

		for (TransactionAction transAction : actions) {
			Set<TransactionAction> dependsOnSet = new HashSet<>();
			List<Long> deps = dependsOn.get(transAction.getAction().getActionId());
			if (null != deps) {
				for (Long depActionId : deps) {
					actions.stream().filter(d -> d.getAction().getActionId().equals(depActionId)).findFirst().ifPresent(dependsOnSet::add);
				}
			}
			transAction.setDependsOn(dependsOnSet);
		}
		
		return actions;
	}

}
