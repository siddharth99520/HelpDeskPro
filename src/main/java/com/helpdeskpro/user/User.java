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
    
    private String password;
    
    @Enumerated(EnumType.STRING)
    private Role role;

    protected User() {}

    public User(String id, String name, String password, Role role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public User(String id, String name, Role role) {
        this.id = id;
        this.name = name;
        this.password = "$2a$10$wE.VwVb/8xV1qY4oH1N2P.V8r5p3L6Fz3yM/O5Qz/e9m/v5M2bM9i"; // Default "password" for backwards compatibility
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}
