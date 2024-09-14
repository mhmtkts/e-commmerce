package com.example.e_commmerce.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ExceptionResponse{
    private String message;
    public int status;
    public LocalDateTime dateTime;
}

