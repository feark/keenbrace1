package com.keenbrace.gson;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.keenbrace.core.utils.WLoger;


import java.lang.reflect.Type;

/**
 * Created by ken on 16/1/20.
 */
public class ResultDeserializer<T> implements JsonDeserializer<T> {

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        WLoger.debug("json:"+json.toString());

        return new Gson().fromJson(json.toString(), typeOfT);
    }
}
