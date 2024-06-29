package helpers;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.testng.TestNGCitrusTestRunner;
import com.consol.citrus.message.Message;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.dataaccess.webservicesserver.NumberToDollars;
import com.dataaccess.webservicesserver.NumberToDollarsResponse;
import features.CustomMarshaller;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static com.consol.citrus.ws.actions.SoapActionBuilder.soap;

public class SoapHelperTest  extends TestNGCitrusTestRunner {

    private TestContext context;

    @Test(description = "SOAPHelper", enabled = true)
    @CitrusTest
    public void test() {
        this.context = citrus.getCitrusContext().createTestContext();

        CustomMarshaller<Class<NumberToDollars>> ptxRq = new CustomMarshaller<>();
        CustomMarshaller<Class<NumberToDollarsResponse>> ptxRs = new CustomMarshaller<>();

        soap(soapActionBuilder -> soapActionBuilder
                .client("soapHelper")
                .send()
                .payload(ptxRq.convert(
                        NumberToDollars.class,
                        getNumberToDollarsRq(),
                        "http://www.dataaccess.com/webservicesserver/",
                        "NumberToDollars")));

        soap(soapActionBuilder -> soapActionBuilder
                .client("soapHelper")
                .receive()
                .payload(ptxRs.convert(
                        NumberToDollarsResponse.class,
                        getNumberToDollarsRs(),
                        "http://www.dataaccess.com/webservicesserver/",
                        "NumberToDollarsResponse"
                )));
    }

    public NumberToDollars getNumberToDollarsRq() {
        NumberToDollars numberToDollars = new NumberToDollars();
        numberToDollars.setDNum(new BigDecimal("15"));
        return numberToDollars;
    }

    public NumberToDollarsResponse getNumberToDollarsRs() {
        NumberToDollarsResponse numberToDollarsResponse = new NumberToDollarsResponse();
        numberToDollarsResponse.setNumberToDollarsResult("fifteen dollars");
        return numberToDollarsResponse;
    }
}