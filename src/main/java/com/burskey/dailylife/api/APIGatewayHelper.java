package com.burskey.dailylife.api;

import com.amazonaws.services.apigateway.model.GetRestApisResult;
import com.amazonaws.services.apigateway.model.RestApi;
import com.amazonaws.services.apigatewayv2.model.GetApisResult;

import java.text.MessageFormat;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class APIGatewayHelper {

    AWSClientConfig config = null;
    GetApisResult http = null;
    GetRestApisResult rest = null;


    private APIGatewayHelper() {
    }

    public static APIGatewayHelper With(AWSClientConfig config) {
        APIGatewayHelper helper = new APIGatewayHelper();
        helper.config = config;
        helper.http = HttpApiGateWayHelper.With(config);
        helper.rest = RestHttpApiGateWayHelper.With(config);
        return helper;
    }


    public String findIdForItemNamed(String aName) {
        Predicate<RestApi> sameName = item -> this.rest.getItems().stream().anyMatch(anItem -> anItem.getName().equals(aName));
        List aList = this.rest.getItems().stream().filter(sameName).collect(Collectors.toList());
        if (aList != null && aList.size() > 0) {
            RestApi anAPI = (RestApi) aList.get(0);
            return anAPI.getId();
        }
        return null;
    }


    public String constructBaseURIForEnvironment(String gatewayName, String anEnvironment) {
        String appID = this.findIdForItemNamed("Basic AWS Api Gateway");
        String pattern = "https://{0}.execute-api.{1}.amazonaws.com/{2}/";

        MessageFormat formatter = new MessageFormat(pattern);
        String result = formatter.format(new Object[]{appID, this.config.getRegion(), anEnvironment});
        return result;
    }


}
