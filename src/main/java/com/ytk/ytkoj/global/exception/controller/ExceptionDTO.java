package com.ytk.ytkoj.global.exception.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionDTO {
    public String message;

    @Override
    public String toString() {
        return "Message: " + message;
    }
}