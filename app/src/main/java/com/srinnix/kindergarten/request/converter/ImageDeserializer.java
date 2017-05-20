package com.srinnix.kindergarten.request.converter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.srinnix.kindergarten.model.Image;

import java.lang.reflect.Type;

/**
 * Created by anhtu on 5/20/2017.
 */

public class ImageDeserializer implements JsonDeserializer<Image> {
    @Override
    public Image deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String caption = null;
        if (jsonObject.has("caption")) {
            caption = jsonObject.get("caption").getAsString();
        }

        long createdAt = 0;
        if (jsonObject.has("created_at")) {
            createdAt = jsonObject.get("created_at").getAsLong();
        }

        String url = jsonObject.get("url").getAsString();

        boolean isVideo = false;
        if (jsonObject.has("is_video")) {
            isVideo = jsonObject.get("is_video").getAsBoolean();
        }

        String thumbnailVideo = null;
        if (isVideo) {
            thumbnailVideo = getUrlThumbnailVideo(url);
        }

        return new Image(caption, createdAt, url, thumbnailVideo, isVideo);
    }

    private String getUrlThumbnailVideo(String url) {
        int upload = url.indexOf("upload");

        StringBuilder builder = new StringBuilder(url);
        builder.insert(upload + 6, "/so_auto");
        builder.replace(builder.length() - 3, builder.length(), "jpg");
        return builder.toString();
    }
}
