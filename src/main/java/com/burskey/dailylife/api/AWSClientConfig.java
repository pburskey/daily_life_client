package com.burskey.dailylife.api;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.apigateway.model.GetRestApisResult;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Region;

public class AWSClientConfig {
    public AWSClientConfig(String accessKey, String secretAccessKey, String bucketName, String region) {
        this.accessKey = accessKey;
        this.secretAccessKey = secretAccessKey;
        this.bucketName = bucketName;
        this.region = Region.fromValue(region);
    }

    private String accessKey;

    private String secretAccessKey;

    private String bucketName;

    private Region region = Region.US_East_2;


    public AmazonS3 amazonS3() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretAccessKey);

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(region.toString())
                .build();
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretAccessKey() {
        return secretAccessKey;
    }

    public String getBucketName() {
        return bucketName;
    }

    public Region getRegion() {
        return region;
    }


    public GetRestApisResult restAPIGateway() {

        return RestHttpApiGateWayHelper.With(this);
    }


}
