package rest;

import dto.HobbyDTO;
import dto.PersonDTO;
import entities.*;
import exceptions.PersonNotFound;
import facades.PersonFacade;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
//Uncomment the line below, to temporarily disable this test
//@Disabled
public class PersonResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static EntityManagerFactory emf;
    private static PersonFacade facade;
    private static Person p1, p2;
    private static Address a1, a2;
    private static CityInfo c1;
    private static List<Phone> phones1, phones2;
    private static List<Hobby> h1, h2;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;


    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        
        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;


    }
    
    @AfterAll
    public static void closeTestServer(){
        //System.in.read();
         //Don't forget this, if you called its counterpart in @BeforeAll
         EMF_Creator.endREST_TestWithDB();
         httpServer.shutdownNow();
    }
    
    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {

        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = PersonFacade.getPersonFacade(emf);
        EntityManager em = emf.createEntityManager();
        prepareTestPersons();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            em.persist(p1);
            em.persist(p2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }

    }
    public static void prepareTestPersons() {

        phones1 = new ArrayList();
        phones2 = new ArrayList();
        h1 = new ArrayList();
        h2 = new ArrayList();

        p1 = new Person("joe@testmail.dk", "Joe", "Hansen");
        p2 = new Person("gurli@testmail.dk", "Gurli", "Kofod");
        c1 = new CityInfo("9999", "Valhalla");
        a1 = new Address("Troldevej 9", c1);
        a2 = new Address("Vikingegade 35", c1);

        phones1.add(new Phone("2834928", "home"));
        phones2.add(new Phone("99483271", "work"));
        phones2.add(new Phone("12364823", "home"));

        h1.add(new Hobby("Sailing", "sailing.dk", "general", "outdoors"));
        h1.add(new Hobby("Dancing", "dancing.dk", "general", "indoors"));
        h2.add(new Hobby("Sailing", "sailing.dk", "general", "outdoors"));

        p1.setAddress(a1);
        p1.setPhoneNumbers(phones1);
        p1.setHobbies(h1);

        p2.setAddress(a2);
        p2.setPhoneNumbers(phones2);
        p2.setHobbies(h2);
    }
    
    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/persons").then().statusCode(200);
    }
   
    //This test assumes the database contains two rows

    
    @Test
    public void testHobbyCount() throws Exception {
        given()
        .contentType("application/json")
        .get("/persons/count/Sailing").then()
        .assertThat()
        .statusCode(HttpStatus.OK_200.getStatusCode())
        .body("count", equalTo(2));   
    }
    @Test
    public void testGetAllPersons() {
        List<PersonDTO> personsList;

       personsList = given()
                .contentType("application/json")
                .get("/persons/")
                .then()
                .extract().body().jsonPath().getList("" ,PersonDTO.class);


        assertThat(personsList.size(), equalTo(2));


    }
    @Test
    public void testGetPersonByPhone() {

        String phone = p1.getPhoneNumbers().get(0).getNumber();

        given()
                .contentType("application/json")
                .get("/persons/phone/{phone}",phone)
                .then()
                .assertThat().statusCode(200)
                .body("firstName", equalTo(p1.getFirstName()));


    }

    @Test
    public  void testGetPersonsWithGivenHobbies(){

        String hoppyName = p2.getHobbies().get(0).getName();
        List<PersonDTO> personDTOList;

        personDTOList = given()
                .contentType("application/json")
                .get("/persons/hobby/{hobby}", hoppyName)
                .then()
                .extract().body().jsonPath().getList("", PersonDTO.class);


        assertThat(personDTOList.size(), equalTo(2));
    }

    @Test
    public void testGetPersonsFromGivenCity(){

        String nameOfCity = p1.getAddress().getCityInfo().getCity();

        given()
                .contentType("application/json")
                .get("/persons/city/{city}", nameOfCity)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("zipCode", hasItem("9999"));

    }
    @Test
    public void testGetAllHobbies(){

        HobbyDTO hobbyDTO = new HobbyDTO(h1.get(0));

        given()
                .contentType("application/json")
                .get("/persons/hobbies")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("name", hasItem("Sailing"))
                .and()
                .body("name", hasItem("Dancing"));



    }
    @Test
    public void TestAddPerson() {

        Person testPerson = new Person("Pelle@mail.dk", "Pelle", "Rasmussen");
        Address testAddress = new Address("Testvej 4", new CityInfo("666", "Helvede"));

        List<Phone> testPhoneList = new ArrayList<>();
        Phone testPhone = new Phone("666", "Work");
        testPhoneList.add(testPhone);

        testPerson.setAddress(testAddress);
        testPerson.setHobbies(h1);
        testPerson.setPhoneNumbers(testPhoneList);

        PersonDTO personDTO = new PersonDTO(testPerson);

        given()
                .contentType("application/json")
                .body(personDTO)
                .when()
                .post("persons")
                .then()
                .body("firstName", equalTo("Pelle"));


    }
    @Test
    public void TestGetPersonById (){

        int p_id = p1.getId();

        given()
                .contentType("application/json")
                .get("persons/id/{id}", p_id)
                .then()
                .assertThat().statusCode(200)
                .and()
                .assertThat()
                .body("firstName", equalTo(p1.getFirstName()));


    }
    @Test
    public void TestDeletePerson(){

        int p_id = p1.getId();

        given()
                .contentType("application/json")
                .delete("persons/{id}",p_id)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .assertThat()
                .body("id", equalTo(p1.getId()));

        List<PersonDTO> personsList;

        personsList = given()
                .contentType("application/json")
                .get("/persons/")
                .then()
                .extract().body().jsonPath().getList("" ,PersonDTO.class);


        assertThat(personsList.size(), equalTo(1));


}
        @Test
    public void TestEditPerson()  {

            p1.setFirstName("John");
            PersonDTO personDTO = new PersonDTO(p1);

            given()
                    .contentType("application/json")
                    .body(personDTO)
                    .put("persons/{id}", p1.getId())
                    .then()
                    .assertThat()
                    .statusCode(200)
                    .and()
                    .assertThat()
                    .body("id", equalTo(p1.getId()))
                    .and()
                    .body("firstName", equalTo("John"));


        }

        @Test
    public void TestExeceptionGetPersonByPhone() {

        given()
                .contentType("application/json")
                .get("persons/phone/1234")
                .then()
                .assertThat()
                .statusCode(404)
                .and()
                .assertThat()
                .body("message", equalTo("Person with the provided phone number was not found."));


        }


}

