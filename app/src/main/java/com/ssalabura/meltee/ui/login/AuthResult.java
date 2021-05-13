package com.ssalabura.meltee.ui.login;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
class AuthResult {
    @Nullable
    private AuthUserDetails success;
    @Nullable
    private Integer error;

    AuthResult(@Nullable Integer error) {
        this.error = error;
    }

    AuthResult(@Nullable AuthUserDetails success) {
        this.success = success;
    }

    @Nullable
    AuthUserDetails getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}