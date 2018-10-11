package ru.timestop.entrance.entities;

import com.google.gson.annotations.SerializedName;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author t.i.m.e.s.t.o.p
 * @version 1.0.0
 * @since 30.09.2018
 */
@Entity
public class ResultEntity {
    private static final String DELIMITER = ";";

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

    public List<String> getFiles() {
        return Arrays.asList(filenames.split(DELIMITER));
    }

    public static final class Builder {
        private Integer id;
        private ResultCode code;
        private Integer number;
        private List<String> filenames;
        private Throwable error;

        public Builder(Integer number) {
            this.id = null;
            this.code = ResultCode.OK;
            this.number = number;
            this.filenames = new ArrayList<>();
            this.error = null;

        }

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder setCode(ResultCode code) {
            this.code = code;
            return this;
        }

        public Builder addFilename(String filename) {
            this.filenames.add(filename);
            return this;
        }

        public Builder setError(Throwable error) {
            this.error = error;
            return this;
        }

        public ResultEntity build() {
            ResultEntity result = new ResultEntity();
            result.setId(id);
            result.setCode(code.toString());
            result.setNumber(number);
            String files = String.join(DELIMITER, filenames);
            result.setFilenames(files);
            if (error == null) {
                result.setError(null);
            } else {
                result.setError(error.getLocalizedMessage());
            }
            return result;
        }
    }
}
