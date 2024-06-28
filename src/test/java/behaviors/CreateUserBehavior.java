package behaviors;

import com.consol.citrus.TestActionRunner;
import com.consol.citrus.TestBehavior;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.message.MessageType;
import org.springframework.http.HttpStatus;


import static com.consol.citrus.actions.EchoAction.Builder.echo;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

public class CreateUserBehavior implements TestBehavior {

    private TestContext context;
    public String name;
    public String job;

    public CreateUserBehavior(TestContext context, String name, String job) {
        this.context = context;
        this.name = name;
        this.job = job;
    }

    @Override
    public void apply(TestActionRunner testActionRunner) {
        String rqBody = "\n{\n" +
                "    \"name\": \"" + name + "\",\n" +
                "    \"job\": \"" + job + "\"\n" +
                "}";

        testActionRunner.run(echo(rqBody));

        // Отправка
        testActionRunner.run(http()
                .client("restClientReqres")
                .send()
                .post("users")
                .message()
                .type(MessageType.JSON)
                .body(
                        rqBody
                ));

        testActionRunner.run(http()
                .client("restClientReqres")
                .receive()
                .response(HttpStatus.CREATED)
                .message()
                .type(MessageType.JSON)
                .extract(fromBody()
                        .expression("$.id", "currentId")
                        .expression("$.createdAt", "currentCreatedAt"))
        );

        testActionRunner.run(echo("\n currentId = ${currentId} and currentCreatedAt = ${currentCreatedAt}"));
    }
}
