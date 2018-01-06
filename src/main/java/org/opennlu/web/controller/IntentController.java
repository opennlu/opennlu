package org.opennlu.web.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.opennlu.agent.Agent;
import org.opennlu.agent.intent.Intent;
import org.opennlu.json.JsonConfig;
import org.opennlu.web.Application;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by René Preuß on 6/14/2017.
 */
public class IntentController {
    public static Route index = (Request request, Response response) -> {
        Agent agent = Application.agentDao.findAgent(request.params("agent"));
        JsonArray jsonIntentsArray = new JsonArray();
        for(Intent intent : agent.getIntentManager().getIntents()) {
            JsonObject jsonIntentObject = new JsonObject();
            jsonIntentObject.addProperty("name", intent.getName());
            jsonIntentsArray.add(jsonIntentObject);
        }
        return jsonIntentsArray;
    };

    public static Route store = (Request request, Response response) -> {
        Agent agent = Application.agentDao.findAgent(request.params("agent"));
        Intent newIntent = agent.getIntentManager().registerIntent(JsonConfig.fromString(request.body()));
        return newIntent.toJson();
    };

    public static Route show = (Request request, Response response) -> {
        Agent agent = Application.agentDao.findAgent(request.params("agent"));
        Intent intent = agent.getIntentManager().findIntent(request.params("intent"));
        return intent.toJson();
    };

    public static Route update = (Request request, Response response) -> {
        return "";
    };

    public static Route delete = (Request request, Response response) -> {
        Agent agent = Application.agentDao.findAgent(request.params("agent"));
        Intent intent = agent.getIntentManager().findIntent(request.params("intent"));
        if(intent != null) {
            agent.getIntentManager().unregisterIntent(intent);
        }
        response.status(204);
        return "";
    };
}
