package documents.integrationtests;

import documents.app.Application;
import documents.dto.files.catalogues.CatalogueDto;
import documents.dto.files.documents.ConcreteDocumentDto;
import documents.dto.files.documents.DocumentDto;
import documents.dto.files.documents.FilePathDto;
import documents.dto.restdtos.ManageAccessDto;
import documents.dto.user.UserDto;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WholeIntegrationTest {

    @Autowired
    private TestRestTemplate template;

    private String adminLogin = "login";
    private String adminPass = "password";

    private String newUserLogin = "login7";
    private String newUserPass = "pass";

    private String thirdUserLogin = "login8";
    private String thirdUserPassword = "pass";

    private String NEW_TEST_CATALOGUE_IN_ROOT_FOLDER = "new_test_catalogue_in_root_folder";


    @Test
    public void mockkTest() {

    }
    public void check_login_as_admin() {
        UserDto userDto = template.withBasicAuth(adminLogin, adminPass)
                .getForObject("/user/current", UserDto.class);
        Assert.assertEquals(userDto.getRole(), "ADMIN");
        Assert.assertEquals(userDto.getLogin(), adminLogin);
    }

    public void new_user_creation() {
        UserDto userDtoNewUser = UserDto.builder().login(newUserLogin).password(newUserPass).build();
        template.postForObject("/user/register", userDtoNewUser, UserDto.class);
    }

    public void third_user_creation() {
        UserDto userDtoNewUser = UserDto.builder().login(thirdUserLogin).password(thirdUserPassword).build();
        template.postForObject("/user/register", userDtoNewUser, UserDto.class);
    }

    public UserDto check_login_with_new_user() {
        ResponseEntity<UserDto> response = template.withBasicAuth(newUserLogin, newUserPass)
                .getForEntity("/user/current", UserDto.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        return response.getBody();
    }

    public UserDto check_login_with_third_user() {
        ResponseEntity<UserDto> response = template.withBasicAuth(thirdUserLogin, thirdUserPassword)
                .getForEntity("/user/current", UserDto.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        return response.getBody();
    }

    public void grant_admin_access_to_new_user_by_admin_user(UserDto userDtoNewUser) {
        UserDto grantAccessDto = UserDto.builder().id(userDtoNewUser.getId()).role("ADMIN").build();
        UserDto userDto = template.withBasicAuth(adminLogin, adminPass)
                .postForObject("/user/grantaccess", grantAccessDto, UserDto.class);
        Assert.assertEquals(newUserLogin, userDto.getLogin());
        Assert.assertEquals("ADMIN", userDto.getRole());
    }

    public void decline_admin_access_for_new_user_by_admin_user(UserDto userDto) {
        UserDto grantAccessDto = UserDto.builder().id(userDto.getId()).role("USER").build();
        userDto = template.withBasicAuth(adminLogin, adminPass)
                .postForObject("/user/grantaccess", grantAccessDto, UserDto.class);
        Assert.assertEquals(newUserLogin, userDto.getLogin());
        Assert.assertEquals("USER", userDto.getRole());
    }

    public CatalogueDto get_root_catalogue_by_admin_user() {
        CatalogueDto catalogueDto = template.withBasicAuth(adminLogin, adminPass)
                .getForObject("/catalogue/root", CatalogueDto.class);
        Assert.assertNull(catalogueDto.getParentId());
        Assert.assertEquals("root", catalogueDto.getName());
        Assert.assertNotNull(catalogueDto.getId());
        return catalogueDto;
    }

    public CatalogueDto new_catalogue_in_root_folder_creation_by_admin(CatalogueDto rootCatalogue) {
        final String new_test_catalogue_in_root_folder = NEW_TEST_CATALOGUE_IN_ROOT_FOLDER;
        CatalogueDto newCatalogueDto = CatalogueDto.builder()
                .name(new_test_catalogue_in_root_folder)
                .parentId(rootCatalogue.getId())
                .build();

        ResponseEntity<CatalogueDto> catalogue_response = template.withBasicAuth(adminLogin, adminPass)
                .postForEntity("/catalogue", newCatalogueDto, CatalogueDto.class);

        CatalogueDto catalogueDto = catalogue_response.getBody();

        Assert.assertNotNull(catalogueDto);
        Assert.assertEquals(rootCatalogue.getId(), catalogueDto.getParentId());
        Assert.assertEquals(new_test_catalogue_in_root_folder, catalogueDto.getName());
        Assert.assertNotNull(catalogueDto.getId());
        return catalogueDto;
    }

    public void get_catalogue_by_new_user(Long id) {
        ResponseEntity<CatalogueDto> catalogue_response = template.withBasicAuth(newUserLogin, newUserPass)
                .getForEntity("/catalogue/" + id, CatalogueDto.class);
        Assert.assertEquals(catalogue_response.getStatusCode(), HttpStatus.OK);
    }

    public void creation_of_new_folder_by_new_not_permitted_user(CatalogueDto catalogueDto) {
        final String third_catalogue = "third_catalogue";
        CatalogueDto thirdCatalogueDto = CatalogueDto.builder().name(third_catalogue)
                .parentId(catalogueDto.getId()).build();

        ResponseEntity<CatalogueDto> catalogue_response = template.withBasicAuth(newUserLogin, newUserPass)
                .postForEntity("/catalogue", thirdCatalogueDto, CatalogueDto.class);

        Assert.assertEquals(catalogue_response.getStatusCode(), HttpStatus.FORBIDDEN);
    }

    public void giving_readwrite_rights_to_user_in_catalogue(UserDto userDto, CatalogueDto catalogueDto) {
        ManageAccessDto manageAccessDto = ManageAccessDto.builder()
                .access(ManageAccessDto.TypeOfAccess.READWRITE)
                .modify(ManageAccessDto.TypeOfModifying.GRANT)
                .fileId(catalogueDto.getId())
                .userId(userDto.getId()).build();

        ResponseEntity<Object[]> manage_access_response = template.withBasicAuth(adminLogin, adminPass)
                .postForEntity("/access", manageAccessDto, Object[].class);

        Assert.assertEquals(manage_access_response.getStatusCode(), HttpStatus.OK);
    }

    public CatalogueDto creation_of_new_folder_by_new_permitted_user(CatalogueDto catalogueDto) {
        final String third_catalogue = "third_catalogue";
        CatalogueDto thirdCatalogueDto = CatalogueDto.builder().name(third_catalogue)
                .parentId(catalogueDto.getId()).build();

        ResponseEntity<CatalogueDto> catalogue_response = template.withBasicAuth(newUserLogin, newUserPass)
                .postForEntity("/catalogue", thirdCatalogueDto, CatalogueDto.class);

        Assert.assertEquals(catalogue_response.getStatusCode(), HttpStatus.OK);

        return catalogue_response.getBody();
    }

    public DocumentDto new_document_creation_in_new_cat_by_new_user(CatalogueDto catalogueDto){
        String new_doc_name = "new_doc_name";

        List<FilePathDto> list = List.of(
                FilePathDto.builder().path("p/a/t/1").build(),
                FilePathDto.builder().path("p/a/t/2").build(),
                FilePathDto.builder().path("p/a/t/3").build()
        );

        ConcreteDocumentDto concreteDocumentDto = ConcreteDocumentDto.builder()
                .name(new_doc_name)
                .description("descr")
                .data(list)
                .build();

        DocumentDto documentDto = DocumentDto.builder()
                .parentId(catalogueDto.getId())
                .concreteDocument(concreteDocumentDto)
                .documentType("fax")
                .priority("HIGH")
                .build();

        ResponseEntity<DocumentDto> document_response = template.withBasicAuth(newUserLogin, newUserPass)
                .postForEntity("/documents", documentDto, DocumentDto.class);

        Assert.assertEquals(document_response.getStatusCode(), HttpStatus.OK);

        documentDto = document_response.getBody();

        Assert.assertNotNull(documentDto);
        Assert.assertEquals(new_doc_name, documentDto.getName());
        Assert.assertEquals("HIGH", documentDto.getPriority());
        return documentDto;

    }

    public DocumentDto third_document_creation_in_third_cat_by_third_user(CatalogueDto catalogueDto){
        String new_doc_name = "third_doc_name";

        ConcreteDocumentDto concreteDocumentDto = ConcreteDocumentDto.builder()
                .name(new_doc_name)
                .description("descr")
                .build();

        DocumentDto documentDto = DocumentDto.builder()
                .parentId(catalogueDto.getId())
                .concreteDocument(concreteDocumentDto)
                .documentType("fax")
                .priority("LOW")
                .build();

        ResponseEntity<DocumentDto> document_response = template.withBasicAuth(thirdUserLogin, thirdUserPassword)
                .postForEntity("/documents", documentDto, DocumentDto.class);

        Assert.assertEquals(document_response.getStatusCode(), HttpStatus.OK);

        documentDto = document_response.getBody();

        Assert.assertNotNull(documentDto);
        Assert.assertEquals(new_doc_name, documentDto.getName());
        Assert.assertEquals("LOW", documentDto.getPriority());
        return documentDto;

    }

    public void read_access_check(DocumentDto documentDto, DocumentDto documentDto2){
        ResponseEntity<DocumentDto> document_response = template.withBasicAuth(thirdUserLogin, thirdUserPassword)
                .getForEntity("/documents/{id}", DocumentDto.class, documentDto.getId());
        Assert.assertEquals(document_response.getStatusCode(), HttpStatus.OK);

        ResponseEntity<DocumentDto> document_response1 = template.withBasicAuth(newUserLogin, newUserPass)
                .getForEntity("/documents/{id}", DocumentDto.class, documentDto2.getId());
        Assert.assertEquals(document_response1.getStatusCode(), HttpStatus.OK);

        ResponseEntity<DocumentDto> document_response2 = template.withBasicAuth(thirdUserLogin, thirdUserPassword)
                .getForEntity("/documents/{id}", DocumentDto.class, documentDto.getId());
        Assert.assertEquals(document_response2.getStatusCode(), HttpStatus.OK);

        ResponseEntity<DocumentDto> document_response3 = template.withBasicAuth(newUserLogin, newUserPass)
                .getForEntity("/documents/{id}", DocumentDto.class, documentDto2.getId());
        Assert.assertEquals(document_response3.getStatusCode(), HttpStatus.OK);
    }

    public void modify_check(DocumentDto documentDto, DocumentDto documentDto2){
        String new_doc_name = "mod_doc";
        ConcreteDocumentDto concreteDocumentDto = ConcreteDocumentDto.builder()
                .name(new_doc_name)
                .description("descr")
                .build();

        concreteDocumentDto.setParentDocumentId(documentDto.getId());
        ResponseEntity<DocumentDto> document_response = template.withBasicAuth(thirdUserLogin, thirdUserPassword)
                .postForEntity("/documents/modify", concreteDocumentDto, DocumentDto.class);
        Assert.assertEquals(document_response.getStatusCode(), HttpStatus.FORBIDDEN);

        ResponseEntity<DocumentDto> document_response1 = template.withBasicAuth(newUserLogin, newUserPass)
                .postForEntity("/documents/modify", concreteDocumentDto, DocumentDto.class);
        Assert.assertEquals(document_response1.getStatusCode(), HttpStatus.OK);
        DocumentDto documentDto3 = document_response1.getBody();
        Assert.assertEquals(new_doc_name, documentDto3.getName());
        Assert.assertEquals(2L, (long) documentDto3.getConcreteDocument().getVersion());

    }

    public void modify_catalogue_check(CatalogueDto catalogueDto, CatalogueDto catalogueDto1){
        ResponseEntity<CatalogueDto> catalogue_response = template.withBasicAuth(thirdUserLogin, thirdUserPassword)
                .postForEntity("/catalogue/modify", catalogueDto, CatalogueDto.class);
        Assert.assertEquals(HttpStatus.FORBIDDEN, catalogue_response.getStatusCode());

        ResponseEntity<CatalogueDto> catalogue_response1 = template.withBasicAuth(thirdUserLogin, thirdUserPassword)
                .postForEntity("/catalogue/modify", catalogueDto1, CatalogueDto.class);
        Assert.assertEquals(HttpStatus.OK, catalogue_response1.getStatusCode());

        ResponseEntity<CatalogueDto> catalogue_response2 = template.withBasicAuth(newUserLogin, newUserPass)
                .postForEntity("/catalogue/modify", catalogueDto, CatalogueDto.class);
        Assert.assertEquals(HttpStatus.OK, catalogue_response2.getStatusCode());

        ResponseEntity<CatalogueDto> catalogue_response3 = template.withBasicAuth(newUserLogin, newUserPass)
                .postForEntity("/catalogue/modify", catalogueDto1, CatalogueDto.class);
        Assert.assertEquals(HttpStatus.OK, catalogue_response3.getStatusCode());
    }




    @Test
    public void test() {

        // CHECK LOGIN AS ADMIN
        check_login_as_admin();

        // NEW USER CREATION (IF NOT EXISTS)
        new_user_creation();

        // THIRD USER CREATION (IF NOT EXIST)
        third_user_creation();

        // CHECK LOGIN WITH NEW USER
        UserDto newUserDto = check_login_with_new_user();

        // GRANT ADMIN ACCESS TO NEW USER
        grant_admin_access_to_new_user_by_admin_user(newUserDto);

        // DECLINE ADMIN ACCESS TO NEW USER
        decline_admin_access_for_new_user_by_admin_user(newUserDto);

        // GET ROOT CATALOGUE
        CatalogueDto rootCatalogueDto = get_root_catalogue_by_admin_user();

        // NEW CATALOGUE CREATION BY ADMIN USER
        CatalogueDto new_catalogue_in_root_folder = new_catalogue_in_root_folder_creation_by_admin(rootCatalogueDto);

        // GET NEW CATALOGUE BY REGULAR NEW USER
        get_catalogue_by_new_user(new_catalogue_in_root_folder.getId());

        // CREATION OF NEW FOLDER BY NOT PERMITTED USER
        creation_of_new_folder_by_new_not_permitted_user(new_catalogue_in_root_folder);

        // GIVING RIGHTS TO READWRITE NEW_TEST_CATALOGUE TO NEW USER
        giving_readwrite_rights_to_user_in_catalogue(newUserDto, new_catalogue_in_root_folder);

        //CREATION OF NEW FOLDER BY PERMITTED USER
        CatalogueDto thirdCatalogue = creation_of_new_folder_by_new_permitted_user(new_catalogue_in_root_folder);

        // NEW DOC CREATION BY NEW USER IN NEW CATALOGUE
        DocumentDto documentDto = new_document_creation_in_new_cat_by_new_user(new_catalogue_in_root_folder);

        // THIRD DOC BY THIRD USER IN THIRD CATALOGUE
        DocumentDto documentDto2 = new_document_creation_in_new_cat_by_new_user(thirdCatalogue);

        // ACCESS CHECK
        read_access_check(documentDto, documentDto2);

        //MODIFY CHECK
        modify_check(documentDto, documentDto2);

        //modify catalogue check
        modify_catalogue_check(new_catalogue_in_root_folder, thirdCatalogue);

    }


    @After
    public void finalMethod() {
        CatalogueDto catalogueDto = template.withBasicAuth(adminLogin, adminPass)
                .getForObject("/catalogue/root", CatalogueDto.class);
        final Long rootCatalogueId = catalogueDto.getId();

        catalogueDto = CatalogueDto.builder()
                .parentId(rootCatalogueId)
                .name(NEW_TEST_CATALOGUE_IN_ROOT_FOLDER)
                .build();

        template.withBasicAuth(adminLogin, adminPass)
                .postForLocation("/catalogue/deleteByNameAndParentId", catalogueDto);
    }

}
