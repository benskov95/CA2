package facades;

import dto.PersonDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import exceptions.MissingInput;
import exceptions.PersonNotFound;
import utils.EMF_Creator;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//@Disabled
public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade facade;
    private static Person p1, p2;
    private static Address a1, a2;
    private static CityInfo c1;
    private static List<Phone> phones1, phones2;
    private static List<Hobby> h1, h2;

    public PersonFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
       emf = EMF_Creator.createEntityManagerFactoryForTest();
       facade = PersonFacade.getPersonFacade(emf);
       prepareTestPersons();
   }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
       EntityManager em = emf.createEntityManager();
       try {
           em.getTransaction().begin();
           em.createNamedQuery("Person.deleteAllRows").executeUpdate();
           em.createNamedQuery("Address.deleteAllRows").executeUpdate();
           em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
           em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
           em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
           em.persist(p1);
           em.persist(p2);
           em.getTransaction().commit();
       } finally {
           em.close();
       }
    }

    @AfterEach
    public void tearDown() throws PersonNotFound {
    }
    
    @Test
    public void testGetAllPersons() {
        List<PersonDTO> persons = facade.getAllPersons();
        assertTrue(persons.size() == 2);
    }
    
    @Test
    public void testAddPerson() throws MissingInput, PersonNotFound {
        PersonDTO addedPerson = facade.addPerson(createTestPerson());
        assertEquals(3, facade.getAllPersons().size());
        assertTrue(addedPerson.getCity().equals("Valhalla"));
    }
    
    @Test
    public void testGetPersonByPhone() throws PersonNotFound {
        PersonDTO pDTO = facade.getPersonByPhone(p1.getPhoneNumbers().get(0).getNumber());
        assertEquals(p1.getFirstName(), pDTO.getFirstName());
    }
    
    @Test
    public void testGetPersonByID() throws PersonNotFound {
        PersonDTO pDTO = facade.getPersonById(p2.getId());
        assertTrue(pDTO.getEmail().equals(p2.getEmail()));
    }
    
    @Test
    public void testGetAllPersonsWithHobby() throws PersonNotFound {
        List<PersonDTO> persons = facade.getAllPersonsWithHobby("Dancing");
        assertEquals(1, persons.size());
    }
    
    @Test
    public void testGetAllPersonsFromCity() throws PersonNotFound {
        List<PersonDTO> persons = facade.getAllPersonsFromCity("Valhalla");
        assertEquals(2, persons.size());
    }
    
    @Test
    public void testDeletePerson() throws PersonNotFound, MissingInput {
        PersonDTO deleted = facade.deletePerson(p1.getId());
        PersonNotFound thrown
                = assertThrows(PersonNotFound.class, () -> {
                    facade.getPersonById(deleted.getId());
                });
        assertTrue(thrown.getMessage().equals("Person with the provided ID was not found."));
    }

    
    @Test
    public void testEditPerson() throws PersonNotFound, MissingInput {
        assertTrue(p1.getFirstName().equals("Joe"));
        p1.setFirstName("Anton");
        facade.editPerson(new PersonDTO(p1));
        assertTrue(facade.getPersonById(p1.getId()).getFirstName().equals("Anton"));
    }
    
    @Test
    public void testPersonNotFoundException() {
        PersonNotFound thrown
                = assertThrows(PersonNotFound.class, () -> {
                    facade.getPersonById(500);
                });
        assertTrue(thrown.getMessage().equals("Person with the provided ID was not found."));
    }
    
    @Test
    public void testMissingInputException() {
        PersonDTO pTest = new PersonDTO(p1);
        pTest.setFirstName("");
        MissingInput thrown
                = assertThrows(MissingInput.class, () -> {
                    facade.addPerson(pTest);
                });
        assertTrue(thrown.getMessage().equals("All fields must be filled out"));
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
    h2.add(new Hobby("Jogging", "jogging.dk", "general", "outdoors"));

    p1.setAddress(a1);
    p1.setPhoneNumbers(phones1);
    p1.setHobbies(h1);

    p2.setAddress(a2);
    p2.setPhoneNumbers(phones2);
    p2.setHobbies(h2);
}
    
    private PersonDTO createTestPerson() {
        PersonDTO pDTO = new PersonDTO(p2);
        pDTO.setFirstName("Leif");
        pDTO.setLastName("Grågård");
        pDTO.setEmail("leif@testmail.dk");
        pDTO.setPhoneNumbers(new ArrayList());
        pDTO.getPhoneNumbers().add(new Phone("84772323", "work"));
        return pDTO;
    }

}
