package rest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import utility.ConfigReader;

import static io.restassured.RestAssured.given;

public class RequestHandler {

    private static ConfigReader config;
    private String accessToken;

    private RequestSpecification getRequestSpecification() {
        return new RequestSpecBuilder().
                setBaseUri("https://graph.facebook.com").
                addHeader("Content-Type", "application/json").
                addFilter(new RequestLoggingFilter()). //TODO ONLY FOR DEBUGGING PURPOSES
                build();
    }

    private RequestUserAccessToken getUserAccessToken() {
        return given(getRequestSpecification()).
                param(config.AppToken()).
                get("/" + config.AppID()).
                then().log().body().
                statusCode(HttpStatus.SC_OK).extract().as(RequestUserAccessToken.class);
    }

    private RequestPageAccessToken getPageAccessToken() {

        RequestUserAccessToken requestUserAccessToken = getUserAccessToken();

        for (Datum data : requestUserAccessToken.getData()) {
            accessToken = data.getAccessToken();
//            break;
        }

        return given(getRequestSpecification()).
                param("access_token", accessToken).
                get("/" + config.UserID()).
                then().log().body().
                statusCode(HttpStatus.SC_OK).extract().as(RequestPageAccessToken.class);
    }

    public CreatePostResponse postMessage(String message) {

        RequestPageAccessToken requestPageAccessToken = getPageAccessToken();

        for (Datum data : requestPageAccessToken.getData()) {
            accessToken = data.getAccessToken();
        }

        return given(getRequestSpecification()).
                param("access_token", accessToken).
                param("message", message).
                post("/" + config.PageID() + "/feed").
                then().log().body().
                statusCode(HttpStatus.SC_OK).extract().as(CreatePostResponse.class);
    }

    public UpdatePostResponse updateMessage(String message, String messageId) {

        RequestPageAccessToken requestPageAccessToken = getPageAccessToken();

        for (Datum data : requestPageAccessToken.getData()) {
            accessToken = data.getAccessToken();
        }

        return given(getRequestSpecification()).
                param("access_token", accessToken).
                param("message", message).
                post("/" + messageId).
                then().log().body().
                statusCode(HttpStatus.SC_OK).extract().as(UpdatePostResponse.class);
    }

    public UpdatePostResponse deleteMessage(String messageId) {

        RequestPageAccessToken requestPageAccessToken = getPageAccessToken();

        for (Datum data : requestPageAccessToken.getData()) {
            accessToken = data.getAccessToken();
        }

        return given(getRequestSpecification()).
                param("access_token", accessToken).
                delete("/" + messageId).
                then().log().body().
                statusCode(HttpStatus.SC_OK).extract().as(UpdatePostResponse.class);
    }
}
