package org.opennlu.agent.skill;

import org.opennlu.agent.Agent;
import org.opennlu.agent.entity.Entity;
import org.opennlu.agent.intent.Intent;
import org.opennlu.json.ConfigSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by René Preuß on 8/31/2017.
 */
public class SkillManager {
    private final Agent agent;
    private List<Skill> skills = new ArrayList<>();

    private Map<Skill, List<Intent>> installedSkillIntents = new HashMap<>();
    private Map<Skill, List<Entity>> installedSkillEntities = new HashMap<>();

    public SkillManager(Agent agent) {
        this.agent = agent;
    }

    public Skill addSkill(Skill skill) throws Exception {
        System.out.println(String.format("Lean new skill %s for %s", skill, agent.getId()));
        List<Entity> installedEntities = new ArrayList<>();
        for (ConfigSection entity : skill.getEntities()) {
            Entity newEnt = agent.getEntityManager().registerEntity(entity);
            System.out.println("NewEnt: " + newEnt);
            installedEntities.add(newEnt);
        }
        installedSkillEntities.put(skill, installedEntities);

        List<Intent> installedIntents = new ArrayList<>();
        for (ConfigSection intent : skill.getIntents()) {
            installedIntents.add(agent.getIntentManager().registerIntent(intent));
        }
        installedSkillIntents.put(skill, installedIntents);

        agent.getTrainingManager().trainEntities();

        skills.add(skill);
        return skill;
    }

    public void removeSkill(Skill skill) {
        for (Entity entity : installedSkillEntities.get(skill)) {
            agent.getEntityManager().unregisterEntity(entity);
        }

        for (Intent intent : installedSkillIntents.get(skill)) {
            agent.getIntentManager().unregisterIntent(intent);
        }

        agent.getTrainingManager().trainEntities();
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void forgetSkills() {
        for (Skill skill : skills) {
            removeSkill(skill);
        }
    }
}
