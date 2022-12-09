package LiveProject;


import io.restassured.RestAssured;
import io.restassured.authentication.AuthenticationScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;

import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class GithubProject {

    //ssh key
    String sshkey="ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIP0lLOEOM0s9sJh28GIRz5NMIRTcRTtMqGDghIpUDmHs";

    int   sshkeyid;
    //Request specification

    RequestSpecification requestspec = new RequestSpecBuilder().
            setBaseUri("https://api.github.com/user/keys").       //BaseUri
          //  setAuth(oauth("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")).    //Github token: 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
            addHeader("Authorization","token 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").
            addHeader("Content-Type","application/json").
            build();

    //Response Specification
    ResponseSpecification responsespec=new ResponseSpecBuilder().
            expectResponseTime(lessThan(4000L)).
            expectBody("key",equalTo(sshkey)).
            expectBody("title",equalTo("TestAPIKey")).
            build();

    @Test (priority = 0)
    public void postRequestTest()
    {
        //Path:  https://api.github.com/user/keys

        //RequestBody
        Map<String,String> reqBody= new HashMap<>();
        reqBody.put("title","TestAPIKey");
        reqBody.put("key",sshkey);

        //Generate Response
        Response response =given().spec(requestspec).body(reqBody).when().post();

        //Extract the ID
        sshkeyid=response.then().extract().path("id");

        //Assertion
        response.then().statusCode(201).spec(responsespec);

    }

    @Test (priority = 2)

    public void getRequestTest()
    {
        //Path:https://api.github.com/user/keys/{keyId}

        //Generate response and assert
        Response response =
                (Response) given().spec(requestspec).pathParam("keyID",sshkeyid).
                when().get("{keyId} ").
                then().statusCode(200).spec(responsespec);
    }

    @Test (priority = 3)

    public void deleteRequestTest()
    {
        //Path:https://api.github.com/user/keys/{keyId}

        //Generate response and assert

        Response response = (Response) given().spec(requestspec).pathParam("keyID",sshkeyid).
                when().delete("{keyId} ").
                then().statusCode(204).spec(responsespec);
    }

}