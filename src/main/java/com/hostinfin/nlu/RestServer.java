package com.hostinfin.nlu;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hostinfin.nlu.controller.AgentController;
import com.hostinfin.nlu.controller.SessionController;
import org.opennlu.OpenNLU;

import java.io.UnsupportedEncodingException;

import static spark.Spark.*;

/**
 * Created by René Preuß on 1/8/2018.
 */
public class RestServer {
    public static void main(String[] args) throws UnsupportedEncodingException {
        Gson gson = new Gson();
        Algorithm algorithm = Algorithm.HMAC256("qwertyuiopasdfghjklzxcvbnm123456");
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("accounts.preuss.io")
                .build();

        // OpenNLU
        OpenNLU openNLU = new OpenNLU();
        SessionController sessionController = new SessionController(openNLU);
        AgentController agentController = new AgentController(openNLU);

        // Server
        port(8080);
        post(Path.Session.CREATE, "application/json", sessionController::create, gson::toJson);
        post(Path.Session.PARSE, "application/json", sessionController::parse, gson::toJson);
        post(Path.Agent.TRAIN, "application/json", agentController::train, gson::toJson);

        // Status report
        get("/status", (req, res) -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("status", true);
            return jsonObject;
        }, gson::toJson);

        // Authentication
        before((req, res) -> {
            DecodedJWT jwt = verifier.verify(req.headers("Authorization").split(" ")[1]);
            req.attribute("agent", jwt.getClaim("agent_id").asString());
        });

        // Response content type
        after((req, res) -> res.type("application/json"));

        exception(Exception.class, (e, request, response) -> {
            response.type("application/json");
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("message", e.getMessage());
            e.printStackTrace();
            response.body(gson.toJson(jsonObject));
        });

        notFound((req, res) -> {
            res.type("application/json");
            return "{\"message\":\"Custom 404\"}";
        });
    }
}
