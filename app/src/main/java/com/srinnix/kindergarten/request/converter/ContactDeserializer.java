package com.srinnix.kindergarten.request.converter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.srinnix.kindergarten.model.Contact;
import com.srinnix.kindergarten.model.ContactParent;
import com.srinnix.kindergarten.model.ContactTeacher;

import java.lang.reflect.Type;

/**
 * Created by anhtu on 2/25/2017.
 */

public class ContactDeserializer implements JsonDeserializer<Contact> {

    @Override
    public Contact deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        if (!obj.has("class_name")) {
            return getContactParents(obj);
        } else {
            return getContactTeacher(obj);
        }
    }

    private Contact getContactTeacher(JsonObject obj) {
        String id = obj.get("_id").getAsString();
        String name = obj.get("name").getAsString();
        String image = obj.get("image").getAsString();
        String className = obj.get("class_name").getAsString();
        return new ContactTeacher(id, name, image, className);
    }

    private Contact getContactParents(JsonObject obj) {
        String id = obj.get("_id").getAsString();
        String name = obj.get("name").getAsString();
        return new ContactParent(id, name);
    }
}
