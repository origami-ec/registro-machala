/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.models;

import java.util.Date;

/**
 *
 * @author Ricardo
 */
public class UserTask {

    String nameTask;
    String user;
    Date createTime;
    Date endTime;

    public UserTask() {
    }

    public UserTask(String nameTask, String user, Date createTime, Date endTime) {
        this.nameTask = nameTask;
        this.user = user;
        this.createTime = createTime;
        this.endTime = endTime;
    }

    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "UserTask{" + "nameTask=" + nameTask + ", user=" + user
                + ", createTime=" + createTime + ", endTime=" + endTime + '}';
    }

}
