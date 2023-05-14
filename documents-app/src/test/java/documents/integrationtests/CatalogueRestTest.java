package documents.integrationtests;

import documents.app.Application;
import documents.dto.files.catalogues.CatalogueDto;
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
public class CatalogueRestTest {
    @Autowired
    private TestRestTemplate template;

    private String adminLogin = "login";
    private String adminPass = "password";

    @Test
    public void get_1_test() {
        ResponseEntity<Object> response = template.withBasicAuth(adminLogin, adminPass)
                .getForEntity("/catalogue/1", Object.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void get_0_test() {
        ResponseEntity<Object> response = template.withBasicAuth(adminLogin, adminPass)
                .getForEntity("/catalogue/0", Object.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void get_root_test() {
        ResponseEntity<Object> response = template.withBasicAuth(adminLogin, adminPass)
                .getForEntity("/catalogue/root", Object.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void open_root_test() {
        CatalogueDto catalogueDto = template.withBasicAuth(adminLogin, adminPass)
                .getForObject("/catalogue/root", CatalogueDto.class);

        ResponseEntity<Object> response = template.withBasicAuth(adminLogin, adminPass)
                .getForEntity("/catalogue/open/{id}", Object.class, catalogueDto.getId());
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);

        ResponseEntity<Object> response2 = template.withBasicAuth(adminLogin, adminPass)
                .getForEntity("/catalogue/open/{id}?type=DOCUMENT", Object.class, catalogueDto.getId());
        Assert.assertEquals(response2.getStatusCode(), HttpStatus.OK);

        ResponseEntity<Object> response3 = template.withBasicAuth(adminLogin, adminPass)
                .getForEntity("/catalogue/open/{id}?name=doc", Object.class, catalogueDto.getId());
        Assert.assertEquals(response3.getStatusCode(), HttpStatus.OK);

        ResponseEntity<Object> response4 = template.withBasicAuth(adminLogin, adminPass)
                .getForEntity("/catalogue/open/{id}?documentType=fax", Object.class, catalogueDto.getId());
        Assert.assertEquals(response4.getStatusCode(), HttpStatus.OK);
    }


}
