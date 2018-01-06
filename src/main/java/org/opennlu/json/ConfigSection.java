package org.opennlu.json;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by René Preuß on 6/15/2017.
 */
public class ConfigSection {
    private static final Type stringListType = new TypeToken<List<String>>() {}.getType();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final JsonObject jsonObject;

    public ConfigSection() {
        this.jsonObject = new JsonObject();
    }

    public ConfigSection(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public ConfigSection getConfigSection(String memberName) {
        if(has(memberName))
            return new ConfigSection(jsonObject.getAsJsonObject(memberName));
        JsonObject newJsonSection = new JsonObject();
        jsonObject.add(memberName, newJsonSection);
        return new ConfigSection(newJsonSection);
    }

    public List<ConfigSection> getConfigSectionList(String memberName) {
        List<ConfigSection> configurationSections = new ArrayList<>();
        for(JsonElement jsonObjectSection : jsonObject.get(memberName).getAsJsonArray()) {
            configurationSections.add(new ConfigSection(jsonObjectSection.getAsJsonObject()));
        }
        return configurationSections;
    }

    public JsonElement get(String memberName) {
        return jsonObject.get(memberName);
    }

    public boolean has(String memberName) {
        return jsonObject.has(memberName);
    }

    public String getString(String memberName) {
        return getString(memberName, null);
    }

    public String getString(String memberName, String defaultValue) {
        if(has(memberName))
            return get(memberName).getAsString();
        return defaultValue;
    }

    public int getInt(String memberName) {
        return getInt(memberName, 0);
    }

    public int getInt(String memberName, int defaultValue) {
        if(has(memberName))
            return get(memberName).getAsInt();
        return defaultValue;
    }

    public boolean getBoolean(String memberName) {
        return getBoolean(memberName, false);
    }

    public boolean getBoolean(String memberName, boolean defaultValue) {
        if(has(memberName))
            return get(memberName).getAsBoolean();
        return defaultValue;
    }

    public List<String> getList(String memberName) {
        return getList(memberName, new ArrayList<>());
    }

    public List<String> getList(String memberName, List<String> defaultValue) {
        if(has(memberName))
            return gson.fromJson(get(memberName), stringListType);
        return defaultValue;
    }

    public void set(String memberName, String s) {
        jsonObject.addProperty(memberName, s);
    }

    public void set(String memberName, int i) {
        jsonObject.addProperty(memberName, i);
    }

    public void set(String memberName, boolean b) {
        jsonObject.addProperty(memberName, b);
    }

    public void set(String memberName, List<String> strings) {
        jsonObject.add(memberName, gson.toJsonTree(strings, stringListType));
    }

    public void set(String memberName, ConfigSection config) {
        jsonObject.add(memberName, config.getJsonObject());
    }

    public void setConfigSectionList(String memberName, List<ConfigSection> configList) {
        JsonArray jsonArray = new JsonArray();
        for (ConfigSection config : configList) {
            jsonArray.add(config.getJsonObject());
        }
        jsonObject.add(memberName, jsonArray);
    }

    public void setIfNull(String memberName, String s) {
        if(!has(memberName))
            jsonObject.addProperty(memberName, s);
    }

    public void setIfNull(String memberName, int i) {
        if(!has(memberName))
            jsonObject.addProperty(memberName, i);
    }

    public void setIfNull(String memberName, boolean b) {
        if(!has(memberName))
            jsonObject.addProperty(memberName, b);
    }

    public void setIfNull(String memberName, List<String> strings) {
        if(!has(memberName))
            jsonObject.add(memberName, gson.toJsonTree(strings, stringListType));
    }

    public String toJson() {
        return gson.toJson(jsonObject);
    }

    public JsonArray getJsonArray(String memberName) {
        return jsonObject.getAsJsonArray(memberName);
    }

    public JsonObject getJsonObject(String memberName) {
        return jsonObject.getAsJsonObject(memberName);
    }

    JsonObject getJsonObject() {
        return jsonObject;
    }
}
