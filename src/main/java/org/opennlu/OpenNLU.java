package org.opennlu;

import org.opennlu.agent.Agent;
import org.opennlu.jdbi.Database;

import java.util.HashMap;
import java.util.Map;

public class OpenNLU {
    private Database database;
    private Map<Integer, Agent> agentCache = new HashMap<>();

    public OpenNLU() {
        this.database = new Database(this);
    }

    public Database getDatabase() {
        return database;
    }

    public Agent getAgent(int agentId) throws Exception {
        Agent agent = agentCache.get(agentId);
        if(agent == null) {
            agent = new Agent(this, agentId);
            agentCache.put(agentId, agent);
        }
        return agent;
    }
}
