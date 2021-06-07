package org.opennlu.json;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;

/**
 * Created by René Preuß on 6/15/2017.
 */
public class JsonConfig extends ConfigSection {

    private JsonConfig(JsonObject jsonObject) {
        super(jsonObject);
    }

    public static JsonConfig loadConfiguration(File jsonFile) throws IOException {
        if (jsonFile.exists()) {
            try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(jsonFile))) {
                return new JsonConfig(new JsonParser().parse(inputStreamReader).getAsJsonObject());
            }
        } else {
            return new JsonConfig(new JsonObject());
        }
    }

    public static JsonConfig fromString(String jsonString) throws FileNotFoundException {
        return new JsonConfig(new JsonParser().parse(jsonString).getAsJsonObject());
    }

    public void save(File jsonFile) throws IOException {
        if (!jsonFile.exists())
            if (!jsonFile.createNewFile())
                throw new IOException("Cannot create file.");
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(jsonFile))) {
            outputStreamWriter.write(toJson());
        }
    }
}
