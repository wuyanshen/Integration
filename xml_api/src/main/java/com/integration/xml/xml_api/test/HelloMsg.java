package com.integration.xml.xml_api.test;

public class HelloMsg {
    private String msg;
    private String currentTime;

    public HelloMsg(String msg, String currentTime) {
        this.msg = msg;
        this.currentTime = currentTime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }
}
