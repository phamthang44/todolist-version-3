package com.greenwich.todo.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserStatus {
    @JsonProperty("banned")
    BANNED,
    @JsonProperty("active")
    ACTIVE
}
