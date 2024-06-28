package tests.lesson1;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import dto.Data;
import dto.Support;
import dto.UserDTO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

import static com.consol.citrus.actions.EchoAction.Builder.echo;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;


public class FirstTestGetUser extends TestNGCitrusSpringSupport {

    private TestContext context;

    @Test(description = "Работа с переменными")
    @CitrusTest
    public void test() {
        this.context = citrus.getCitrusContext().createTestContext();

        context.setVariable("variable", "superValue");
        $(echo("Property \"value\" = " + context.getVariable("variable")));
        $(echo("We have userId = " + context.getVariable("userId")));
        $(echo("We have userId = " + "${userId}"));
        variable("now", "citrus:currentDate()");
    }

    @Test(description = "Получение информации о пользователе 1, валидация на строку")
    @CitrusTest
    public void getTestActions1() {
        this.context = citrus.getCitrusContext().createTestContext();

        // Отправка
        run(http()
                .client("restClientReqres")
                .send()
                .get("users/" + context.getVariable("userId")));
        //
        run(http()
                .client("restClientReqres")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .body(
                        "{\n" +
                                "    \"data\": {\n" +
                                "        \"id\": 5,\n" +
                                "        \"email\": \"charles.morris@reqres.in\",\n" +
                                "        \"first_name\": \"Charles\",\n" +
                                "        \"last_name\": \"Morris\",\n" +
                                "        \"avatar\": \"https://reqres.in/img/faces/5-image.jpg\"\n" +
                                "    },\n" +
                                "    \"support\": {\n" +
                                "        \"url\": \"https://reqres.in/#support-heading\",\n" +
                                "        \"text\": \"To keep ReqRes free, contributions towards server costs are appreciated!\"\n" +
                                "    }\n" +
                                "}"
                )
        );
    }

    @Test(description = "Получение информации о пользователе 2, валидация на DTO объект")
    @CitrusTest
    public void getTestActions2() {
        this.context = citrus.getCitrusContext().createTestContext();

        // Отправка
        run(http()
                .client("restClientReqres")
                .send()
                .get("users/" + context.getVariable("userId")));
        //
        run(http()
                .client("restClientReqres")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .body(new ObjectMappingPayloadBuilder(getUserDTO(), "objectMapper"))
        );
    }

    @Test(description = "Получение информации о пользователе 2, валидация JSON файл")
    @CitrusTest
    public void getTestActions3() {
        this.context = citrus.getCitrusContext().createTestContext();

        // Отправка
        run(http()
                .client("restClientReqres")
                .send()
                .get("users/" + context.getVariable("userId")));

        run(http()
                .client("restClientReqres")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .body(new ClassPathResource("json/user5.json"))
        );
    }

    public UserDTO getUserDTO() {
        return UserDTO.builder()
                .data(new Data(
                        "https://reqres.in/img/faces/5-image.jpg",
                        "charles.morris@reqres.in",
                        5L,
                        "Charles",
                        "Morris"))
                .support(new Support(
                        "To keep ReqRes free, contributions towards server costs are appreciated!",
                        "https://reqres.in/#support-heading"))
                .build();
    }
}
