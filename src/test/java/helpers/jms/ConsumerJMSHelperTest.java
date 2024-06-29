package helpers.jms;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.testng.TestNGCitrusTestRunner;
import com.consol.citrus.message.MessageType;
import org.testng.annotations.Test;

public class ConsumerJMSHelperTest extends TestNGCitrusTestRunner {
    private TestContext context;

    // Очереди сообщений - пример для IBS websphere
    @Test(description = "JMS Consumer helper", enabled = true)
    @CitrusTest
    public void getTestActions() {
        context = citrus.getCitrusContext().createTestContext();

        receive(action -> action
                .endpoint("helperJMSConsumer")
                .messageType(MessageType.XML)
                .xsd("schemaName")
                .schemaValidation(true)
                .payload("payload")
        );
    }
}
