package org.opennlu.agent.intent;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import org.opennlu.agent.context.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by René Preuß on 6/2/2017.
 */
public class Intent {
    private final int id;
    private final List<String> textResponses;
    private final String name;
    private final List<DocumentSample> userSays;
    private final List<Parameter> parameterList;
    private final List<Context> inputContexts;
    private final List<Context> outputContexts;
    private final List<String> rawUserSays;
    private String action;

    public Intent(int id, String name, List<Context> inputContexts, List<Context> outputContexts, List<String> userSays, String action, List<Parameter> parameterList, List<String> textResponses) throws IOException {
        this.id = id;
        this.name = name;
        this.inputContexts = inputContexts;
        this.outputContexts = outputContexts;
        this.userSays = new ArrayList<>();
        this.action = action;
        this.rawUserSays = userSays;
        for (String userSay : userSays) {
            this.userSays.add(new DocumentSample(name, WhitespaceTokenizer.INSTANCE.tokenize(removeEntities(userSay).toLowerCase())));
        }
        this.parameterList = parameterList;
        this.textResponses = textResponses;

        // Train parameters
        for (Parameter parameter : parameterList) {
            parameter.getEntity().train(this);
        }
    }

    public String getName() {
        return name;
    }

    public List<DocumentSample> getDocumentSamples() {
        return userSays;
    }

    private String removeEntities(String userSay) {
        return userSay.replaceAll("@[a-z0-9.-]*\\{[^}]*}", "");
    }

    public List<Parameter> getParameters() {
        return parameterList;
    }

    public List<String> getRawStrings() {
        return rawUserSays;
    }

    public List<String> getResponseStrings() {
        return textResponses;
    }

    public String getAction() {
        return action;
    }

    public List<Context> getInputContexts() {
        return inputContexts;
    }

    public List<Context> getOutputContexts() {
        return outputContexts;
    }

    public JsonObject toJson() {
        JsonObject jsonIntentObject = new JsonObject();
        jsonIntentObject.addProperty("name", getName());
        JsonArray jsonUserSaysArray = new JsonArray();
        for (String userSay : getRawStrings()) {
            jsonUserSaysArray.add(new JsonPrimitive(userSay));
        }
        jsonIntentObject.add("user_says", jsonUserSaysArray);
        jsonIntentObject.addProperty("action", getAction());
        JsonArray jsonParameterArray = new JsonArray();
        for (Parameter parameter : getParameters()) {
            JsonObject jsonParameterObject = new JsonObject();
            jsonParameterObject.addProperty("name", parameter.getName());
            jsonParameterObject.addProperty("required", parameter.isRequired());
            jsonParameterObject.addProperty("entity", parameter.getEntity().getName());
            JsonArray jsonFallbackStringArray = new JsonArray();
            for (String fallbackString : parameter.getFallbackStrings()) {
                jsonFallbackStringArray.add(new JsonPrimitive(fallbackString));
            }
            jsonParameterObject.add("fallback", jsonFallbackStringArray);
            jsonParameterArray.add(jsonParameterObject);
        }
        jsonIntentObject.add("parameters", jsonParameterArray);
        JsonObject jsonResponseObject = new JsonObject();
        JsonArray jsonTextResponseArray = new JsonArray();
        for (String responseString : getRawStrings()) {
            jsonTextResponseArray.add(new JsonPrimitive(responseString));
        }
        jsonResponseObject.add("text_response", jsonTextResponseArray);
        jsonIntentObject.add("response", jsonResponseObject);
        return jsonIntentObject;
    }

    public int getId() {
        return id;
    }
}
