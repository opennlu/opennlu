package org.opennlu.agent.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import opennlp.tools.namefind.*;
import opennlp.tools.util.*;
import opennlp.tools.util.featuregen.AdaptiveFeatureGenerator;
import org.opennlu.util.StringsInputStream;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by René Preuß on 6/8/2017.
 */
public class DynamicEntity extends Entity {
    public DynamicEntity(String name) {
        super(name, EntityType.DYNAMIC, new String[0]);
    }

    @Override
    public void train(String language) throws Exception {
        for(String line : getSamples()) {
            System.out.println(String.format("Apply training for '%s'...", line));
        }

        // reading training data
        InputStreamFactory modelIn = new StringsInputStream<>(getSamples());

        ObjectStream<NameSample> sampleStream = null;
        try {
            sampleStream = new NameSampleDataStream(
                    new PlainTextByLineStream(modelIn, StandardCharsets.UTF_8));
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        // setting the parameters for training
        TrainingParameters params = new TrainingParameters();
        params.put(TrainingParameters.ITERATIONS_PARAM, String.valueOf(70));
        params.put(TrainingParameters.CUTOFF_PARAM, String.valueOf(1));

        // training the model using TokenNameFinderModel class
        TokenNameFinderModel nameFinderModel = NameFinderME.train("en", null, sampleStream,
                params, TokenNameFinderFactory.create(null, null, Collections.emptyMap(), new BioCodec()));

        setTokenNameFinder(new NameFinderME(nameFinderModel));
    }

    @Override
    public JsonObject toJson() {
        JsonObject jsonEntityObject = new JsonObject();
        jsonEntityObject.addProperty("name", getName());
        jsonEntityObject.addProperty("type", getType().name());
        JsonArray jsonUserSayArray = new JsonArray();
        for(String sample : getSamples()) {
            jsonUserSayArray.add(new JsonPrimitive(sample));
        }
        jsonEntityObject.add("user_says", jsonUserSayArray);
        return jsonEntityObject;
    }
}
