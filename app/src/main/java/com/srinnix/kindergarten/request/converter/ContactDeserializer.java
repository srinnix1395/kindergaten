package com.srinnix.kindergarten.request.converter;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.model.Contact;
import com.srinnix.kindergarten.model.ContactParent;
import com.srinnix.kindergarten.model.ContactTeacher;

import java.lang.reflect.Type;
import java.util.ArrayList;

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
        String gender = obj.get("gender").getAsString();
        String image = obj.get("image").getAsString();
        String className = obj.get("class_name").getAsString();
        return new ContactTeacher(id, name, gender, image, className);
    }

    private Contact getContactParents(JsonObject obj) {
        String id = obj.get("_id").getAsString();
        String name = obj.get("name").getAsString();
        String gender = obj.get("gender").getAsString();

        Type listType = new TypeToken<ArrayList<Child>>() {
        }.getType();
        ArrayList<Child> children = new Gson().fromJson(obj.getAsJsonArray("children"), listType);
        return new ContactParent(id, name, gender, children);
    }
}
