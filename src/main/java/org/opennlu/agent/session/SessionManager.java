package org.opennlu.agent.session;

import org.opennlu.agent.Agent;
import org.opennlu.agent.context.Context;

import java.util.List;

/**
 * Created by René Preuß on 8/31/2017.
 */
public class SessionManager {
    private Agent agent;

    public SessionManager(Agent agent) {
        this.agent = agent;
    }

    public Session createSession() {
        return new Session(this, agent);
    }

    public Session getSessionById(int id, List<Context> inputContext) {
        return new Session(this, agent, id, inputContext);
    }
}
