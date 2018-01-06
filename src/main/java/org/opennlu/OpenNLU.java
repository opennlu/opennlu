package org.opennlu;

import org.opennlu.jdbi.Database;

public class OpenNLU {
    private Database database;

    public OpenNLU() {
        this.database = new Database(this);
    }

    public Database getDatabase() {
        return database;
    }
}
