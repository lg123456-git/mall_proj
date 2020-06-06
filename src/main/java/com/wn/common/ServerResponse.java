package com.wn.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @BelongsProject: mall_proj
 * @BelongsPackage: com.wn.common
 * @Author: 廖刚
 * @CreateTime: 2020-05-14 10:34
 * @Description:
 */
@Data
public class ServerResponse<T> implements Serializable {

    private Integer status;
    private T data;
    private String msg;

    public ServerResponse(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public ServerResponse() {
    }

    public ServerResponse(Integer status, T data) {
        this.status = status;
        this.data = data;
    }

    public ServerResponse(Integer status) {
        this.status = status;
    }

    public ServerResponse setResultStatus(Integer status){
        return new ServerResponse(status);
    }

    public ServerResponse responseError(Integer status,String msg){
        return new ServerResponse(status,msg);
    }

    public ServerResponse responseSuccess(Integer status,String msg){
        return new ServerResponse(status,msg);
    }

    public ServerResponse<T> setServerRespnse(Integer status,T data){

        return new ServerResponse(status,data);
    }



}
