package tests;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import dto.CreateUserRsDTO;
import dto.Data;
import dto.Support;
import dto.UserDTO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

import static com.consol.citrus.actions.EchoAction.Builder.echo;
import static com.consol.citrus.dsl.JsonPathSupport.jsonPath;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;
import static com.consol.citrus.validation.json.JsonMessageValidationContext.Builder.json;

public class FirstTestCreateUser extends TestNGCitrusSpringSupport {

    private TestContext context;

    private String name = "Nig";
    private String job = "Ga Ngsta";

    @Test(description = "Создание пользователя")
    @CitrusTest
    public void getTestActions() {
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
        //
        run(http()
                .client("restClientReqres")
                .receive()
                .response(HttpStatus.CREATED)
                .message()
                .type(MessageType.JSON)
                .body(new ObjectMappingPayloadBuilder(getCreateUserDTO(),"objectMapper"))
        );
    }

    @Test(description = "Создание пользователя + validate в rs")
    @CitrusTest
    public void getTestActions2() {
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
                .body(new ObjectMappingPayloadBuilder(getCreateUserDTO2(),"objectMapper"))
                .validate(json()
                        .ignore("$.createdAt"))
                .validate(jsonPath()
                        .expression("$.name", name)
                        .expression("$.job", job))
                .extract(fromBody()
                        .expression("$.id", "currentId")
                        .expression("$.createdAt", "currentCreatedAt"))
        );

        run(echo("\n currentId = ${currentId} and currentCreatedAt = ${currentCreatedAt}"));
    }

    public CreateUserRsDTO getCreateUserDTO() {
        return CreateUserRsDTO.builder()
                .name(name)
                .job(job)
                .id("@isNumber()@")
                .createdAt("@ignore()@")
                .build();
    }

    public CreateUserRsDTO getCreateUserDTO2() {
        return CreateUserRsDTO.builder()
                .name(name)
                .job(job)
                .id("@isNumber()@")
                .createdAt("unknown")
                .build();
    }
}
