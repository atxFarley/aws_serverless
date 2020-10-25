package fieldtracker;

import java.io.Serializable;

public class FileUpload implements Serializable, Cloneable  {

    private static final long serialVersionUID = 1110006039433711156L;

    private String fileKey;
    private String fileContentType;
    private String  presignedUrl;

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public String getPresignedUrl() {
        return presignedUrl;
    }

    public void setPresignedUrl(String presignedUrl) {
        this.presignedUrl = presignedUrl;
    }
}
