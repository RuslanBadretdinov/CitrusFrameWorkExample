package helpers;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.testng.TestNGCitrusTestRunner;

import com.consol.citrus.message.MessageType;
import dto.Data;
import dto.Support;
import dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

// TestNGCitrusTestRunner - из старой библиотеки, достаточно использовать TestNGCitrusSpringSupport
public class RestHelperTest extends TestNGCitrusTestRunner {
    private TestContext context;

    @Test(description = "Rest helper", enabled = true)
    @CitrusTest
    public void getTestActions() {
        context = citrus.getCitrusContext().createTestContext();

        http(httpActionBuilder -> httpActionBuilder
                .client("restHelper")
                .send().get("users/${userId}"));

        http(httpActionBuilder -> httpActionBuilder
                .client("restHelper")
                .receive()
                .response(HttpStatus.OK)
                .messageType(MessageType.JSON)
                .payload(getUserDTO(), "objectMapper"));
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
