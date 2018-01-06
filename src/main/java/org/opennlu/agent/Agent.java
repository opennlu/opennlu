package org.opennlu.agent;

import org.opennlu.agent.context.Context;
import org.opennlu.agent.entity.EntityManager;
import org.opennlu.agent.intent.IntentManager;
import org.opennlu.agent.skill.Skill;
import org.opennlu.agent.training.TrainingManager;
import org.opennlu.agent.session.SessionManager;
import org.opennlu.agent.skill.SkillManager;
import org.opennlu.json.ConfigSection;
import org.opennlu.json.JsonConfig;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by René Preuß on 6/2/2017.
 */
public class Agent {
    private final File baseDirectory;
    private final EntityManager entityManager;
    private final IntentManager intentManager;
    private final JsonConfig jsonConfig;
    private final File jsonFile;
    private final TrainingManager trainingManager;
    private SkillManager skillManager;
    private SessionManager sessionManager;

    public Agent(File baseDirectory) throws Exception {
        this.baseDirectory = baseDirectory;
        // Configuration
        this.jsonFile = new File(getBaseDirectory(), "config.json");
        this.jsonConfig = JsonConfig.loadConfiguration(jsonFile);

        getConfig().setIfNull("language", "de");
        ConfigSection webhookSection = getConfig()
                .getConfigSection("fulfillment")
                .getConfigSection("webhook");
        webhookSection.setIfNull("url", "http://localhost:8080/ai");
        webhookSection.setIfNull("username", "");
        webhookSection.setIfNull("password", "");
        saveConfig();

        // Entities & Intents & ML
        this.entityManager = new EntityManager(this);
        this.intentManager = new IntentManager(this);
        this.trainingManager = new TrainingManager(this);
        this.skillManager = new SkillManager(this);
        this.sessionManager = new SessionManager(this);

        // Add skills
        getSkillManager().addSkill(new Skill(new File(getBaseDirectory(), "skills")));
    }

    private void saveConfig() {
        try {
            getConfig().save(jsonFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLanguage() {
        return jsonConfig.getString("language");
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public File getBaseDirectory() {
        return baseDirectory;
    }

    public IntentManager getIntentManager() {
        return intentManager;
    }

    public TrainingManager getTrainingManager() {
        return trainingManager;
    }

    public JsonConfig getConfig() {
        return jsonConfig;
    }

    public SkillManager getSkillManager() {
        return skillManager;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
