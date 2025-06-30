package com.library.restapi.demo.model.helper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RoleId implements Serializable {

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "role", length = 50)
    private String role;

    public RoleId() {}

    public RoleId(Integer userId, String role) {
        this.userId = userId;
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleId)) return false;
        RoleId roleId = (RoleId) o;
        return Objects.equals(userId, roleId.userId) &&
                Objects.equals(role, roleId.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, role);
    }

    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
