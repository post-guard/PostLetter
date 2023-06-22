package top.rrricardo.postletter.models;


import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * 会话类
 */
public class Session implements Serializable {
    private int id;
    /**
     * 会话名称
     */
    @NotNull
    private String name;
    /**
     * 会话详情
     */
    @NotNull
    private String details;
    /**
     * 会话等级
     * 也就是会话中可以容纳的人数
     */
    private int level;

    public Session(@NotNull String name, @NotNull String details, int level) {
        this.name = name;
        this.details = details;
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    public String getDetails() {
        return details;
    }

    public void setDetails(@NotNull String details) {
        this.details = details;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
