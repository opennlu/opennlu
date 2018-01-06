package org.opennlu.jdbi.mapper;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StringMapper implements ResultSetMapper<String> {
    private final String property;

    public StringMapper(String property) {
        this.property = property;
    }

    @Override
    public String map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return resultSet.getString(property);
    }
}
