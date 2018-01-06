package tests.nlu;

import org.junit.Test;
import org.opennlu.OpenNLU;
import org.opennlu.agent.Agent;
import org.opennlu.agent.AgentResponse;
import org.opennlu.agent.context.Context;
import org.opennlu.agent.session.Session;
import org.opennlu.agent.skill.Skill;
import tests.nlu.agent.AgentTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by René Preuß on 6/14/2017.
 */
public class BasicTests extends AgentTest {
    private static final String INPUT_PROMPT = "> ";

    @Test
    public void testParse() throws Exception {
        Session session = getAgent().getSessionManager().createSession();

        printSession("Current Session: ", session.getInputContext());

        AgentResponse response = session.parse("Ich möchte dich Kaede nennen");

        assertEquals("Ich möchte dich Kaede nennen", response.getMessage());

        assertEquals(1, response.getEntityValues().size());
        assertEquals("Kaede", response.getEntityValues().get("@name"));
        assertEquals("agent.name.set", response.getIntent().getName());
        assertEquals("Mein Name ist jetzt Kaede.", response.getFulfillment().getResponse());

        printSession("Recalculated Session: ", response.getContext());
    }

    @Test
    public void testContexts() throws Exception {
        Session session;
        AgentResponse response;
        System.out.println();

        // test ai output contexts
        session = getAgent().getSessionManager().createSession();
        response = session.parse("Ich möchte süßigkeiten");
        printResponse(response);
        printSession("Input Session: ", session.getInputContext());
        System.out.println();

        response = session.parse("Pocky");
        printResponse(response);
        printSession("Input Session: ", session.getInputContext());
        System.out.println();

        // test ai input contexts
        response = session.parse("Pocky");
        printResponse(response);
        printSession("Input Session: ", session.getInputContext());
        System.out.println();

    }

    @Test
    public void testParametersFulfillment() throws Exception {
        Session session;
        AgentResponse response;
        System.out.println();

        // test ai output contexts
        session = getAgent().getSessionManager().createSession();
        response = session.parse("Kann ich deinen Namen ändern?");
        printResponse(response);
        printSession("Input Session: ", session.getInputContext(), session.getInputParameters());
        System.out.println();

        response = session.parse("Kaede");
        printResponse(response);

        // test ai input contexts
        response = session.parse("Mustermann");
        printSession("Input Session: ", session.getInputContext(), session.getInputParameters());
        printResponse(response);
        printSession("Final Session: ", session.getInputContext(), session.getInputParameters());

    }

    public static void main(String[] args) throws Exception {
        OpenNLU openNLU = new OpenNLU();
        Agent agent = new Agent(openNLU, 1);

        // Add smalltalk skills
        agent.getSkillManager().addSkill(new Skill(openNLU, 1));

        agent.getTrainingManager().trainEntities();

        Session session = agent.getSessionManager().createSession();

        String line;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print(INPUT_PROMPT);
            while (null != (line = reader.readLine())) {

                try {
                    printSession("Input Session: ", session.getInputContext());
                    AgentResponse response = session.parse(line);
                    System.out.println(String.format("%s: %s (%s)", response.getIntent().getName(), response.getFulfillment().getResponse(), response.getMessage()));
                    printSession("Output Session: ", session.getInputContext());
                    System.out.println(response.toJson().toString());
                    System.out.println();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                System.out.print(INPUT_PROMPT);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("See ya!");
    }
}
