package org.opennlu.jdbi;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import org.opennlu.OpenNLU;
import org.opennlu.agent.Agent;
import org.opennlu.agent.AgentResponse;
import org.opennlu.agent.session.Session;
import org.opennlu.agent.skill.Skill;
import org.opennlu.jdbi.mapper.*;
import org.opennlu.json.ConfigSection;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import java.util.List;

public class Database {

    private final OpenNLU openNLU;
    private Handle handle;

    public Database(OpenNLU openNLU) {
        this.openNLU = openNLU;
        MysqlConnectionPoolDataSource dbPool = new MysqlConnectionPoolDataSource();
        dbPool.setUrl(openNLU.getConfig().getConfigSection("SQL").getString("ConnectionString"));
        dbPool.setUser(openNLU.getConfig().getConfigSection("SQL").getString("Username"));
        dbPool.setPassword(openNLU.getConfig().getConfigSection("SQL").getString("Password"));
        handle = new DBI(dbPool).open();
    }

    public ConfigSection getAgentConfig(int id) {
        return handle.createQuery("SELECT * FROM `agents` WHERE `id` = :identifier;")
                .bind("identifier", id)
                .map(new AgentMapper())
                .first();
    }

    public List<ConfigSection> getEntityConfigsBySkill(int skillId) {
        return handle.createQuery("SELECT * FROM `entities` WHERE `skill_id` = :identifier;")
                .bind("identifier", skillId)
                .map(new EntityMapper())
                .list();
    }

    public List<ConfigSection> getIntentConfigs(int skillId) {
        return handle.createQuery("SELECT * FROM `intents` WHERE `skill_id` = :identifier;")
                .bind("identifier", skillId)
                .map(new IntentMapper(openNLU))
                .list();
    }

    public List<String> getIntentUserSays(int intentId) {
        return handle.createQuery("SELECT * FROM `intent_user_says` WHERE `intent_id` = :identifier;")
                .bind("identifier", intentId)
                .map(new StringMapper("content"))
                .list();
    }

    public List<ConfigSection> getIntentInputContexts(int intentId) {
        return handle.createQuery("SELECT * FROM `intent_input_contexts` WHERE `intent_id` = :identifier;")
                .bind("identifier", intentId)
                .map(new InputContextMapper())
                .list();
    }

    public List<ConfigSection> getIntentOutputContexts(int intentId) {
        return handle.createQuery("SELECT * FROM `intent_output_contexts` WHERE `intent_id` = :identifier;")
                .bind("identifier", intentId)
                .map(new OutputContextMapper())
                .list();
    }

    public List<String> getIntentTextResponses(int intentId) {
        return handle.createQuery("SELECT * FROM `intent_responses` WHERE `intent_id` = :identifier;")
                .bind("identifier", intentId)
                .map(new StringMapper("content"))
                .list();
    }

    public List<ConfigSection> getIntentParameterConfigs(int intentId) {
        return handle.createQuery("SELECT * FROM `intent_parameters` WHERE `intent_id` = :identifier;")
                .bind("identifier", intentId)
                .map(new ParameterMapper(openNLU))
                .list();
    }

    public ConfigSection getEntityConfigById(String entityId) {
        return handle.createQuery("SELECT * FROM `entities` WHERE `id` = :identifier;")
                .bind("identifier", entityId)
                .map(new EntityMapper())
                .first();
    }

    public List<String> getIntentParameterFallbacks(String intentId) {
        return handle.createQuery("SELECT * FROM `intent_parameter_fallbacks` WHERE `intent_parameter_id` = :identifier;")
                .bind("identifier", intentId)
                .map(new StringMapper("content"))
                .list();
    }

    public int createSession(Agent agent) {
        return handle.insert("INSERT INTO `sessions` (agent_id, created_at, updated_at) VALUES (?, NOW(), NOW());", agent.getId());
    }

    public void createQuery(Agent agent, Session session, AgentResponse response) {
        if (response.getMessage().length() > 0) {
            handle.createStatement("INSERT INTO `queries` (query, exec_time, intent_id, session_id, agent_id, created_at, updated_at) VALUES (:query, :exec_time, :intent_id, :session_id, :agent_id, NOW(), NOW());")
                    .bind("query", response.getMessage())
                    .bind("exec_time", response.getExecutionTime())
                    .bind("intent_id", response.getIntent().getId())
                    .bind("session_id", session.getId())
                    .bind("agent_id", agent.getId())
                    .execute();
        }
    }

    public List<Skill> getAgentSkills(int agentId) {
        return handle.createQuery("SELECT skills.* FROM agent_skills" +
                " JOIN skills ON (skills.id = agent_skills.skill_id)" +
                " WHERE agent_skills.agent_id = :identifier;")
                .bind("identifier", agentId)
                .map(new SkillMapper(openNLU))
                .list();
    }
}
