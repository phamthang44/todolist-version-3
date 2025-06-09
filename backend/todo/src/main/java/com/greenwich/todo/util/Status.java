package com.greenwich.todo.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Status {
    @JsonProperty("pending")
    PENDING,
    @JsonProperty("in_progress")
    IN_PROGRESS,
    @JsonProperty("completed")
    COMPLETED,
}
