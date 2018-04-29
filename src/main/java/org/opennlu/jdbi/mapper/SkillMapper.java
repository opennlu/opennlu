package org.opennlu.jdbi.mapper;

import org.opennlu.OpenNLU;
import org.opennlu.agent.skill.Skill;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SkillMapper implements ResultSetMapper<Skill> {

    private final OpenNLU openNLU;

    public SkillMapper(OpenNLU openNLU) {
        this.openNLU = openNLU;
    }

    @Override
    public Skill map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new Skill(openNLU, resultSet.getInt("id"), resultSet.getString("name"));
    }
}
