package com.kuark.jfzk.demo.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseTBaseGroupPrivilegeRelation<M extends BaseTBaseGroupPrivilegeRelation<M>> extends Model<M> implements IBean {

    public void setId(java.lang.Long id) {
        set("id", id);
    }

    public java.lang.Long getId() {
        return get("id");
    }

    public void setGroupId(java.lang.Long groupId) {
        set("group_id", groupId);
    }

    public java.lang.Long getGroupId() {
        return get("group_id");
    }

    public void setPrivilegeId(java.lang.Long privilegeId) {
        set("privilege_id", privilegeId);
    }

    public java.lang.Long getPrivilegeId() {
        return get("privilege_id");
    }

    public void setType(java.lang.Integer type) {
        set("type", type);
    }

    public java.lang.Integer getType() {
        return get("type");
    }

}
