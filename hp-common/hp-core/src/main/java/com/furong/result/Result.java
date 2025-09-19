package com.furong.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Origami
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result<T> implements Serializable {

    //响应的状态码
    private Integer code;

    //提示信息
    private String msg;

    //总记录数
    private Long count;

    //响应数据
    private T data;

    /**
     * 构建带分页的结果
     * @param data
     * @param count
     */
    public static Result buildSuccess(Object data, Long count){
        return Result.builder()
                .code(0)
                .msg("操作成功")
                .data(data)
                .count(count)
                .build();
    }

    /**
     * 构建不带总记录数的成功的返回
     * @param data
     */
    public static Result buildSuccess(Object data){
        return buildSuccess(data,null);
    }

    /**
     * 根据布尔的结果判断返回成功还是失败
     * @param result
     */
    public static Result jude(Boolean result) {
        return result?buildSuccess():buildFail(20000,"操作失败!");
    }


    /**
     * 构建成功消息的提示
     */
    public static Result buildSuccess(){
        return buildSuccess(null);
    }

    /**
     * 构建失败消息
     * @param code
     * @param msg
     */
    public static Result buildFail(Integer code,String msg){
        return Result.builder()
                .code(code)
                .msg(msg)
                .build();
    }

}