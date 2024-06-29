package helpers;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.testng.TestNGCitrusTestRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import javax.sql.DataSource;

public class SQLHelperTest extends TestNGCitrusTestRunner {

    @Autowired
    @Qualifier("sqlHelper")
    private DataSource dataSource;

    private TestContext context;

    // Очереди сообщений - пример для IBS websphere
    @Test(description = "SQLHelper", enabled = true)
    @CitrusTest
    public void getTestActions() {
        context = citrus.getCitrusContext().createTestContext();

//        sql(action -> action
//                .dataSource(dataSource)
//                .statement("CREATE TABLE drivers()"));

        query(action -> action
                .dataSource(dataSource)
                .statement("select role_name from roles where role_id = 36")
                .extract("role_name", "current_role")
        );

        echo("${current_role}");
    }
}
