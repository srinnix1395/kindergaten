package com.srinnix.kindergarten.request.converter;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.srinnix.kindergarten.model.Image;
import com.srinnix.kindergarten.request.model.ImageResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by anhtu on 4/5/2017.
 */

public class ImageDeserializer implements JsonDeserializer<ImageResponse> {

    @Override
    public ImageResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ArrayList<Image> arrayList = new ArrayList<>();

        JsonArray jsonArray = json.getAsJsonArray();
        if (jsonArray.size() == 0) {
            return null;
        }
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
        String caption = jsonObject.get("caption").getAsString();
        long createdAt = jsonObject.get("created_at").getAsLong();

        JsonArray images = jsonObject.getAsJsonArray("images");
        String url;
        for (JsonElement image : images) {
            JsonObject asJsonObject = image.getAsJsonObject();
            url = asJsonObject.get("url").getAsString();
            if (asJsonObject.get("is_video") != null) {
                arrayList.add(new Image(caption, createdAt, url, true));
            } else {
                arrayList.add(new Image(caption, createdAt, url, false));
            }
        }
        return new ImageResponse(arrayList);
    }
}
