package rest;

import com.sun.jmx.snmp.Timestamp;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import utility.ConfigReader;
import java.util.ResourceBundle;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class RequestHandler {

    public static ConfigReader config;

    protected RequestSpecification getRequestSpecification() {
        return new RequestSpecBuilder().
                setBaseUri("https://graph.facebook.com").
                addHeader("Content-Type", "application/json").
                addFilter(new RequestLoggingFilter()). //TODO ONLY FOR DEBUGGING PURPOSES
                build();
    }

    public RequestUserAccessToken getUserAccessToken() {
        return given(getRequestSpecification()).
                param(config.AppToken()).
                get("/" + config.AppID()).
                then().log().body().
                statusCode(HttpStatus.SC_OK).extract().as(RequestUserAccessToken.class);
    }

    public RequestPageAccessToken getPageAccessToken(Map<String, String> parameters) {
        return given(getRequestSpecification()).
                params(parameters).
                get("/" + config.UserID()).
                then().log().body().
                statusCode(HttpStatus.SC_OK).extract().as(RequestPageAccessToken.class);
    }

    public CreatePostResponse postMessage(Map<String, String> parameters) {
        return given(getRequestSpecification()).
                params(parameters).
                post("/" + config.PageID() + "/feed").
                then().log().body().
                statusCode(HttpStatus.SC_OK).extract().as(CreatePostResponse.class);
    }

    public UpdatePostResponse updateMessage(Map<String, String> parameters, String messageId) {
        return given(getRequestSpecification()).
                params(parameters).
                post("/" + messageId).
                then().log().body().
                statusCode(HttpStatus.SC_OK).extract().as(UpdatePostResponse.class);
    }

    public UpdatePostResponse deleteMessage(Map<String, String> parameters, String messageId) {
        return given(getRequestSpecification()).
                params(parameters).
                delete("/" + messageId).
                then().log().body().
                statusCode(HttpStatus.SC_OK).extract().as(UpdatePostResponse.class);
    }
}
