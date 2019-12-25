package com.loushi.controller.common;

import org.springframework.beans.factory.annotation.Value;

public abstract class AbstractProfiles {

    @Value("${spring.profiles.active}")
    private String active;

    protected String getActive() {
        return active;
    }

}
