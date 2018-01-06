package org.opennlu.agent.intent;

import org.opennlu.agent.entity.Entity;

/**
 * Created by René Preuß on 6/2/2017.
 */
public class Parameter {
    private final String name;
    private final boolean required;
    private final Entity entity;
    private final String[] fallbackStrings;

    public Parameter(String name, boolean required, Entity entity, String[] fallbackStrings) {
        this.name = name;
        this.required = required;
        this.entity = entity;
        this.fallbackStrings = fallbackStrings;
    }

    public Parameter(String name, boolean required, Entity entity) {
        this.name = name;
        this.required = required;
        this.entity = entity;
        this.fallbackStrings = new String[] { "Please send a value for " + name };
    }

    public Entity getEntity() {
        return entity;
    }

    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return required;
    }

    public String[] getFallbackStrings() {
        return fallbackStrings;
    }
}
