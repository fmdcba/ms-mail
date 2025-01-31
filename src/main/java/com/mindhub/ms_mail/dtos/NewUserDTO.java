package com.mindhub.ms_mail.dtos;

import com.mindhub.ms_mail.utils.RoleType;

public record NewUserDTO(Long id, String username, String email, String password, RoleType roles) {
}
