package com.helpdeskpro.domain;

public class User {
    private String id;
    private String name;
    private Role role;

    public User(String id, String name, Role role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }
}
