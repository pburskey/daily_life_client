package com.burskey.dailylife.api;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.apigateway.AmazonApiGatewayClient;
import com.amazonaws.services.apigateway.AmazonApiGatewayClientBuilder;
import com.amazonaws.services.apigateway.model.GetRestApisRequest;
import com.amazonaws.services.apigateway.model.GetRestApisResult;


public class RestHttpApiGateWayHelper {

    public static GetRestApisResult With(AWSClientConfig config){

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(config.getAccessKey(), config.getSecretAccessKey());

        AmazonApiGatewayClient client = (AmazonApiGatewayClient) AmazonApiGatewayClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(config.getRegion().toString())
                .build();

        // Get a list of all APIs
        GetRestApisRequest request = new GetRestApisRequest();

        GetRestApisResult response = client.getRestApis(request);
        return response;
    }

}
