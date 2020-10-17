package fieldtracker;

import java.sql.Timestamp;
import java.io.Serializable;

import java.util.logging.Level;
import java.util.logging.Logger;


public class FieldActivityFileType implements Serializable, Cloneable{


    private static final long serialVersionUID = 900190823584362053L;
    private Integer fieldActivityFileTypeId;
    private String fileType;
    private String fileTypeDesc;
    private boolean active;
    private Timestamp createdDate;
    private Timestamp editDate;
    private Integer lastEditUserId;


    public Integer getFieldActivityFileTypeId() {
        return fieldActivityFileTypeId;
    }

    public void setFieldActivityFileTypeId(Integer fieldActivityFileTypeId) {
        this.fieldActivityFileTypeId = fieldActivityFileTypeId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileTypeDesc() {
        return fileTypeDesc;
    }

    public void setFileTypeDesc(String fileTypeDesc) {
        this.fileTypeDesc = fileTypeDesc;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getEditDate() {
        return editDate;
    }

    public void setEditDate(Timestamp editDate) {
        this.editDate = editDate;
    }

    public Integer getLastEditUserId() {
        return lastEditUserId;
    }

    public void setLastEditUserId(Integer lastEditUserId) {
        this.lastEditUserId = lastEditUserId;
    }
}
