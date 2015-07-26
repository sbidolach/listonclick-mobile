package com.microstep.android.onclick.rest.model;

public class StatusXml {

    protected String message;
    protected int code;
    protected int rowId;
    protected String customCode;

 
    public String getMessage() {
        return message;
    }

    public void setMessage(String value) {
        this.message = value;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int value) {
        this.code = value;
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int value) {
        this.rowId = value;
    }
    
    public String getCustomCode() {
        return customCode;
    }

    public void setCustomCode(String value) {
        this.customCode = value;
    }

}
