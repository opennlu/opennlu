package org.opennlu.web.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.opennlu.agent.Agent;
import org.opennlu.agent.AgentResponse;
import org.opennlu.web.Application;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;

/**
 * Created by René Preuß on 6/14/2017.
 */
public class AgentController extends Controller {
    public static Route index = (Request request, Response response) -> {
        return "";
    };

    public static Route store = (Request request, Response response) -> {
        return "";
    };

    public static Route show = (Request request, Response response) -> {
        return "";
    };

    public static Route update = (Request request, Response response) -> {
        return "";
    };

    public static Route delete = (Request request, Response response) -> {
        return "";
    };

    public static Route parse = (Request request, Response response) -> {
        JsonObject requestBody = new JsonParser().parse(request.body()).getAsJsonObject();
        Agent agent = Application.agentDao.findAgent(request.params("agent"));
        AgentResponse agentResponse = agent.getTrainingManager().parse(requestBody.get("message").getAsString(), new ArrayList<>());
        return agentResponse.toJson();
    };

    public static Route parseGet = (Request request, Response response) -> {
        Agent agent = Application.agentDao.findAgent(request.params("agent"));
        AgentResponse agentResponse = agent.getTrainingManager().parse(request.queryParams("message"), new ArrayList<>());
        return agentResponse.toJson();
    };

    public static Route train = (Request request, Response response) -> {
        Agent agent = Application.agentDao.findAgent(request.params("agent"));
        // TODO clear train cache...
        response.status(204);
        return "";
    };
}
