package com.ex.qwblog.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *  {
 *      "code" : "400",
 *      "message" : "잘못된 요청입니다.",
 *      "validation" : {
 *          "title" : "제목을 입력해주세요."
 *      }
 *  }
 */
@Getter
//@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {

    private final String code;
    private final String message;
    private final Map<String, String> validation;


    public void addValidation(String fieldName, String errorMessage){
        this.validation.put(fieldName, errorMessage);
    }

    @Builder
    public ErrorResponse(String code, String message, Map<String, String> validation){
        this.code = code;
        this.message = message;
        this.validation = validation;
    }

    @RequiredArgsConstructor
    private class ValidationTuple{
        private final String fieldName;
        private final String errorMessage;
    }
}
