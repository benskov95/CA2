package facades;

import dto.PersonDTO;
import entities.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PersonFacade implements IPersonFacade{

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private PersonFacade() {}

    /**
     *
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
            em.getTransaction().begin();
            em.persist(newPerson);
            em.getTransaction().commit();

            return new PersonDTO(newPerson);

        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO getPersonByPhone(String phone) {

        EntityManager em = getEntityManager();

        TypedQuery<Person> query = em.createQuery(
                        "SELECT p FROM Person p join p.phoneNumbers ph where ph.number = :phone", Person.class);

        query.setParameter("phone", phone);

        return new PersonDTO(query.getSingleResult());

    }

    private Person preparePerson (PersonDTO personDTO){

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

    public static void main(String[] args) {

//        Person person = new Person("Pelle@mail.dk","Pelle","Rasmussen");
//        Address address = new Address("Smedeløkken 66", new CityInfo(3770,"Allinge"));
//        List<Hobby> hobbies = new ArrayList<>();
//        hobbies.add(new Hobby("Tennis","ko lort", "Prutfis", "lort"));
//        List<Phone> phones = new ArrayList<>();
//        phones.add(new Phone("2010210102", "work"));
//        phones.add(new Phone("201102", "home"));
//
//        person.setPhoneNumbers(phones);
//        person.setHobbies(hobbies);
//        person.setAddress(address);
//
//        PersonDTO personDTO = new PersonDTO(person);
//
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
//
//        PersonFacade.getPersonFacade(emf).addPerson(personDTO);
       PersonDTO personDTO = PersonFacade.getPersonFacade(emf).getPersonByPhone("2010210102");
        System.out.println(personDTO.getFirstName());
    }
}
