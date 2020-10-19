package facades;

import dto.PersonDTO;
import entities.Person;

import java.util.List;

public interface IPersonFacade {

    public List<PersonDTO> getAllPersons ();
//    public PersonDTO getPersonByPhone(String phone);
    public PersonDTO addPerson (PersonDTO personDTO);
}
