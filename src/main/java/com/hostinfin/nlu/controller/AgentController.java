package com.hostinfin.nlu.controller;

import com.google.gson.JsonObject;
import org.opennlu.OpenNLU;
import spark.Request;
import spark.Response;

/**
 * Created by René Preuß on 1/8/2018.
 */
public class AgentController extends Controller {
    private final OpenNLU openNLU;

    public AgentController(OpenNLU openNLU) {
        this.openNLU = openNLU;
    }

    public Object train(Request req, Response res) throws Exception {
        canAccessAgent(req, res);
        JsonObject jsonObject = new JsonObject();
        try {
            openNLU
                    .getAgent(Integer.parseInt(req.params(":agent")))
                    .reloadSkills();
            jsonObject.addProperty("trained", true);
        } catch (Exception e) {
            jsonObject.addProperty("trained", false);
        }
        return jsonObject;
    }
}
