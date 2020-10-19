package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.PersonDTO;
import facades.PersonFacade;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import utils.EMF_Creator;

@Path("persons")
public class PersonResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final PersonFacade FACADE = PersonFacade.getPersonFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllPersons() {
        List<PersonDTO> personDTOs = FACADE.getAllPersons();
        return new Gson().toJson(personDTOs);
    }
    
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonById(@PathParam("id") int id) {
        PersonDTO personDTO = FACADE.getPersonById(id);
        return new Gson().toJson(personDTO);
    }

    @GET
    @Path("city/{city}")
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllPersonsFromCity(@PathParam("city") String city) {
        List<PersonDTO> personDTOs = FACADE.getAllPersonsFromCity(city);
        return new Gson().toJson(personDTOs);
    }

    @GET
    @Path("hobby/{hobby}")
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllPersonsWithHobby(@PathParam("hobby") String hobby) {
        List<PersonDTO> personDTOs = FACADE.getAllPersonsWithHobby(hobby);
        return new Gson().toJson(personDTOs);
    }

    @GET
    @Path("phone/{phone}")
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonByPhone(@PathParam("phone") String phone) {
        PersonDTO personDTO = FACADE.getPersonByPhone(phone);
        return new Gson().toJson(personDTO);
    }

    @GET
    @Path("count/{hobby}")
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllPersonsWithHobbyCount(@PathParam("hobby") String hobby) {
        List<PersonDTO> personDTOs = FACADE.getAllPersonsWithHobby(hobby);
        return "{\"count\":" + personDTOs.size() + "}";
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String addPerson(String person) {
        PersonDTO personDTO = GSON.fromJson(person, PersonDTO.class);
        personDTO = FACADE.addPerson(personDTO);
        return new Gson().toJson(personDTO);
    }

    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public String editPerson(@PathParam("id") int id, String person) {
        PersonDTO personDTO = GSON.fromJson(person, PersonDTO.class);
        personDTO.setId(id);
        personDTO = FACADE.editPerson(personDTO);
        return new Gson().toJson(personDTO);
    }

    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public String deletePerson(@PathParam("id") int id) {
        PersonDTO personDTO = FACADE.deletePerson(id);
        return new Gson().toJson(personDTO);
    }
}
