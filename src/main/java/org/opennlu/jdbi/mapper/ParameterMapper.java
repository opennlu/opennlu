package org.opennlu.jdbi.mapper;

import org.opennlu.OpenNLU;
import org.opennlu.json.ConfigSection;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ParameterMapper implements ResultSetMapper<ConfigSection> {

    private final OpenNLU openNLU;

    public ParameterMapper(OpenNLU openNLU) {
        this.openNLU = openNLU;
    }

    @Override
    public ConfigSection map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        ConfigSection config = new ConfigSection();
        config.set("name", resultSet.getString("name"));
        config.set("required", resultSet.getBoolean("required"));
        config.set("entity", openNLU.getDatabase().getEntityConfigById(resultSet.getString("entity_id")).getString("name"));
        config.set("fallback", openNLU.getDatabase().getIntentParameterFallbacks(resultSet.getString("id")));
        return config;
    }
}
