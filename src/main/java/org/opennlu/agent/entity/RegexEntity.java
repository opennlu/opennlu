package org.opennlu.agent.entity;

import com.google.gson.JsonObject;
import opennlp.tools.namefind.RegexNameFinder;

import java.util.regex.Pattern;

/**
 * Created by René Preuß on 6/2/2017.
 */
public class RegexEntity extends Entity {
    private final Pattern[] patterns;

    public RegexEntity(String name, String[] samples, Pattern[] patterns) {
        super(name, EntityType.REGEX, samples);
        this.patterns = patterns;
    }

    @Override
    public void train(String language) throws Exception {
        setTokenNameFinder(new RegexNameFinder(patterns));
    }

    @Override
    public JsonObject toJson() {
        JsonObject jsonEntityObject = new JsonObject();
        jsonEntityObject.addProperty("name", getName());
        jsonEntityObject.addProperty("type", getType().name());
        return jsonEntityObject;
    }
}
