package tests;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.testng.annotations.Test;

import static com.consol.citrus.actions.EchoAction.Builder.echo;


public class FirstTestGetUser extends TestNGCitrusSpringSupport {

    private TestContext context;

    @Test(description = "Получение информации о пользователе")
    @CitrusTest
    public void getTestActions() {
        this.context = citrus.getCitrusContext().createTestContext();

        context.setVariable("variable", "superValue");
        $(echo("Property \"value\" = " + context.getVariable("variable")));
        $(echo("We have userId = " + context.getVariable("userId")));
        $(echo("We have userId = " + "${userId}"));
        variable("now", "citrus:currentDate()");
    }
}
