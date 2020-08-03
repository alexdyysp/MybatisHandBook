package com.alexdyysp.model;

import com.alexdyysp.type.Enabled;

import java.util.Date;
import java.util.List;

public class SysRole {
    /**
     * 角色ID
     */
    private Long id;
    /**
     * 角色名
     */
    private String roleName;
    /**
     * 有效标志
     */
    private Enabled enabled;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 用户信息
     */
    private SysUser user;
    /**
     * 角色包含的权限列表
     */
    List<SysPrivilege> privilegeList;

    public SysRole(){
    }

    public SysRole(String roleName, Enabled enabled, String createBy, Date createTime) {
        this.roleName = roleName;
        this.enabled = enabled;
        this.createBy = createBy;
        this.createTime = createTime;
    }

    public SysRole(Long id, String roleName, Enabled enabled, String createBy, Date createTime, SysUser user) {
        this.id = id;
        this.roleName = roleName;
        this.enabled = enabled;
        this.createBy = createBy;
        this.createTime = createTime;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Enabled getEnabled() {
        return enabled;
    }

    public void setEnabled(Enabled enabled) {
        this.enabled = enabled;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public SysUser getUser() {
        return user;
    }

    public void setUser(SysUser user) {
        this.user = user;
    }

    public List<SysPrivilege> getPrivilegeList() {
        return privilegeList;
    }

    public void setPrivilegeList(List<SysPrivilege> privilegeList) {
        this.privilegeList = privilegeList;
    }


    @Override
    public String toString() {
        return "SysRole{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createTime=" + createTime +
                ", user=" + user +
                ", privilegeList=" + privilegeList +
                '}';
    }
}
