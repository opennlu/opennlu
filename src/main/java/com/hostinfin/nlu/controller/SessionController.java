package com.hostinfin.nlu.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.opennlu.OpenNLU;
import org.opennlu.agent.context.Context;
import spark.Request;
import spark.Response;

import java.util.ArrayList;

/**
 * Created by René Preuß on 1/8/2018.
 */
public class SessionController extends Controller {
    private final OpenNLU openNLU;
    private final JsonParser parser;

    public SessionController(OpenNLU openNLU) {
        this.openNLU = openNLU;
        this.parser = new JsonParser();
    }

    public Object parse(Request req, Response res) throws Exception {
        canAccessAgent(req, res);
        JsonObject json = parser.parse(req.body()).getAsJsonObject();
        System.out.println(json.toString());
        return openNLU
                .getAgent(Integer.parseInt(req.params(":agent")))
                .getSessionManager()
                .getSessionById(
                        Integer.parseInt(req.params(":session")),
                        json.has("context") ?
                                Context.fromJsonArray(json.getAsJsonArray("context")) :
                                new ArrayList<>())
                .parse(json.get("query").getAsString())
                .toJson();
    }

    public Object create(Request req, Response res) throws Exception {
        canAccessAgent(req, res);
        return openNLU
                .getAgent(Integer.parseInt(req.params(":agent")))
                .getSessionManager()
                .createSession()
                .toJson();
    }
}
