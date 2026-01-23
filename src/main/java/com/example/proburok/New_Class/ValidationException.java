package com.example.proburok.New_Class;
//Специальный тип исключения для ошибок валидации. Это позволяет отделить ошибки валидации от других типов ошибок.
public  class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}