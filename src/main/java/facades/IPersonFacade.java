package facades;

import dto.PersonDTO;
import java.util.List;

public interface IPersonFacade {

    public List<PersonDTO> getAllPersons ();
    public PersonDTO getPersonByPhone(String phone);
    public PersonDTO addPerson (PersonDTO personDTO);
    public List<PersonDTO> getAllPersonsFromCity (String city);
    public List<PersonDTO> getAllPersonsWithHobby (String hobby);
    public PersonDTO deletePerson (int id);
    public PersonDTO editPerson (PersonDTO personDTO);
    public PersonDTO getPersonById(int id);
}
