package LiveProject;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.dsl.PactDslWithState;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@ExtendWith(PactConsumerTestExt.class)                       //addding extension to class test will run as junit
public class ConsumerTest
{
    //headers
    Map<String, String> headers = new HashMap<>();

    //API resource path
    String resourcepath = "/api/users";

    @Pact(provider = "UserProvider", consumer = "UserConsumer")
    public RequestResponsePact createPact(PactDslWithProvider builder)
    {
        //Headers
        headers.put("Content-Type", "application/json");

        //request and response body  (bothe are same we r craeting single variable as requestresponsebody
        DslPart requestresponsebody = new PactDslJsonBody().
                numberType("id", 1).
                stringType("firstName", "Indra").
                stringType("lastname", "Jadhav").
                stringType("email", "abc@gmail.com");

        return builder.given("A request to create user").
                uponReceiving("A request to create user").
                method("POST").
                path(resourcepath).
                headers(headers).
                body(requestresponsebody).
                willRespondWith().
                status(201).
                body(requestresponsebody).
                toPact();
    }

    @Test
    //junit test  import org.junit.jupiter.api.Test;  (pact dies not support Testng

    @PactTestFor(providerName = "UserProvider", port = "8282")
    public void consumerTest()
    {
        //Base URI

        String baseURI = "https://localhost:8282";

        //request body
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("id", 1);
        reqBody.put("firstname", "Indra");
        reqBody.put("lastname", "Jadhav");
        reqBody.put("email", "abc@gmail.com");

        //Generate response

        given().headers(headers).body(reqBody).log().all().
                when().post(baseURI + resourcepath).
                then().statusCode(201).log().all();

    }
}
