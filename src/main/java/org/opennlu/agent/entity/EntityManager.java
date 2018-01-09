package org.opennlu.agent.entity;

import org.opennlu.agent.Agent;
import org.opennlu.json.ConfigSection;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by René Preuß on 6/14/2017.
 */
public class EntityManager {

    private final Agent agent;
    private List<Entity> entities = new ArrayList<>();

    public EntityManager(Agent agent) throws Exception {
        this.agent = agent;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Entity findEntity(String name) {
        for (Entity entity : entities)
            if(entity.getName().equals(name))
                return entity;
        return null;
    }

    public Entity registerEntity(ConfigSection entityConfiguration) throws Exception {
        if(!entityConfiguration.has("name")) {
            throw new Exception("The parameter 'name' is missing.");
        } else if(!entityConfiguration.has("type")) {
            throw new Exception("The parameter 'type' is missing.");
        } else {
            EntityType entityType = EntityType.valueOf(entityConfiguration.getString("type"));
            Entity entity;
            switch (entityType) {
                case ENUM:
                    entity = new EnumEntity(entityConfiguration.getString("name"), new String[0]);
                    break;
                case REGEX:
                    entity = new RegexEntity(entityConfiguration.getString("name"), new String[0], new Pattern[0]);
                    break;
                case DYNAMIC:
                    entity = new DynamicEntity(entityConfiguration.getString("name"));
                    break;
                default:
                    throw new Exception(String.format("Entity type '%s' not found.", entityConfiguration.getString("type")));
            }
            return registerEntity(entity);
        }
    }

    public Entity registerEntity(Entity entity) {
        entities.add(entity);
        return entity;
    }

    public void unregisterEntity(Entity entity) {
        entities.remove(entity);
    }
}
