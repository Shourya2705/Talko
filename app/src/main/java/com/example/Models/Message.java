package com.example.Models;

public class Message {
    String uid,msg,msgId;
    Long time;

    public Message(String uid, String msg, Long time) {
        this.uid = uid;
        this.msg = msg;
        this.time = time;
    }

    public Message(String uid, String msg) {
        this.uid = uid;
        this.msg = msg;
    }
    public Message(){

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
