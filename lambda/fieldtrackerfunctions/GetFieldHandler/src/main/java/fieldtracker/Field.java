package fieldtracker;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Field implements Serializable, Cloneable {


    private static final long serialVersionUID = -6827579876203291225L;

    private Integer fieldId;
    private String fieldName;
    private String fieldDesc;
    private String growerName;
    private Integer growerId;
    private String ownerName;
    private Integer ownerId;
    private BigDecimal acres;
    private String addressStreet;
    private String addressCity;
    private String addressState;
    private String addressZip;
    private String addressCounty;
    private String addressDesc;
    private ArrayList<FieldAttribute> fieldAttributes;
    private ArrayList<FieldHistory> fieldHistory;
    private ArrayList<FieldActivity> fieldActivities;

    public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getGrowerName() {
        return growerName;
    }

    public void setGrowerName(String growerName) {
        this.growerName = growerName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public BigDecimal getAcres() {
        return acres;
    }

    public void setAcres(BigDecimal acres) {
        this.acres = acres;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressState() {
        return addressState;
    }

    public void setAddressState(String addressState) {
        this.addressState = addressState;
    }

    public String getAddressZip() {
        return addressZip;
    }

    public void setAddressZip(String addressZip) {
        this.addressZip = addressZip;
    }

    public String getAddressCounty() {
        return addressCounty;
    }

    public void setAddressCounty(String addressCounty) {
        this.addressCounty = addressCounty;
    }

    public String getAddressDesc() {
        return addressDesc;
    }

    public void setAddressDesc(String addressDesc) {
        this.addressDesc = addressDesc;
    }

    public ArrayList<FieldAttribute> getFieldAttributes() {
        return fieldAttributes;
    }

    public void setFieldAttributes(ArrayList<FieldAttribute> fieldAttributes) {
        this.fieldAttributes = fieldAttributes;
    }

    public ArrayList<FieldHistory> getFieldHistory() {
        return fieldHistory;
    }

    public void setFieldHistory(ArrayList<FieldHistory> fieldHistory) {
        this.fieldHistory = fieldHistory;
    }

    public ArrayList<FieldActivity> getFieldActivities() {
        return fieldActivities;
    }

    public void setFieldActivities(ArrayList<FieldActivity> fieldActivities) {
        this.fieldActivities = fieldActivities;
    }

    public String getFieldDesc() {
        return fieldDesc;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }

    public Integer getGrowerId() {
        return growerId;
    }

    public void setGrowerId(Integer growerId) {
        this.growerId = growerId;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }


    class FieldAttribute {
        private Integer fieldId;
        private String attributeName;
        private ArrayList<String> attributeValues;


        public String getAttributeName() {
            return attributeName;
        }

        public void setAttributeName(String attributeName) {
            this.attributeName = attributeName;
        }

        public ArrayList<String> getAttributeValues() {
            return attributeValues;
        }

        public void setAttributeValues(ArrayList<String> attributeValues) {
            this.attributeValues = attributeValues;
        }
        public Integer getFieldId() {
            return fieldId;
        }

        public void setFieldId(Integer fieldId) {
            this.fieldId = fieldId;
        }


    }

    class FieldHistory {

        private Integer fieldId;
        private Integer fieldHistoryId;
        private Timestamp fileHistoryDate;
        private String fieldChangeType;
        private String oldValue;
        private String newValue;


        public Integer getFieldId() {
            return fieldId;
        }

        public void setFieldId(Integer fieldId) {
            this.fieldId = fieldId;
        }

        public Integer getFieldHistoryId() {
            return fieldHistoryId;
        }

        public void setFieldHistoryId(Integer fieldHistoryId) {
            this.fieldHistoryId = fieldHistoryId;
        }

        public Timestamp getFileHistoryDate() {
            return fileHistoryDate;
        }

        public void setFileHistoryDate(Timestamp fileHistoryDate) {
            this.fileHistoryDate = fileHistoryDate;
        }

        public String getFieldChangeType() {
            return fieldChangeType;
        }

        public void setFieldChangeType(String fieldChangeType) {
            this.fieldChangeType = fieldChangeType;
        }

        public String getOldValue() {
            return oldValue;
        }

        public void setOldValue(String oldValue) {
            this.oldValue = oldValue;
        }

        public String getNewValue() {
            return newValue;
        }

        public void setNewValue(String newValue) {
            this.newValue = newValue;
        }
    }


    class FieldActivity {
        private int fieldId;
        private int fieldActivityId;
        private String fieldActivityDate;
        private String fieldActivityType;
        private Integer fieldActivityTypeId;
        private String fieldActivityDesc;
        private ArrayList<FieldActivityFile> fieldActivityFiles;

        public int getFieldId() {
            return fieldId;
        }

        public void setFieldId(int fieldId) {
            this.fieldId = fieldId;
        }

        public int getFieldActivityId() {
            return fieldActivityId;
        }

        public void setFieldActivityId(int fieldActivityId) {
            this.fieldActivityId = fieldActivityId;
        }



        public String getFieldActivityType() {
            return fieldActivityType;
        }

        public void setFieldActivityType(String fieldActivityType) {
            this.fieldActivityType = fieldActivityType;
        }

        public String getFieldActivityDesc() {
            return fieldActivityDesc;
        }

        public void setFieldActivityDesc(String fieldActivityDesc) {
            this.fieldActivityDesc = fieldActivityDesc;
        }

        public ArrayList<FieldActivityFile> getFieldActivityFiles() {
            return fieldActivityFiles;
        }

        public void setFieldActivityFiles(ArrayList<FieldActivityFile> fieldActivityFiles) {
            this.fieldActivityFiles = fieldActivityFiles;
        }

        public Integer getFieldActivityTypeId() {
            return fieldActivityTypeId;
        }

        public void setFieldActivityTypeId(Integer fieldActivityTypeId) {
            this.fieldActivityTypeId = fieldActivityTypeId;
        }

        public String getFieldActivityDate() {
            return fieldActivityDate;
        }

        public void setFieldActivityDate(String fieldActivityDate) {
            this.fieldActivityDate = fieldActivityDate;
        }


        class FieldActivityFile {
            private Integer fieldActivityFileId;
            private Integer fieldActivityId;
            private String fieldActivityFilename;
            private Integer fieldActivityFileTypeId;
            private BigDecimal fieldActivityFileSizeMB;
            private String fieldActivityFileLocation;
            private Integer fieldActivityVendorPartnerId;
            private Timestamp fieldActivityFileDate;
            private Boolean fieldActivityFileGeoreferenced;


            public Integer getFieldActivityFileId() {
                return fieldActivityFileId;
            }

            public void setFieldActivityFileId(Integer fieldActivityFileId) {
                this.fieldActivityFileId = fieldActivityFileId;
            }

            public Integer getFieldActivityId() {
                return fieldActivityId;
            }

            public void setFieldActivityId(Integer fieldActivityId) {
                this.fieldActivityId = fieldActivityId;
            }

            public String getFieldActivityFilename() {
                return fieldActivityFilename;
            }

            public void setFieldActivityFilename(String fieldActivityFilename) {
                this.fieldActivityFilename = fieldActivityFilename;
            }

            public Integer getFieldActivityFileTypeId() {
                return fieldActivityFileTypeId;
            }

            public void setFieldActivityFileTypeId(Integer fieldActivityFileTypeId) {
                this.fieldActivityFileTypeId = fieldActivityFileTypeId;
            }

            public BigDecimal getFieldActivityFileSizeMB() {
                return fieldActivityFileSizeMB;
            }

            public void setFieldActivityFileSizeMB(BigDecimal fieldActivityFileSizeMB) {
                this.fieldActivityFileSizeMB = fieldActivityFileSizeMB;
            }

            public String getFieldActivityFileLocation() {
                return fieldActivityFileLocation;
            }

            public void setFieldActivityFileLocation(String fieldActivityFileLocation) {
                this.fieldActivityFileLocation = fieldActivityFileLocation;
            }

            public Integer getFieldActivityVendorPartnerId() {
                return fieldActivityVendorPartnerId;
            }

            public void setFieldActivityVendorPartnerId(Integer fieldActivityVendorPartnerId) {
                this.fieldActivityVendorPartnerId = fieldActivityVendorPartnerId;
            }

            public Timestamp getFieldActivityFileDate() {
                return fieldActivityFileDate;
            }

            public void setFieldActivityFileDate(Timestamp fieldActivityFileDate) {
                this.fieldActivityFileDate = fieldActivityFileDate;
            }

            public Boolean getFieldActivityFileGeoreferenced() {
                return fieldActivityFileGeoreferenced;
            }

            public void setFieldActivityFileGeoreferenced(Boolean fieldActivityFileGeoreferenced) {
                this.fieldActivityFileGeoreferenced = fieldActivityFileGeoreferenced;
            }
        }

    }

}
