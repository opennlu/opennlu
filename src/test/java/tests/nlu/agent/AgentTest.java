package tests.nlu.agent;

import org.junit.After;
import org.junit.Before;
import org.opennlu.OpenNLU;
import org.opennlu.agent.Agent;
import org.opennlu.agent.AgentResponse;
import org.opennlu.agent.context.Context;
import org.opennlu.agent.skill.Skill;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by René Preuß on 5/15/2017.
 */
public class AgentTest {
    private Agent agent;
    private OpenNLU openNLU;

    @Before
    public final void setUp() throws Exception {
        System.out.println("AgentTest.setUp");


        openNLU = new OpenNLU(OpenNLU.getLocalConfig(new File("config.json")));
        agent = new Agent(getNLU(), 2);
    }

    @After
    public final void tearDown() {
        System.out.println("AgentTest.tearDown");
    }

    public Agent getAgent() {
        return agent;
    }

    public void printResponse(AgentResponse response) {
        System.out.println("-------------------------------------------------------------------");
        System.out.println(String.format("Input: %s", response.getMessage()));
        System.out.println(String.format("Output: %s", response.getFulfillment().getResponse()));
        System.out.println(String.format("Intent: %s", response.getIntent().getName()));
        System.out.println(String.format("Score: %s", response.getScore()));
        System.out.println("-------------------------------------------------------------------");
    }

    public static void printSession(String prefix, List<Context> contexts) {
        printSession(prefix, contexts, new HashMap<>());
    }

    public static void printSession(String prefix, List<Context> contexts, Map<String, String> parameters) {
        System.out.print(prefix);
        for (Context context : contexts) {
            System.out.print(String.format("%s:%s ", context.getName(), context.getTimeToLive()));
        }
        System.out.print("\n > Parameters:");
        for (Map.Entry<String, String> parameter: parameters.entrySet()) {
            System.out.print(String.format("%s:%s ", parameter.getKey(), parameter.getValue()));
        }
        System.out.println();
    }

    public OpenNLU getNLU() {
        return openNLU;
    }
}
