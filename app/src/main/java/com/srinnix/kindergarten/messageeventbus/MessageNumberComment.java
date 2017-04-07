package com.srinnix.kindergarten.messageeventbus;

/**
 * Created by anhtu on 4/7/2017.
 */

public class MessageNumberComment {
    public final String idPost;
    public final int numberComment;

    public MessageNumberComment(String idPost, int numberComment) {
        this.idPost = idPost;
        this.numberComment = numberComment;
    }
}
