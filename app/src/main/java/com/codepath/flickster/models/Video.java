package com.codepath.flickster.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vidhya on 9/16/17.
 */

public class Video {

    String key;
    String site;
    String type;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Video(JSONObject jsonObject) throws JSONException {
        this.key = jsonObject.getString("key");
        this.site = jsonObject.getString("site");
    }
}
