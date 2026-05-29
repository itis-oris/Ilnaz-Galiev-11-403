package com.itis.oris.skilltrade.dto;

import com.itis.oris.skilltrade.entity.User;

public record UserDto(
        Long id,
        String username,
        String email,
        String bio,
        String city,
        String role,
        boolean blocked
) {
    public static UserDto from(User u) {
        if (u == null) return null;
        return new UserDto(
                u.getId(), u.getUsername(), u.getEmail(),
                u.getBio(), u.getCity(),
                u.getRole().name(), u.isBlocked()
        );
    }
}
