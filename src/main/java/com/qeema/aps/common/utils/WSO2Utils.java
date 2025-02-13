package com.qeema.aps.common.utils;

import lombok.Data;

public class WSO2Utils {

    @Data
    public class AuthResponse {
        String access_token;
        String refresh_token;
        String token_type;
        String scope;
        Integer expiresIn;
    }

}
