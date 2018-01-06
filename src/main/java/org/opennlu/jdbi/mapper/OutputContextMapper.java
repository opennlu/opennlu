package org.opennlu.jdbi.mapper;

import org.opennlu.json.ConfigSection;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OutputContextMapper implements ResultSetMapper<ConfigSection> {
    @Override
    public ConfigSection map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        ConfigSection config = new ConfigSection();
        config.set("name", resultSet.getString("name"));
        config.set("ttl", resultSet.getString("ttl"));
        return config;
    }
}
