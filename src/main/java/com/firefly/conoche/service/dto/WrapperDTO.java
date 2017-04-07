package com.firefly.conoche.service.dto;

/**
 * Created by melvin on 4/7/2017.
 */
public class WrapperDTO<T> {
    private T data;

    public WrapperDTO(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
