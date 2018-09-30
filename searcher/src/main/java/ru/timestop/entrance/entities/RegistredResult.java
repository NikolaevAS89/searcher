package ru.timestop.entrance.entities;

import com.google.gson.annotations.SerializedName;

/**
 * @author t.i.m.e.s.t.o.p
 * @version 1.0.0
 * @since 30.09.2018
 */
public class RegistredResult {
    @SerializedName(value = "id")
    private Integer id;
    @SerializedName(value = "code")
    private String code;
    @SerializedName(value = "number")
    private Integer number;
    @SerializedName(value = "filenames")
    private String filenames;
    @SerializedName(value = "error")
    private String error;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getFilenames() {
        return filenames;
    }

    public void setFilenames(String filenames) {
        this.filenames = filenames;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
