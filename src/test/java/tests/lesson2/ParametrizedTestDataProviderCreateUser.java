package tests.lesson2;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.consol.citrus.actions.EchoAction.Builder.echo;
import static com.consol.citrus.dsl.JsonPathSupport.jsonPath;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;
import static com.consol.citrus.validation.json.JsonMessageValidationContext.Builder.json;

public class ParametrizedTestDataProviderCreateUser extends TestNGCitrusSpringSupport {

    private TestContext context;

    @DataProvider(name = "dataProvider")
    public Object[][] getProvider() {
        return new Object[][] {
                new Object[] {"George", "Driver"},
                new Object[] {"Nig", "Gangster"},
                new Object[] {"Greg", "Fixer"}
        };
    }

    @Test(description = "Создание пользователя", dataProvider = "dataProvider")
    @CitrusTest
    public void getTestActions(String name, String job) {
        this.context = citrus.getCitrusContext().createTestContext();

        this.context = citrus.getCitrusContext().createTestContext();

        String rqBody = "\n{\n" +
                "    \"name\": \"" + name + "\",\n" +
                "    \"job\": \"" + job + "\"\n" +
                "}";

        $(echo(rqBody));

        // Отправка
        run(http()
                .client("restClientReqres")
                .send()
                .post("users")
                .message()
                .type(MessageType.JSON)
                .body(
                        rqBody
                ));

        run(http()
                .client("restClientReqres")
                .receive()
                .response(HttpStatus.CREATED)
                .message()
                .type(MessageType.JSON)
                .validate(json()
                        .ignore("$.createdAt"))
                .validate(jsonPath()
                        .expression("$.name", name)
                        .expression("$.job", job))
                .extract(fromBody()
                        .expression("$.id", "currentId")
                        .expression("$.createdAt", "currentCreatedAt"))
        );
    }
}
