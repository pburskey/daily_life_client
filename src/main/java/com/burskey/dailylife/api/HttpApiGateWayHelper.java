package com.burskey.dailylife.api;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.apigatewayv2.AmazonApiGatewayV2Client;
import com.amazonaws.services.apigatewayv2.AmazonApiGatewayV2ClientBuilder;
import com.amazonaws.services.apigatewayv2.model.GetApisRequest;
import com.amazonaws.services.apigatewayv2.model.GetApisResult;

public class HttpApiGateWayHelper {


    public static GetApisResult With(AWSClientConfig config){

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(config.getAccessKey(), config.getSecretAccessKey());

        AmazonApiGatewayV2Client client = (AmazonApiGatewayV2Client) AmazonApiGatewayV2ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(config.getRegion().toString())
                .build();

        // Get a list of all APIs
        GetApisRequest request = new GetApisRequest();

        GetApisResult response = client.getApis(request);


        return response;
    }

}
