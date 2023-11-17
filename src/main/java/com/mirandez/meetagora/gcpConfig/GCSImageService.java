package com.mirandez.meetagora.gcpConfig;


import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class GCSImageService {

    private final Storage storage;

    @Value("${gcp.project-id}")
    private String projectId;

    @Value("${gcp.bucket-name}")
    private String bucketName;
    @Value("${gcp.bucket-classroom}")
    private String bucketClassroom;

    public GCSImageService() {
        this.storage = StorageOptions.getDefaultInstance().getService();
    }

    public byte[] getImage(String objectName) {
        byte[] imageBytes = storage.readAllBytes(bucketName, objectName);
        return imageBytes;
    }

    public byte[] getClassroomImage(String objectName){
        byte[] imageBytes = storage.readAllBytes(bucketClassroom, objectName);
        return  imageBytes;
    }

    public boolean saveImage(String imageName, byte[] imageData) {
        try {
            BlobId blobId = BlobId.of(bucketName, imageName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
            storage.create(blobInfo, imageData);
            return true;
        } catch (Exception e) {
            log.error("[ GCS ] Error : {} ", e.toString() );
            return false;
        }
    }
}