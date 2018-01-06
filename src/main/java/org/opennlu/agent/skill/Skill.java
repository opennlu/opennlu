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

    public Skill(OpenNLU openNLU, int skillId) {
        entities.addAll(openNLU.getDatabase().getEntityConfigsBySkill(skillId));
        intents.addAll(openNLU.getDatabase().getIntentConfigs(skillId));
    }

    public List<ConfigSection> getEntities() {
        return entities;
    }

    public List<ConfigSection> getIntents() {
        return intents;
    }
}
