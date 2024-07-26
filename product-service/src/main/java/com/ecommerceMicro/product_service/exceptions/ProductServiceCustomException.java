package com.ecommerceMicro.product_service.exceptions;

import lombok.Data;

@Data
public class ProductServiceCustomException extends RuntimeException{

    private String errorCode;

    public ProductServiceCustomException(String message,String errorCode){
        super(message);
        this.errorCode=errorCode;
    }


}
