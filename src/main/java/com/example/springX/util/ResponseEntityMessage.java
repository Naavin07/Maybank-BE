package com.example.springX.util;

import lombok.Data;

@Data
public class ResponseEntityMessage {
    private String message;

    public ResponseEntityMessage(String message){
        this.message= message;
    }
}
