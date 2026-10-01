package com.helpdeskpro.user;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Table(name = "users")
public class User {
    @Id
    private String id;
    
    private String name;
    
    @Enumerated(EnumType.STRING)
    private Role role;

    protected User() {}

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
