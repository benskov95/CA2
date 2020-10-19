package facades;

import dto.PersonDTO;
import entities.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PersonFacade implements IPersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private PersonFacade() {
    }

    /**
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PersonFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }


    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }


    @Override
    public List<PersonDTO> getAllPersons() {

        EntityManager em = getEntityManager();

        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p", Person.class);

        List<Person> resultList = query.getResultList();

        List<PersonDTO> dtoList = new ArrayList<>();

        for (Person person : resultList) {
            dtoList.add(new PersonDTO(person));

        }


        return dtoList;
    }

    @Override
    public PersonDTO addPerson(PersonDTO personDTO) {

        EntityManager em = getEntityManager();

        try {
            Person newPerson = preparePerson(personDTO);
            createNewOrUseExisitingInfo(em, newPerson);
            em.getTransaction().begin();
            em.persist(newPerson);
            em.getTransaction().commit();

            return new PersonDTO(newPerson);

        } finally {
            em.close();
        }
    }

    @Override
    public List<PersonDTO> getAllPersonsFromCity(String city) {

        EntityManager em = getEntityManager();

        TypedQuery<Person> query = em.createQuery(
                "SELECT p FROM Person p join p.address.cityInfo c where c.city = :city", Person.class);
        query.setParameter("city", city);
        List<Person> resultList = query.getResultList();

        List<PersonDTO> personDTOList = new ArrayList<>();
        for (Person person : resultList) {

            personDTOList.add(new PersonDTO(person));

        }
        return personDTOList;
    }

    @Override
    public List<PersonDTO> getAllPersonsWithHobby(String hobby) {
        return null;
    }

    @Override
    public PersonDTO deletePerson(int id) {
        return null;
    }

    @Override
    public PersonDTO editPerson(PersonDTO personDTO) {
        return null;
    }

    @Override
    public PersonDTO getPersonByPhone(String phone) {

        EntityManager em = getEntityManager();

        TypedQuery<Person> query = em.createQuery(
                "SELECT p FROM Person p join p.phoneNumbers ph where ph.number = :phone", Person.class);

        query.setParameter("phone", phone);

        return new PersonDTO(query.getSingleResult());

    }

    private Person preparePerson(PersonDTO personDTO) {

        Person newPerson = new Person();
        CityInfo newCityInfo = new CityInfo(personDTO.getZipCode(), personDTO.getCity());
        Address newAddress = new Address(personDTO.getStreet(), newCityInfo);
        newPerson.setFirstName(personDTO.getFirstName());
        newPerson.setLastName(personDTO.getLastName());
        newPerson.setAddress(newAddress);
        newPerson.setPhoneNumbers(personDTO.getPhoneNumbers());
        newPerson.setEmail(personDTO.getEmail());
        newPerson.setDateCreated(new Date());
        newPerson.setHobbies(personDTO.getHobbies());

        return newPerson;
    }

    private void createNewOrUseExisitingInfo(EntityManager em, Person p) {
        Query q1 = em.createQuery("SELECT a FROM Address a");
        Query q2 = em.createQuery("SELECT c FROM CityInfo c");
        Query q3 = em.createQuery("SELECT h FROM Hobby h");
        Query q4 = em.createQuery("SELECT p FROM Person p WHERE p.email = :email");
        q4.setParameter("email", p.getEmail());
        List<Address> addresses = q1.getResultList();
        List<CityInfo> cities = q2.getResultList();
        List<Hobby> hobbies = q3.getResultList();
        List<Person> existingPersons = q4.getResultList();

        for (Address a : addresses) {
            if (a.getStreet().equals(p.getAddress().getStreet())) {
                p.setAddress(a);
            }
        }
        for (CityInfo c : cities) {
            if (c.getCity().equals(p.getAddress().getCityInfo().getCity())) {
                p.getAddress().setCityInfo(c);
            }
        }
        int hobbyCount = -1;
        for (Hobby h1 : p.getHobbies()) {
            hobbyCount++;
            for (Hobby h2 : hobbies) {
                if (h2.getName().equals(h1.getName())) {
                    p.getHobbies().set(hobbyCount, h2);
                }
            }
        }
        if (existingPersons.size() > 0) {
            p.setEmail(existingPersons.get(0).getEmail());
        }
    }

//    public static void main(String[] args) {
//
//        Person person = new Person("John@mail.dk", "John", "Rasmussen");
//        Address address = new Address("Smedeløkken 66", new CityInfo(3770, "Allinge"));
//        List<Hobby> hobbies = new ArrayList<>();
//        hobbies.add(new Hobby("Fodbold", "ko lort", "Prutfis", "lort"));
//        List<Phone> phones = new ArrayList<>();
//        phones.add(new Phone("4214214", "work"));
//        phones.add(new Phone("1122444", "home"));
//
//        person.setPhoneNumbers(phones);
//        person.setHobbies(hobbies);
//        person.setAddress(address);
//
//        PersonDTO personDTO = new PersonDTO(person);
//
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
//
//        PersonFacade.getPersonFacade(emf).addPerson(personDTO);
//       PersonDTO personDTO = PersonFacade.getPersonFacade(emf).getPersonByPhone("2010210102");
//        System.out.println(personDTO.getFirstName());
//    }
 //   }
}
