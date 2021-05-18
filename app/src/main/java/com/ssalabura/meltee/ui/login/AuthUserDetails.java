package com.ssalabura.meltee.ui.login;

import io.realm.mongodb.User;

/**
 * Class exposing authenticated user details to the UI.
 */
class AuthUserDetails {
    private User user;
    private String displayName;
    //... other data fields that may be accessible to the UI

    AuthUserDetails(User user, String displayName) {
        this.user = user;
        this.displayName = displayName;
    }

    User getUser() {
        return user;
    }

    String getDisplayName() {
        return displayName;
    }
}