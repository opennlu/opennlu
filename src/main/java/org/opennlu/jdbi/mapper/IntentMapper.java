package org.opennlu.jdbi.mapper;

import org.opennlu.OpenNLU;
import org.opennlu.json.ConfigSection;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IntentMapper implements ResultSetMapper<ConfigSection> {
    public static final int INTENT_SCHEMA_VERSION = 2;
    private final OpenNLU openNLU;

    public IntentMapper(OpenNLU openNLU) {
        this.openNLU = openNLU;
    }

    @Override
    public ConfigSection map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        ConfigSection config = new ConfigSection();
        config.set("schema", INTENT_SCHEMA_VERSION);
        config.set("name", resultSet.getString("name"));
        config.getConfigSection("contexts")
                .setConfigSectionList("input_contexts", openNLU.getDatabase().getIntentInputContexts(resultSet.getInt("id")));
        config.getConfigSection("contexts")
                .setConfigSectionList("output_contexts", openNLU.getDatabase().getIntentOutpurContexts(resultSet.getInt("id")));
        config.set("user_says", openNLU.getDatabase().getIntentUserSays(resultSet.getInt("id")));
        config.set("action", resultSet.getString("action"));
        config.setConfigSectionList("parameters", openNLU.getDatabase().getIntentParameterConfigs(resultSet.getInt("id")));
        config.getConfigSection("response")
                .set("text_response", openNLU.getDatabase().getIntentTextResponses(resultSet.getInt("id")));
        return config;
    }
}
