package org.opennlu.jdbi;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import org.opennlu.OpenNLU;
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
        dbPool.setUrl("jdbc:mysql://kaedeai.c2.to/wh2_kaedeai?connectTimeout=0&autoReconnect=true");
        dbPool.setUser("wh2_kaedeai");
        dbPool.setPassword("Kennwort1");
        handle = new DBI(dbPool).open();
    }

    public ConfigSection getAgentConfig(int id) {
        return handle.createQuery("SELECT * FROM `ai_agents` WHERE `id` = :identifier;")
                .bind("identifier", id)
                .map(new AgentMapper())
                .first();
    }

    public List<ConfigSection> getEntityConfigsBySkill(int skillId) {
        return handle.createQuery("SELECT * FROM `ai_entities` WHERE `ai_skill_id` = :identifier;")
                .bind("identifier", skillId)
                .map(new EntityMapper())
                .list();
    }

    public List<ConfigSection> getIntentConfigs(int skillId) {
        return handle.createQuery("SELECT * FROM `ai_intents` WHERE `ai_skill_id` = :identifier;")
                .bind("identifier", skillId)
                .map(new IntentMapper(openNLU))
                .list();
    }

    public List<String> getIntentUserSays(int intentId) {
        return handle.createQuery("SELECT * FROM `ai_intent_user_says` WHERE `ai_intent_id` = :identifier;")
                .bind("identifier", intentId)
                .map(new StringMapper("content"))
                .list();
    }

    public List<ConfigSection> getIntentInputContexts(int intentId) {
        return handle.createQuery("SELECT * FROM `ai_intent_input_contexts` WHERE `ai_intent_id` = :identifier;")
                .bind("identifier", intentId)
                .map(new InputContextMapper())
                .list();
    }

    public List<ConfigSection> getIntentOutpurContexts(int intentId) {
        return handle.createQuery("SELECT * FROM `ai_intent_output_contexts` WHERE `ai_intent_id` = :identifier;")
                .bind("identifier", intentId)
                .map(new OutputContextMapper())
                .list();
    }

    public List<String> getIntentTextResponses(int intentId) {
        return handle.createQuery("SELECT * FROM `ai_intent_responses` WHERE `ai_intent_id` = :identifier;")
                .bind("identifier", intentId)
                .map(new StringMapper("content"))
                .list();
    }

    public List<ConfigSection> getIntentParameterConfigs(int intentId) {
        return handle.createQuery("SELECT * FROM `ai_intent_parameters` WHERE `ai_intent_id` = :identifier;")
                .bind("identifier", intentId)
                .map(new ParameterMapper(openNLU))
                .list();
    }

    public ConfigSection getEntityConfigById(String entityId) {
        return handle.createQuery("SELECT * FROM `ai_entities` WHERE `id` = :identifier;")
                .bind("identifier", entityId)
                .map(new EntityMapper())
                .first();
    }

    public List<String> getIntentParameterFallbacks(String intentId) {
        return handle.createQuery("SELECT * FROM `ai_intent_parameter_fallbacks` WHERE `ai_intent_parameter_id` = :identifier;")
                .bind("identifier", intentId)
                .map(new StringMapper("content"))
                .list();
    }
}
