package com.library.restapi.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.library.restapi.demo.model.helper.RoleId;
import jakarta.persistence.*;

@Entity
@Table(name = "Role",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role"}))
public class Role {

    @EmbeddedId
    private RoleId id;

    @JsonBackReference
    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    public Role() {}

    public Role(User user, String role) {
        this.user = user;
        this.id = new RoleId(user.getId(), role);
    }

    public RoleId getId() {
        return id;
    }

    public void setId(RoleId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRole() {
        return id.getRole();
    }
}
