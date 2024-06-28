package tests.lesson2;

import behaviors.CreateUserBehavior;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;

import org.testng.annotations.Test;

import static com.consol.citrus.actions.EchoAction.Builder.echo;

public class TestApplyBehaviour  extends TestNGCitrusSpringSupport {

    private TestContext context;

    @Test(description = "Создание пользователя, тестирование ApplyBehavior")
    @CitrusTest
    public void getTestActions() {
        this.context = citrus.getCitrusContext().createTestContext();

        run(applyBehavior(new CreateUserBehavior(context, "Ann", "Driver")));
        run(echo("\n currentId = ${currentId} and currentCreatedAt = ${currentCreatedAt}"));
    }
}
