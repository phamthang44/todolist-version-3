package com.greenwich.todo.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserStatus {
    @JsonProperty("active")
    ACTIVE,
    @JsonProperty("banned")
    BANNED;

}
