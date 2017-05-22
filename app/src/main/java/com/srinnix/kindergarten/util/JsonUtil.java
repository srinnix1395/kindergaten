package com.srinnix.kindergarten.util;

import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.messageeventbus.MessageTyping;
import com.srinnix.kindergarten.model.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by anhtu on 2/24/2017.
 */

public class JsonUtil {
    public static ArrayList<String> parseListContactOnline(Object arg) throws JSONException {
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
            jsonObject.put(ChatConstant._ID_CONVERSATION, message.getConversationId());
            jsonObject.put(ChatConstant.MESSAGE, message.getMessage());
            jsonObject.put(ChatConstant.MESSAGE_TYPE, message.getMessageType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static MessageTyping getMessageTyping(JSONObject jsonObject) throws JSONException {
        String idSender = jsonObject.getString(ChatConstant._ID_SENDER);
        String idReceiver = jsonObject.getString(ChatConstant._ID_RECEIVER);
        boolean isTyping = jsonObject.getBoolean(ChatConstant.IS_TYPING);
        long createdAt = jsonObject.getLong(ChatConstant.CREATED_AT);

        return new MessageTyping(new Message("", idSender, idReceiver, "", createdAt, ChatConstant.PENDING, isTyping));
    }

    public static String getIdUserDisconnect(Object object) {
        JSONObject jsonObject = (JSONObject) object;
        try {
            return jsonObject.getString("_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
