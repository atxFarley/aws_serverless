package fieldtracker;

import java.sql.Timestamp;
import java.io.Serializable;

import java.util.logging.Level;
import java.util.logging.Logger;


public class FieldActivityType implements Serializable, Cloneable{



    private static final long serialVersionUID = -8473850333881938131L;

    private Integer fieldActivityTypeId;
    private String activityType;
    private String activityDesc;
    private boolean active;
    private Timestamp createdDate;
    private Timestamp editDate;
    private Integer lastEditUserId;


    public Integer getFieldActivityTypeId() {
        return fieldActivityTypeId;
    }

    public void setFieldActivityTypeId(Integer fieldActivityTypeId) {
        this.fieldActivityTypeId = fieldActivityTypeId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
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
