package org.opennlu.agent.skill;

import org.opennlu.OpenNLU;
import org.opennlu.json.ConfigSection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by René Preuß on 6/2/2017.
 */
public class Skill {

    private final List<ConfigSection> entities = new ArrayList<>();
    private final List<ConfigSection> intents = new ArrayList<>();
    private final int id;
    private final String name;

    public Skill(OpenNLU openNLU, int id, String name) {
        this.id = id;
        this.name = name;
        entities.addAll(openNLU.getDatabase().getEntityConfigsBySkill(id));
        intents.addAll(openNLU.getDatabase().getIntentConfigs(id));
    }

    public List<ConfigSection> getEntities() {
        return entities;
    }

    public List<ConfigSection> getIntents() {
        return intents;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
