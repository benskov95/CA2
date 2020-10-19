package facades;

import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import utils.EMF_Creator;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//@Disabled
public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static FacadeExample facade;
    private static Person p1, p2;
    private static Address a1, a2;
    private static CityInfo c1, c2;
    private static List<Phone> phones1, phones2;
    private static List<Hobby> h1, h2;

    public PersonFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
       emf = EMF_Creator.createEntityManagerFactoryForTest();
       facade = FacadeExample.getFacadeExample(emf);
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        prepareTestPersons();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows", Person.class);
            em.persist(p1);
            em.persist(p2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
    }
    
    @Test
    public void testGetAllPersons() {
//        List<PersonDTO> persons = facade.getAllPersons();
//        assertTrue(persons.size() == 2);

    }
    
    public static void prepareTestPersons() {
    phones1 = new ArrayList();
    phones2 = new ArrayList();
    h1 = new ArrayList();
    h2 = new ArrayList();

    p1 = new Person("joe@testmail.dk", "Joe", "Hansen");
    p2 = new Person("gurli@testmail.dk", "Gurli", "Kofod");
    c1 = new CityInfo(3700, "Rønne");
    c2 = new CityInfo(9999, "Valhalla");
    a1 = new Address("Troldevej 9", c1);
    a2 = new Address("Vikingegade 35", c2);

    phones1.add(new Phone("2834928", "home"));
    phones2.add(new Phone("99483271", "work"));
    phones2.add(new Phone("12364823", "home"));

    h1.add(new Hobby("Sailing", "sailing.dk", "general", "outdoors"));
    h1.add(new Hobby("Dancing", "dancing.dk", "general", "indoors"));
    h2.add(new Hobby("Fishing", "fishing.dk", "general", "outdoors"));

    p1.setAddress(a1);
    p1.setPhoneNumbers(phones1);
    p1.setHobbies(h1);

    p2.setAddress(a2);
    p2.setPhoneNumbers(phones2);
    p2.setHobbies(h2);
}

}
