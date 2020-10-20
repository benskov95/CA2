package facades;

import dto.CityInfoDTO;
import entities.CityInfo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

public class CityInfoFacade {
    
    private static CityInfoFacade instance;
    private static EntityManagerFactory emf;

    private CityInfoFacade() {
    }

    public static CityInfoFacade getCityInfoFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CityInfoFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public List<CityInfoDTO> getAllZipCodes() {
        EntityManager em = getEntityManager();
        TypedQuery q = em.createQuery("SELECT c FROM CityInfo c", CityInfo.class);
        List<CityInfoDTO> cDTOs = new ArrayList();
        List<CityInfo> zipCodes = q.getResultList();
        for (CityInfo c : zipCodes) {
            cDTOs.add(new CityInfoDTO(c));
        }
        return cDTOs;
    }

    
}
