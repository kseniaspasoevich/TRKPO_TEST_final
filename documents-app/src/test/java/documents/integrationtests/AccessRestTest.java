package documents.integrationtests;

import documents.app.Application;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccessRestTest {
    @Autowired
    private TestRestTemplate template;

    private String adminLogin = "login";
    private String adminPass = "password";

    @Test
    public void check_access() {
        ResponseEntity<Object> response = template.withBasicAuth(adminLogin, adminPass)
                .getForEntity("/access/1", Object.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

}
