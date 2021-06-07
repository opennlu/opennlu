package org.opennlu.agent.context;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by René Preuß on 6/8/2017.
 */
public class Context {
    private final String name;
    private final JsonObject value;
    private int ttl;

    public Context(String name) {
        this.name = name;
        this.value = new JsonObject();
        this.ttl = 0;
    }

    public Context(String name, int ttl) {
        this.name = name;
        this.value = new JsonObject();
        this.ttl = ttl;
    }

    public Context(String name, JsonObject value, int ttl) {
        this.name = name;
        this.value = value;
        this.ttl = ttl;
    }

    public static int getIdentifyer(List<Context> inputContexts) {
        int hashCode = 31;
        for (Context context : inputContexts) {
            hashCode += context.getName().hashCode();
        }
        return hashCode;
    }

    /**
     * @param jsonArray
     * @return
     */
    public static List<Context> fromJsonArray(JsonArray jsonArray) {
        List<Context> contextList = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonContextObject = jsonElement.getAsJsonObject();
            if (jsonContextObject.has("value")) {
                contextList.add(new Context(jsonContextObject.get("name").getAsString(), jsonContextObject.get("value").getAsJsonObject(), jsonContextObject.get("ttl").getAsInt()));
            } else if (jsonContextObject.has("ttl")) {
                contextList.add(new Context(jsonContextObject.get("name").getAsString(), jsonContextObject.get("ttl").getAsInt()));
            } else {
                contextList.add(new Context(jsonContextObject.get("name").getAsString()));
            }
        }

        return contextList;
    }

    public String getName() {
        return name;
    }

    public JsonObject getValue() {
        return value;
    }

    public boolean decreaseTimeToLive() {
        this.ttl -= 1;
        return ttl > 0;
    }

    public int getTimeToLive() {
        return ttl;
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", name);
        jsonObject.add("value", value);
        jsonObject.addProperty("ttl", ttl);
        return jsonObject;
    }
}
