package org.opennlu.agent.entity;

import com.google.gson.JsonObject;
import opennlp.tools.namefind.TokenNameFinder;
import org.opennlu.agent.intent.Intent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by René Preuß on 6/2/2017.
 */
public abstract class Entity {
    private final String name;
    private final EntityType type;
    private final List<String> samplesList = new ArrayList<>();
    private TokenNameFinder tokenNameFinder;

    public Entity(String name, EntityType type, String[] samples) {
        this.name = name;
        this.type = type;
        Collections.addAll(samplesList, samples);
    }

    public List<String> getSamples() {
        return samplesList;
    }

    public String getName() {
        return name;
    }

    public EntityType getType() {
        return type;
    }

    public void train(Intent intent) throws IOException {
        for(String rawString : intent.getRawStrings()) {
            if(rawString.contains(name + "{")) {
                samplesList.add(rawString.replaceAll("@[a-z0-9.-]*\\{([^}]*)}", "<START:" + name.substring(1) + "> $1 <END>"));
            }
        }
    }

    public TokenNameFinder getTokenNameFinder() {
        return tokenNameFinder;
    }

    public void setTokenNameFinder(TokenNameFinder tokenNameFinder) {
        this.tokenNameFinder = tokenNameFinder;
    }

    public abstract void train(String language) throws Exception;

    public abstract JsonObject toJson();
}
