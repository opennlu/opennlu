package org.opennlu.web;

import org.opennlu.agent.AgentDao;
import org.opennlu.json.JsonConfig;
import org.opennlu.web.controller.AgentController;
import org.opennlu.web.controller.EntityController;
import org.opennlu.web.controller.IntentController;
import spark.Spark;

import java.io.File;

/**
 * Created by René Preuß on 6/14/2017.
 */
public class Application {

    public static AgentDao agentDao;

    public static void main(String[] args) throws Exception {
        File jsonFile = new File("examples/web.json");
        JsonConfig webConfiguration = JsonConfig.loadConfiguration(jsonFile);
        webConfiguration.setIfNull("port", 6658);
        webConfiguration.save(jsonFile);
        // Instantiate dependencies
        agentDao = new AgentDao();

        // Configure Spark
        Spark.port(webConfiguration.getInt("port"));
        Spark.staticFiles.location("/public");
        Spark.staticFiles.expireTime(600L);

        // Set up before-filters (called before each get/post)
        //before("*",                  JsonTransformer.addTrailingSlashes);

        // Set up agent routes
        Spark.get("/agents",                              AgentController.index);
        Spark.post("/agents",                             AgentController.store);
        Spark.get("/agents/:agent",                       AgentController.show);
        Spark.put("/agents/:agent",                       AgentController.update);
        Spark.delete("/agents/:agent",                    AgentController.delete);
        Spark.get("/agents/:agent/parse",                 AgentController.parseGet);
        Spark.post("/agents/:agent/parse",                AgentController.parse);
        Spark.post("/agents/:agent/train",                AgentController.train);

        // Set up intent routes
        Spark.get("/agents/:agent/intents",               IntentController.index);
        Spark.post("/agents/:agent/intents",              IntentController.store);
        Spark.get("/agents/:agent/intents/:intent",       IntentController.show);
        Spark.put("/agents/:agent/intents/:intent",       IntentController.update);
        Spark.delete("/agents/:agent/intents/:intent",    IntentController.delete);

        // Set up entity routes
        Spark.get("/agents/:agent/entities",              EntityController.index);
        Spark.post("/agents/:agent/entities",             EntityController.store);
        Spark.get("/agents/:agent/entities/:entity",      EntityController.show);
        Spark.put("/agents/:agent/entities/:entity",      EntityController.update);
        Spark.delete("/agents/:agent/entities/:entity",   EntityController.delete);

        //Set up after-filters (called after each get/post)
        //Spark.after("*",                                  JsonTransformer.jsonOutput);

        Spark.exception(Exception.class, (exception, request, response) -> {
            exception.printStackTrace();
            response.body(exception.getMessage());
        });
    }
}
