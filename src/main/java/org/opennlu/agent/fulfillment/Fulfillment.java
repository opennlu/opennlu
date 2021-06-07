package org.opennlu.agent.fulfillment;

import com.google.gson.JsonObject;
import org.opennlu.agent.AgentResponse;
import org.opennlu.agent.intent.Parameter;

import java.util.Random;

/**
 * Created by René Preuß on 8/31/2017.
 */
public class Fulfillment {
    private final AgentResponse agentResponse;

    public Fulfillment(AgentResponse agentResponse) {
        this.agentResponse = agentResponse;
    }

    public String getResponse() {
        if (agentResponse.getIntent().getResponseStrings().size() > 0) {
            String response = agentResponse.getIntent().getResponseStrings().get(new Random().nextInt(agentResponse.getIntent().getResponseStrings().size()));
            for (Parameter property : agentResponse.getIntent().getParameters()) {
                if (agentResponse.getEntityValues().containsKey(property.getEntity().getName())) {
                    response = response.replace("$" + property.getName(), agentResponse.getEntityValues().get(property.getEntity().getName()));
                }
            }
            return response;
        }
        return "";
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("response", getResponse());
        return jsonObject;
    }
}
