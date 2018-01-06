package org.opennlu.web.filter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.ResponseTransformer;

/**
 * Created by René Preuß on 6/14/2017.
 */
public class JsonTransformer implements ResponseTransformer {
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }
}
