package com.srinnix.kindergarten.util;

import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.model.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by anhtu on 2/24/2017.
 */

public class JsonUtil {
    public static ArrayList<String> parseListContact(Object arg) throws JSONException {
        ArrayList<String> arrayList = new ArrayList<>();

        JSONArray jsonArray = (JSONArray) arg;
        for (int i = 0, size = jsonArray.length(); i < size; i++) {
            arrayList.add(jsonArray.getString(i));
        }
        return arrayList;
    }

    public static JSONObject getJsonMessage(Message message) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ChatConstant._ID, message.getId());
            jsonObject.put(ChatConstant._ID_SENDER, message.getIdSender());
            jsonObject.put(ChatConstant._ID_RECEIVER, message.getIdReceiver());
            jsonObject.put(ChatConstant.MESSAGE, message.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
