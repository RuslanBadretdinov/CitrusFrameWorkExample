package helpers;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.testng.TestNGCitrusTestRunner;

import com.consol.citrus.message.MessageType;
import org.testng.annotations.Test;

public class WssHelperTest extends TestNGCitrusTestRunner {

    private TestContext context;

    @Test(description = "soap", enabled = true, invocationCount = 5)
    @CitrusTest
    public void getTestActions() {
        context = citrus.getCitrusContext().createTestContext();

        send(action -> action
                .endpoint("wssHelper")
                .fork(true)
        );

        receive(action -> action
                .endpoint("wssHelper")
                .messageType(MessageType.JSON)
                .payload(
                        ""
                )
        );
    }
}
