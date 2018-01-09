package com.hostinfin.nlu;

/**
 * Created by René Preuß on 1/8/2018.
 */
public class Path {
    public class Session {
        public static final String CREATE = "/agents/:agent/sessions";
        public static final String PARSE = "/agents/:agent/sessions/:session/parse";
    }

    public class Agent {
        public static final String TRAIN = "/agents/:agent/train";
    }
}
