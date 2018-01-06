package org.opennlu.agent.skill;

import org.opennlu.agent.Agent;
import org.opennlu.json.JsonConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by René Preuß on 8/31/2017.
 */
public class SkillManager {
    private final Agent agent;
    private List<Skill> skills = new ArrayList<>();

    public SkillManager(Agent agent) {
        this.agent = agent;
    }

    public Skill addSkill(Skill skill) throws Exception {
        for(JsonConfig entity : skill.getEntities()) {
            agent.getEntityManager().registerEntity(entity);
        }
        agent.getTrainingManager().trainEntities();

        for(JsonConfig intent : skill.getIntents()) {
            agent.getIntentManager().registerIntent(intent);
        }

        this.skills.add(skill);
        return skill;
    }

    public List<Skill> getSkills() {
        return skills;
    }
}
