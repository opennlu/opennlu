package com.hostinfin.nlu.controller;

import spark.Request;
import spark.Response;

import static spark.Spark.halt;

/**
 * Created by René Preuß on 1/9/2018.
 */
public class Controller {
    public void canAccessAgent(Request req, Response res) throws Exception {
        if (!req.params().containsKey(":agent") || !req.params(":agent").equals(req.attribute("agent"))) {
            throw halt(403, "You have no permissions to access this agent!");
        }
    }
}
