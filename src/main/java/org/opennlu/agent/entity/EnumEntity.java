package org.opennlu.agent.entity;

import com.google.gson.JsonObject;
import opennlp.tools.namefind.RegexNameFinder;

import java.util.regex.Pattern;

/**
 * Created by René Preuß on 6/2/2017.
 */
public class EnumEntity extends Entity {
    private final Pattern[] patterns;

    public EnumEntity(String name, String[] samples) {
        super(name, EntityType.ENUM, samples);
        patterns = new Pattern[samples.length];
        for (int i = 0; i < samples.length; i++) {
            patterns[i] = Pattern.compile("/^" + Pattern.quote(samples[i]) + "$/");
        }
    }

    @Override
    public void train(String language) throws Exception {
        // fixme: find a better way to train this
        setTokenNameFinder(new RegexNameFinder(patterns, "en"));
    }

    @Override
    public JsonObject toJson() {
        JsonObject jsonEntityObject = new JsonObject();
        jsonEntityObject.addProperty("name", getName());
        jsonEntityObject.addProperty("type", getType().name());
        return jsonEntityObject;
    }
}
