package tests.lesson2;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.dataaccess.webservicesserver.NumberToDollars;
import com.dataaccess.webservicesserver.NumberToDollarsResponse;
import features.CustomMarshaller;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static com.consol.citrus.ws.actions.SoapActionBuilder.soap;

public class FirstTestSOAP extends TestNGCitrusSpringSupport {

    private TestContext context;

    @Test(description = "SOAP")
    @CitrusTest
    public void getTestActions1() {
        this.context = citrus.getCitrusContext().createTestContext();

        run(soap()
                .client("soapClient")
                .send()
                .message()
                .body(
                        "    <NumberToDollars xmlns=\"http://www.dataaccess.com/webservicesserver/\">\n" +
                                "      <dNum>15</dNum>\n" +
                                "    </NumberToDollars>"
                ));

        run(soap()
                .client("soapClient")
                .receive()
                .message()
                .body(
                        "    <NumberToDollarsResponse xmlns=\"http://www.dataaccess.com/webservicesserver/\">\n" +
                                "      <NumberToDollarsResult>fifteen dollars</NumberToDollarsResult>\n" +
                                "    </NumberToDollarsResponse>"
                ));
    }

    @Test(description = "SOAP part 2")
    @CitrusTest
    public void getTestActions2() {
        this.context = citrus.getCitrusContext().createTestContext();

        CustomMarshaller<Class<NumberToDollars>> ptxRq = new CustomMarshaller<>();
        CustomMarshaller<Class<NumberToDollarsResponse>> ptxRs = new CustomMarshaller<>();

        run(soap()
                .client("soapClient")
                .send()
                .message()
                .body(ptxRq.convert(
                        NumberToDollars.class,
                        getNumberToDollarsRq(),
                        "http://www.dataaccess.com/webservicesserver/",
                        "NumberToDollars"
                )));

        run(soap()
                .client("soapClient")
                .receive()
                .message()
                .body(ptxRs.convert(
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