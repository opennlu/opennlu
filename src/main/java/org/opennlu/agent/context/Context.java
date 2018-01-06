package org.opennlu.agent.context;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by René Preuß on 6/8/2017.
 */
public class Context {
    private final String name;
    private int ttl;

    public Context(String name) {
        this.name = name;
        this.ttl = 0;
    }

    public Context(String name, int ttl) {
        this.name = name;
        this.ttl = ttl;
    }

    public String getName() {
        return name;
    }

    public boolean decreaseTimeToLive() {
        this.ttl -= 1;
        return ttl > 0;
    }

    public int getTimeToLive() {
        return ttl;
    }

    public static int getIdentifyer(List<Context> inputContexts) {
        int hashCode = 31;
        for (Context context : inputContexts) {
            hashCode += context.getName().hashCode();
        }
        return hashCode;
    }

    public static List<Context> fromJsonArray(JsonArray jsonArray) {
        List<Context> contextList = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray) {
            if(jsonElement.isJsonPrimitive()) {
                contextList.add(new Context(jsonElement.getAsString()));
            } else {
                JsonArray jsonContextArray = jsonElement.getAsJsonArray();
                contextList.add(new Context(jsonContextArray.get(0).getAsString(), jsonContextArray.get(1).getAsInt()));
            }
        }

        return contextList;
    }
}
