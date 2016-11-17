package appmanaged;

import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import cz.nguyen.entity.Company;
import cz.nguyen.entity.Employee;

public class JdbcBatching {
    private static EntityManagerFactory emf;
    
    private static Company microsoft = new Company("Microsoft");
    private static Company redhat = new Company("Red Hat");
    private static Company linkedin = new Company("LinkedIn");
    private static Company google = new Company("Google");
    private static Company lehman = new Company("Lehman Brothers");
    
    @Test 
    public  void batchInsert() {
        emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
                
        em.persist(microsoft);
        em.persist(redhat);
        em.persist(linkedin);
        em.persist(google);
        em.persist(lehman);
        
        System.out.println("Before commit");
        
        em.getTransaction().commit();
        em.close();
        System.out.println("\n\n***** DATA COMITED **********\n\n\n");
    }
}
