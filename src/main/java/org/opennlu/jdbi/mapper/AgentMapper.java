package org.opennlu.jdbi.mapper;

import org.opennlu.json.ConfigSection;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AgentMapper implements ResultSetMapper<ConfigSection> {
    @Override
    public ConfigSection map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        ConfigSection config = new ConfigSection();
        config.set("id", resultSet.getString("id"));
        config.set("language", resultSet.getString("language"));
        ConfigSection webhookSection = config
                .getConfigSection("fulfillment")
                .getConfigSection("webhook");
        webhookSection.set("url", resultSet.getString("webhook_url"));
        webhookSection.set("username", resultSet.getString("webhook_username"));
        webhookSection.set("password", resultSet.getString("webhook_password"));
        return config;
    }
}
