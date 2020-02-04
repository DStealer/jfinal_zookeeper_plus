package com.kuark.jfzk.demo.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseTBaseUser<M extends BaseTBaseUser<M>> extends Model<M> implements IBean {

    public void setId(java.lang.Long id) {
        set("id", id);
    }

    public java.lang.Long getId() {
        return get("id");
    }

    public void setLoginName(java.lang.String loginName) {
        set("login_name", loginName);
    }

    public java.lang.String getLoginName() {
        return get("login_name");
    }

    public void setPassword(java.lang.String password) {
        set("password", password);
    }

    public java.lang.String getPassword() {
        return get("password");
    }

    public void setUsername(java.lang.String username) {
        set("username", username);
    }

    public java.lang.String getUsername() {
        return get("username");
    }

    public void setMobile(java.lang.String mobile) {
        set("mobile", mobile);
    }

    public java.lang.String getMobile() {
        return get("mobile");
    }

    public void setEmail(java.lang.String email) {
        set("email", email);
    }

    public java.lang.String getEmail() {
        return get("email");
    }

    public void setGenTime(java.util.Date genTime) {
        set("gen_time", genTime);
    }

    public java.util.Date getGenTime() {
        return get("gen_time");
    }

    public void setGenBy(java.lang.Long genBy) {
        set("gen_by", genBy);
    }

    public java.lang.Long getGenBy() {
        return get("gen_by");
    }

    public void setStatus(java.lang.Integer status) {
        set("status", status);
    }

    public java.lang.Integer getStatus() {
        return get("status");
    }

}