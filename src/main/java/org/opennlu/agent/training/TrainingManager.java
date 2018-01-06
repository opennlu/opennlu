package org.opennlu.agent.training;

import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.ObjectStreamUtils;
import opennlp.tools.util.TrainingParameters;

import org.opennlu.agent.Agent;
import org.opennlu.agent.AgentResponse;
import org.opennlu.agent.context.Context;
import org.opennlu.agent.entity.Entity;
import org.opennlu.agent.intent.Intent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by René Preuß on 6/14/2017.
 */
public class TrainingManager {
    private static final double AVG_SCORE_DIFF = 0.05;
    private static final double MIN_SCORE = 0.8;
    private static final boolean ALLOW_AVG_SCORE = false;

    private final Agent agent;
    private final Map<Integer, DocumentCategorizerME> categorizerMap = new HashMap<>();

    public TrainingManager(Agent agent) {
        this.agent = agent;
    }

    public void trainEntities() {
        // Train Entity
        for(Entity entity : agent.getEntityManager().getEntities()) {
            if(entity.getSamples().size() > 0) {
                try {
                    entity.train(agent.getLanguage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public AgentResponse parse(String message, List<Context> inputContexts) throws Exception {
        return parse(message, inputContexts, new HashMap<>());
    }

    public AgentResponse parse(String message, List<Context> inputContexts, Map<String, String> inputParameters) throws Exception {

        AgentResponse fulfilmentResponse = handleFulfilment(message, inputContexts, inputParameters);

        if(fulfilmentResponse != null) {
            return fulfilmentResponse;
        }

        DocumentCategorizerME categorizer = findCategorizer(inputContexts);

        if(categorizer == null)
            return new AgentResponse(message, agent.getIntentManager().getFallbackIntent(), inputContexts, inputParameters, 0);

        double[] outcome = categorizer.categorize(message.toLowerCase());
        double score = 0;
        double avgScore = 0;

        String bestCategory = categorizer.getBestCategory(outcome);
        for(int i=0;i<categorizer.getNumberOfCategories();i++){
            if(categorizer.getCategory(i).equals(bestCategory))
                score = outcome[i];
            avgScore += outcome[i];
        }

        avgScore = avgScore / categorizer.getNumberOfCategories();

        if(score >= MIN_SCORE || (ALLOW_AVG_SCORE && score >= (avgScore+AVG_SCORE_DIFF)))
            return new AgentResponse(message, agent.getIntentManager().findIntent(bestCategory), inputContexts, inputParameters, score);

        return new AgentResponse(message, agent.getIntentManager().getFallbackIntent(), inputContexts, inputParameters, score);
    }

    private AgentResponse handleFulfilment(String message, List<Context> inputContexts, Map<String, String> inputParameters) throws Exception {
        Intent fulfilmentIntent = null;
        String fulfilmentMessage = null;
        double fulfilmentScore = 0;

        for(Context context : inputContexts) {
            if(context.getName().startsWith("fulfilment-")) {
                fulfilmentIntent = agent.getIntentManager().findIntent(context.getName().substring(11));
            } else if(context.getName().startsWith("parameter-")) {
                inputParameters.put(context.getName().substring(10), message);
            } else if(context.getName().startsWith("message-")) {
                fulfilmentMessage = context.getName().substring(8);
            } else if(context.getName().startsWith("score-")) {
                fulfilmentScore = Double.parseDouble(context.getName().substring(6));
            }
        }

        if(fulfilmentIntent != null)
            return new AgentResponse(fulfilmentMessage, fulfilmentIntent, inputContexts, inputParameters, fulfilmentScore);
        else
            inputParameters.clear();

        return null;
    }

    private DocumentCategorizerME findCategorizer(List<Context> inputContexts) throws IOException {
        int identifier = Context.getIdentifyer(inputContexts);
        DocumentCategorizerME categorizer = categorizerMap.get(identifier);

        if(categorizer != null)
            return categorizer;

        List<Intent> intentsToTrain = agent.getIntentManager().findIntents(inputContexts);

        if(intentsToTrain.size() < 1)
            return null;

        // Train Intents
        List<DocumentSample> categoryStreams = new ArrayList<>();
        for (Intent intent : intentsToTrain) {
            categoryStreams.addAll(intent.getDocumentSamples());
        }
        // define the training parameters
        TrainingParameters params = new TrainingParameters();
        params.put(TrainingParameters.ITERATIONS_PARAM, String.valueOf(100));
        params.put(TrainingParameters.CUTOFF_PARAM, String.valueOf(0));

        ObjectStream<DocumentSample> combinedDocumentSampleStream = ObjectStreamUtils.createObjectStream(categoryStreams);
        DoccatModel doccatModel = DocumentCategorizerME.train(agent.getLanguage(), combinedDocumentSampleStream, params, new DoccatFactory());
        combinedDocumentSampleStream.close();

        DocumentCategorizerME categorizerME = new DocumentCategorizerME(doccatModel);

        categorizerMap.put(identifier, categorizerME);

        return categorizerME;
    }
}
