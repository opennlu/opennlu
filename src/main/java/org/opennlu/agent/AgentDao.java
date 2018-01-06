package org.opennlu.agent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rene on 6/14/2017.
 */
public class AgentDao {

    private List<Agent> agentList = new ArrayList<>();

    public AgentDao() throws Exception {
        loadAgent(new File("examples/agents/example"));
    }

    private void loadAgent(File configs) throws Exception {
        Agent agent = new Agent(configs);
        agentList.add(agent);
    }

    public Agent findAgent(String name) {
        for(Agent a : agentList) {
            if(a.getBaseDirectory().getName().equals(name)) {
                return a;
            }
        }
        return null;
    }
}
