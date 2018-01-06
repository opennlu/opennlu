package org.opennlu.web.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.opennlu.agent.Agent;
import org.opennlu.agent.entity.Entity;
import org.opennlu.web.Application;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by René Preuß on 6/14/2017.
 */
public class EntityController {
    public static Route index = (Request request, Response response) -> {
        Agent agent = Application.agentDao.findAgent(request.params("agent"));
        JsonArray jsonEntityArray = new JsonArray();
        for(Entity entity : agent.getEntityManager().getEntities()) {
            JsonObject jsonIntentObject = new JsonObject();
            jsonIntentObject.addProperty("name", entity.getName());
            jsonEntityArray.add(jsonIntentObject);
        }
        return jsonEntityArray;
    };

    public static Route store = (Request request, Response response) -> {
        return "";
    };

    public static Route show = (Request request, Response response) -> {
        Agent agent = Application.agentDao.findAgent(request.params("agent"));
        Entity entity = agent.getEntityManager().findEntity(request.params("entity"));
        return entity.toJson();
    };

    public static Route update = (Request request, Response response) -> {
        return "";
    };

    public static Route delete = (Request request, Response response) -> {
        return "";
    };
}
