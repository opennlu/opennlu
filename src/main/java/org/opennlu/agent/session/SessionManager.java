package org.opennlu.agent.session;

import org.opennlu.agent.Agent;
import org.opennlu.agent.AgentResponse;

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
}
