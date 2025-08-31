package com.wasup.mytasks.model;

import java.util.EnumSet;
import java.util.Set;

public enum Role {
    USER,
    ADMIN;

    // Utility to expand a set of assigned roles to include implied roles
    public static Set<Role> expand(Set<Role> assigned) {
        if (assigned == null) return EnumSet.noneOf(Role.class);
        EnumSet<Role> result = EnumSet.noneOf(Role.class);
        for (Role r : assigned) {
            result.addAll(r.implied());
        }
        return result;

    }

    // Returns roles implied by this role, including itself
    public Set<Role> implied() {
        return switch (this) {
            case USER -> EnumSet.of(USER);
            case ADMIN -> EnumSet.of(ADMIN, USER);
        };
    }
}
