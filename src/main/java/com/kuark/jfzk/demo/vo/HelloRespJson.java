package com.kuark.jfzk.demo.vo;

public class HelloRespJson {
    private String who;
    private String time;

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "HelloRespJson{" +
                "who='" + who + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
