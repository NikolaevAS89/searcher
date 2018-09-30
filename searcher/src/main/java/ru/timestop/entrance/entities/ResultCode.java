package ru.timestop.entrance.entities;

/**
 * @author t.i.m.e.s.t.o.p
 * @version 1.0.0
 * @since 30.09.2018
 */
public enum ResultCode {
    OK("00.Result.OK"), NOT_FOUND("01.Result.NotFound"), ERROR("02.Result.Error");

    private String code;

    ResultCode(String code) {
        this.code = code;
    }
}
