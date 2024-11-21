package com.burskey.dailylife.api;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;

import java.io.ByteArrayInputStream;

public class UploadToS3 {

    private final AmazonS3 amazonS3;
    private final AWSClientConfig awsClientConfig;

    private UploadToS3(AmazonS3 amazonS3, AWSClientConfig awsClientConfig) {
        this.amazonS3 = amazonS3;
        this.awsClientConfig = awsClientConfig;
    }

    public static UploadToS3 With(String accessKey, String secretAccessKey, String bucketName, String region){
        AWSClientConfig config = new AWSClientConfig(accessKey, secretAccessKey, bucketName, region);
        UploadToS3 upload = new UploadToS3(config.amazonS3(), config);
        return upload;
    }

    public PutObjectResult upload(String json, String name) throws Exception{

        byte[] data = json.getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(data);


        ObjectMetadata omd = new ObjectMetadata();
        omd.setContentLength(data.length);

        PutObjectResult result = this.amazonS3.putObject(this.awsClientConfig.getBucketName(), name, bais, omd);

        return result;
    }

}
