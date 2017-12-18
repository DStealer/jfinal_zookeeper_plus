package com.kuark.jfzk.demo.vo;

public class HelloReqJson {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "HelloReqJson{" +
                "name='" + name + '\'' +
                '}';
    }
}
