package org.opennlu.agent.fulfillment;

import org.opennlu.agent.AgentResponse;
import org.opennlu.agent.intent.Parameter;

import java.util.Random;

/**
 * Created by René Preuß on 9/2/2017.
 */
public class ParameterFulfillment extends Fulfillment {
    private final Parameter missedParameter;

    public ParameterFulfillment(AgentResponse agentResponse, Parameter missedParameter) {
        super(agentResponse);
        this.missedParameter = missedParameter;
    }

    @Override
    public String getResponse() {
        return missedParameter.getFallbackStrings()[new Random().nextInt(missedParameter.getFallbackStrings().length)];
    }
}
