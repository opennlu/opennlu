package org.opennlu;

import org.opennlu.agent.Agent;
import org.opennlu.jdbi.Database;
import org.opennlu.json.ConfigSection;
import org.opennlu.json.JsonConfig;
import org.opennlu.util.RandomString;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OpenNLU {
    private final ConfigSection configSection;
    private Database database;
    private Map<Integer, Agent> agentCache = new HashMap<>();

    public OpenNLU(ConfigSection configSection) {
        this.configSection = configSection;
        this.database = new Database(this);
    }

    public static void main(String[] args) throws IOException {
        OpenNLU openNLU = new OpenNLU(getLocalConfig(new File("config.json")));

        System.in.read();
    }

    public static ConfigSection getLocalConfig(File configFile) throws IOException {
        JsonConfig config = JsonConfig.loadConfiguration(configFile);
        config.getConfigSection("SQL").setIfNull("ConnectionString", "jdbc:mysql://localhost/nlu?connectTimeout=0&autoReconnect=true");
        config.getConfigSection("SQL").setIfNull("Username", "root");
        config.getConfigSection("SQL").setIfNull("Password", "");
        config.getConfigSection("JWT").setIfNull("Issuer", "opennlu.org");
        config.getConfigSection("JWT").setIfNull("Key", RandomString.getSaltString());
        config.save(configFile);
        return config;
    }

    public Database getDatabase() {
        return database;
    }

    public ConfigSection getConfig() {
        return configSection;
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
