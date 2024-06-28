package tests.lesson2;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import dto.Data;
import dto.Support;
import dto.UserDTO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class TestMock extends TestNGCitrusSpringSupport {
    private TestContext context;

    @Test(description = "Mock")
    @CitrusTest
    public void mock() {
        context = citrus.getCitrusContext().createTestContext();

        // Отправка
        // fork(true) - это включение нескольких потоков - синхронное, асинхронное взаимодействие. Запуск запроса в новом Thread
        run(http()
                .client("mockClientReqres")
                .send()
                .get("users/" + context.getVariable("userId"))
                .fork(true));

        // Заглушка
        // Получение запроса сервером (заглушкой)
        // - для сервера важно знать весь путь к ресурсу с учётом всех деталей "Пути" к ресурсу
        run(http().server("mockServerReqres")
                .receive()
                .get("/api/users/" + context.getVariable("userId")));

        run(http()
                .server("mockServerReqres")
                .send()
                .response()
                .message()
                .contentType("application/json")
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
                ));

        // Отправка запроса сервером (заглушкой)
        run(http()
                .client("mockClientReqres")
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
