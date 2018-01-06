package org.opennlu.agent;

import org.opennlu.OpenNLU;
import org.opennlu.agent.entity.EntityManager;
import org.opennlu.agent.intent.IntentManager;
import org.opennlu.agent.session.SessionManager;
import org.opennlu.agent.skill.SkillManager;
import org.opennlu.agent.training.TrainingManager;
import org.opennlu.json.ConfigSection;

/**
 * Created by René Preuß on 6/2/2017.
 */
public class Agent {
    private final EntityManager entityManager;
    private final IntentManager intentManager;
    private final ConfigSection configSection;
    private final TrainingManager trainingManager;
    private final OpenNLU openNLU;

    private SkillManager skillManager;
    private SessionManager sessionManager;

    public Agent(OpenNLU openNLU, int id) throws Exception {
        this.openNLU = openNLU;
        this.configSection = openNLU.getDatabase().getAgentConfig(id);

        // Entities & Intents & ML
        this.entityManager = new EntityManager(this);
        this.intentManager = new IntentManager(this);
        this.trainingManager = new TrainingManager(this);
        this.skillManager = new SkillManager(this);
        this.sessionManager = new SessionManager(this);

        // TODO add primary skills?
        //getSkillManager().addSkill(new Skill("example"));
    }

    public int getId() {
        return configSection.getInt("id");
    }

    public String getLanguage() {
        return configSection.getString("language");
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public IntentManager getIntentManager() {
        return intentManager;
    }

    public TrainingManager getTrainingManager() {
        return trainingManager;
    }

    public ConfigSection getConfig() {
        return configSection;
    }

    public SkillManager getSkillManager() {
        return skillManager;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public OpenNLU getNLU() {
        return openNLU;
    }
}
