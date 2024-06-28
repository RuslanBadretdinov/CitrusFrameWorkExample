package tests.lesson2;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.JsonPathSupport.jsonPath;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class ParametrizedTestDataProviderGetUser extends TestNGCitrusSpringSupport {

    private TestContext context;

    @DataProvider(name = "dataProvider")
    public Object[][] getProvider() {
        return new Object[][] {
                new Object[] {"1", "George", "Bluth"},
                new Object[] {"2", "Janet", "Weaver"},
                new Object[] {"3", "Emma", "Wong"}
        };
    }

    @Test(description = "Получение информации о пользователе", dataProvider = "dataProvider")
    @CitrusTest
    public void getTestActions(String id, String firstName, String lastName) {
        this.context = citrus.getCitrusContext().createTestContext();

        // Отправка
        run(http()
                .client("restClientReqres")
                .send()
                .get("users/" + id));
        //
        run(http()
                .client("restClientReqres")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .validate(jsonPath()
                        .expression("$.data.id", id)
                        .expression("$.data.first_name", firstName)
                        .expression("$.data.last_name", lastName))
        );
    }
}
