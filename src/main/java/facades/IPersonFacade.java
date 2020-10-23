package facades;

import dto.PersonDTO;

import entities.Person;
import exceptions.AlreadyExist;
import exceptions.CityNotFound;
import exceptions.MissingInput;
import exceptions.PersonNotFound;
import java.util.List;

public interface IPersonFacade {

    public List<PersonDTO> getAllPersons ();
    public PersonDTO getPersonByPhone(String phone) throws PersonNotFound;
    public PersonDTO addPerson (PersonDTO personDTO) throws MissingInput, PersonNotFound, CityNotFound, AlreadyExist;
    public List<PersonDTO> getAllPersonsFromCity (String city) throws PersonNotFound;
    public List<PersonDTO> getAllPersonsWithHobby (String hobby) throws PersonNotFound;
    public PersonDTO deletePerson (int id) throws PersonNotFound;
    public PersonDTO editPerson (PersonDTO personDTO) throws PersonNotFound, MissingInput, CityNotFound;
    public PersonDTO getPersonById(int id) throws PersonNotFound;
}
