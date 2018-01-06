package org.opennlu.agent.skill;

import org.opennlu.json.JsonConfig;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by René Preuß on 6/2/2017.
 */
public class Skill {

    private final List<JsonConfig> entities = new ArrayList<>();
    private final List<JsonConfig> intents = new ArrayList<>();

    public Skill(File skillDirectory) throws IOException {
        // Register entities from entities directory
        File entitiesDirectory = new File(skillDirectory, "entities");
        if(!entitiesDirectory.exists()) {
            if(!entitiesDirectory.mkdirs())
                System.out.println(String.format("Cannot create directory '%s'", entitiesDirectory.getAbsoluteFile()));
        }

        File[] entitiesFiles = entitiesDirectory.listFiles();
        if(entitiesFiles != null) {
            for(File intentFile : entitiesFiles) {
                entities.add(JsonConfig.loadConfiguration(intentFile));
            }
        }

        // Register intents from intents directory
        File intentsDirectory = new File(skillDirectory, "intents");
        if(!intentsDirectory.exists()) {
            if(!intentsDirectory.mkdirs())
                System.out.println(String.format("Cannot create directory '%s'", intentsDirectory.getAbsoluteFile()));
        }

        registerIntents(intentsDirectory);
    }

    private void registerIntents(File intentsDirectory) throws IOException {
        File[] intentFiles = intentsDirectory.listFiles();
        if(intentFiles != null) {
            for(File intentFile : intentFiles) {
                if(intentFile.isFile())
                    intents.add(JsonConfig.loadConfiguration(intentFile));
                registerIntents(intentFile);
            }
        }
    }

    public List<JsonConfig> getEntities() {
        return entities;
    }

    public List<JsonConfig> getIntents() {
        return intents;
    }
}
