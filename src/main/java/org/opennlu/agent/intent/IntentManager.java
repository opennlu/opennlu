package org.opennlu.agent.intent;

import org.opennlu.agent.Agent;
import org.opennlu.agent.context.Context;
import org.opennlu.agent.entity.Entity;
import org.opennlu.json.ConfigSection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by René Preuß on 6/14/2017.
 */
public class IntentManager {
    private final Agent agent;
    private List<Intent> intents = new ArrayList<>();

    public IntentManager(Agent agent) throws Exception {
        this.agent = agent;

        // Register fallback intent
        this.intents.add(new Intent(
                0,
                "fallback",
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                "fallback",
                new ArrayList<>(),
                Arrays.asList(
                        "Kannst Du bitte deine Frage umformulieren.",
                        "Ich habe dich nicht verstanden.",
                        "Entschuldigung! Ich bin mir noch nicht sicher, wie ich dir da helfen kann."
                )
        ));
    }

    public Intent findIntent(String name) {
        for (Intent intent : intents)
            if(intent.getName().equals(name))
                return intent;
        return null;
    }

    public Intent registerIntent(ConfigSection intentConfiguration) throws Exception {

        if(!intentConfiguration.has("name")) {
            throw new Exception("The parameter 'name' is missing.");
        } else if(!intentConfiguration.has("user_says")) {
            throw new Exception("The parameter 'user_says' is missing.");
        } else if(!intentConfiguration.has("action")) {
            throw new Exception("The parameter 'action' is missing.");
        } else if(!intentConfiguration.has("response")) {
            throw new Exception("The parameter 'response' is missing.");
        } else if(!intentConfiguration.getConfigSection("response").has("text_response")) {
            throw new Exception("The parameter 'response.text_response' is missing.");
        } else {
            String intentName = intentConfiguration.get("name").getAsString();
            if (findIntent(intentName) != null) {
                throw new Exception(String.format("Intent '%s' already exists.", intentName));
            } else {
                // Build parameters
                List<Parameter> parameterList = new ArrayList<>();
                if (intentConfiguration.has("parameters")) {
                    for (ConfigSection propertySection : intentConfiguration.getConfigSectionList("parameters")) {
                        if (!propertySection.has("name") || !propertySection.has("entity")) {
                            throw new Exception("Required parameters (name or entity) for the parameter are missing.");
                        } else {
                            String entityName = propertySection.get("entity").getAsString();
                            Entity entity = agent.getEntityManager().findEntity(entityName);
                            if (entity == null) {
                                throw new Exception(String.format("The entity %s does not exist.", entityName));
                            }
                            List<String> fallbackStrings = propertySection.getList("fallback");
                            parameterList.add(new Parameter(
                                    propertySection.get("name").getAsString(),
                                    propertySection.has("required") && propertySection.get("required").getAsBoolean(),
                                    entity,
                                    fallbackStrings.toArray(new String[fallbackStrings.size()]))
                            );
                        }
                    }
                }

                ConfigSection contextSection = intentConfiguration.getConfigSection("contexts");

                Intent intent = new Intent(
                        intentConfiguration.getInt("id"), // todo add support to register new intents
                        intentName,
                        contextSection.has("input_contexts") ?
                                Context.fromJsonArray(contextSection.getJsonArray("input_contexts")) :
                                new ArrayList<>(),
                        contextSection.has("output_contexts") ?
                                Context.fromJsonArray(contextSection.getJsonArray("output_contexts")) :
                                new ArrayList<>(),
                        intentConfiguration.getList("user_says"),
                        intentConfiguration.getString("action"),
                        parameterList,
                        intentConfiguration.getConfigSection("response").getList("text_response")
                );
                return registerIntent(intent);
            }
        }
    }

    public Intent registerIntent(Intent intent) {
        intents.add(intent);
        return intent;
    }

    public List<Intent> getIntents() {
        return intents;
    }

    public void unregisterIntent(Intent intent) {
        intents.remove(intent);
    }

    public Intent getFallbackIntent() {
        return intents.get(0);
    }

    public List<Intent> findIntents(List<Context> inputContexts) {
        List<Intent> selectedIntents = new ArrayList<>();

        if(inputContexts.size() < 1) {
            for (Intent intent : intents)
                if(intent.getInputContexts().size() < 1)
                    selectedIntents.add(intent);

            return selectedIntents;
        }

        for (Intent intent : intents)
            for(Context context : intent.getInputContexts())
                for(Context inputContext : inputContexts)
                    if(context.getName().equals(inputContext.getName()))
                        selectedIntents.add(intent);

        return selectedIntents;
    }

    public void forgetIntents() {
        for(Intent intent : intents) {
            unregisterIntent(intent);
        }
    }
}
