package org.opennlu.agent.session;

import com.google.gson.JsonObject;
import org.opennlu.agent.Agent;
import org.opennlu.agent.AgentResponse;
import org.opennlu.agent.context.Context;
import org.opennlu.agent.intent.Parameter;

import java.util.*;

/**
 * Created by René Preuß on 8/31/2017.
 */
public class Session {
    private final SessionManager sessionManager;
    private final Agent agent;
    private final int id;

    private List<Context> inputContext = new ArrayList<>();
    private Map<String, String> inputParameters = new HashMap<>();

    public Session(SessionManager sessionManager, Agent agent) {
        this.sessionManager = sessionManager;
        this.agent = agent;
        this.id = agent.getNLU().getDatabase().createSession(agent);
    }

    public Session(SessionManager sessionManager, Agent agent, int id, List<Context> inputContext) {
        this.sessionManager = sessionManager;
        this.agent = agent;
        this.id = id;
        this.inputContext = inputContext;
    }

    public int getId() {
        return id;
    }

    public AgentResponse parse(String message) throws Exception {
        AgentResponse agentResponse = agent.getTrainingManager().parse(message, inputContext, inputParameters);
        this.inputContext = agentResponse.getContext();
        this.inputParameters = new HashMap<>();
        for(Parameter parameter : agentResponse.getIntent().getParameters()) {
            if(agentResponse.getEntityValues().containsKey(parameter.getEntity().getName()))
                this.inputParameters.put(parameter.getName(), agentResponse.getEntityValues().get(parameter.getEntity().getName()));
        }
        agentResponse.stopMeasurement();
        agent.getNLU().getDatabase().createQuery(agent, this, agentResponse);
        return agentResponse;
    }

    public List<Context> getInputContext() {
        return inputContext;
    }

    public Map<String, String> getInputParameters() {
        return inputParameters;
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);
        return jsonObject;
    }
}
