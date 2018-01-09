package org.opennlu;

import org.opennlu.agent.Agent;
import org.opennlu.jdbi.Database;
import org.opennlu.json.JsonConfig;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OpenNLU {
    private final JsonConfig config;
    private Database database;
    private Map<Integer, Agent> agentCache = new HashMap<>();

    public OpenNLU() throws IOException {
        File configFile = new File("config.json");
        this.config = JsonConfig.loadConfiguration(configFile);
        config.getConfigSection("SQL").setIfNull("ConnectionString", "jdbc:mysql://localhost/nlu?connectTimeout=0&autoReconnect=true");
        config.getConfigSection("SQL").setIfNull("Username", "root");
        config.getConfigSection("SQL").setIfNull("Password", "");
        config.getConfigSection("JWT").setIfNull("Key", "qwertyuiopasdfghjklzxcvbnm123456");
        config.save(configFile);
        this.database = new Database(this);
    }

    public static void main(String[] args) throws IOException {
        OpenNLU openNLU = new OpenNLU();

        System.in.read();
    }

    public Database getDatabase() {
        return database;
    }

    public JsonConfig getConfig() {
        return config;
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
