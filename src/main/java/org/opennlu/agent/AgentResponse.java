package org.opennlu.agent;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.Span;
import org.opennlu.agent.context.Context;
import org.opennlu.agent.fulfillment.Fulfillment;
import org.opennlu.agent.fulfillment.ParameterFulfillment;
import org.opennlu.agent.intent.Intent;
import org.opennlu.agent.intent.Parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by René Preuß on 6/2/2017.
 */
public class AgentResponse {
    private final String message;
    private final Intent intent;
    private final Map<String, String> entityValues;
    private final List<Context> outputContexts = new ArrayList<>();
    private final double score;
    private final long startTime;
    private Parameter missedParameter = null;
    private long executionTime = 0;

    public AgentResponse(long startTime, String message, Intent intent, final List<Context> inputContexts, Map<String, String> inputParameters, double score) throws Exception {
        this.startTime = startTime;
        this.message = message;
        this.intent = intent;
        this.entityValues = new HashMap<>();
        this.score = score;
        String[] tokens = WhitespaceTokenizer.INSTANCE.tokenize(message);

        for (Parameter parameter : intent.getParameters()) {
            if(inputParameters.containsKey(parameter.getName())) {
                entityValues.put(parameter.getEntity().getName(), inputParameters.get(parameter.getName()));
            } else {
                Span[] spans = parameter.getEntity().getTokenNameFinder().find(tokens);
                String[] names = Span.spansToStrings(spans, tokens);
                String value = String.join(" ", names);

                if(value.length() > 0) {
                    entityValues.put(parameter.getEntity().getName(), value);
                } else {
                    if(parameter.isRequired() && missedParameter == null) {
                        JsonObject dialogObject = new JsonObject();
                        dialogObject.addProperty("intent", intent.getName());
                        dialogObject.addProperty("parameter", parameter.getName());
                        dialogObject.addProperty("message", message);
                        dialogObject.addProperty("score", score);
                        outputContexts.add(new Context("dialog", dialogObject, 1));
                        missedParameter = parameter;
                    }
                }
            }
        }

        // check parameters


        // recalculate context
        for(Context context : inputContexts) {
            if(context.decreaseTimeToLive())
                outputContexts.add(context);
        }

        for(Context context : intent.getOutputContexts()) {
            Context newInputContext = existsContext(outputContexts, context);
            if(newInputContext ==  null) {
                outputContexts.add(context);
            } else {
                outputContexts.remove(newInputContext);
            }
        }
    }

    private Context existsContext(List<Context> outputContexts, Context searchedContext) {
        for (Context context : outputContexts)
            if(context.getName().equals(searchedContext.getName()))
                return context;
        return null;
    }

    public Intent getIntent() {
        return intent;
    }

    public Fulfillment getFulfillment() {
        if(missedParameter != null) {
            return new ParameterFulfillment(this, missedParameter);
        }

        return new Fulfillment(this);
    }

    public Map<String, String> getEntityValues() {
        return entityValues;
    }

    public String getMessage() {
        return message;
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message", getMessage());
        jsonObject.addProperty("score", getScore());
        jsonObject.addProperty("intent", getIntent().getName());
        jsonObject.add("fulfillment", getFulfillment().toJson());
        JsonArray jsonContextArray = new JsonArray();
        for(Context context : getContext()) {
            jsonContextArray.add(context.toJson());
        }
        jsonObject.add("context", jsonContextArray);
        JsonArray jsonPropertyArray = new JsonArray();
        for(Parameter property : getIntent().getParameters()) {
            JsonObject jsonPropertyObject = new JsonObject();
            jsonPropertyObject.addProperty("name", property.getName());
            jsonPropertyObject.addProperty("entity", property.getEntity().getName());
            jsonPropertyObject.addProperty("value", getEntityValues().get(property.getEntity().getName()));
            jsonPropertyArray.add(jsonPropertyObject);
        }
        jsonObject.add("properties", jsonPropertyArray);
        return jsonObject;
    }

    public List<Context> getContext() {
        return outputContexts;
    }

    public double getScore() {
        return score;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void stopMeasurement() {
        if(executionTime == 0) {
            executionTime = System.nanoTime() - startTime;
        }
    }
}
