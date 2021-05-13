package com.ssalabura.meltee.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class AuthUserDetails {
    private String displayName;
    //... other data fields that may be accessible to the UI

    AuthUserDetails(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}