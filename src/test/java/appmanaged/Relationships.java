package appmanaged;

import org.hibernate.jpa.internal.util.PersistenceUtilHelper;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.OneToMany;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import cz.nguyen.entity.Company;
import cz.nguyen.entity.Employee;

public class Relationships {
    private static EntityManagerFactory emf;
    
   
    @BeforeClass 
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("default");
    }
      
    @Test
    public void runtimeConsistency(){
        Company redHat = new Company();
        redHat.setName("Red Hat");
        Employee filip = new Employee();
        filip.setName("Filip");
        
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        
        em.persist(redHat);
        em.persist(filip);
        filip.setCompany(redHat);
        
        em.getTransaction().commit();
        em.close();
        
        em = emf.createEntityManager();
        em.getTransaction().begin();
        Company c = em.find(Company.class, redHat.getId());
        System.out.println(c + " employees: "+c.getEmployees());
        em.getTransaction().commit();
        em.close();
    }
    
    /**
     * Set the following on Company.employees relationship
     * 
     * @OneToMany(mappedBy="company", cascade = CascadeType.PERSIST)
     */
    @Test
    public void unschedulingDelete(){
        Company redHat = new Company();
        redHat.setName("Red Hat");
        Employee filip = new Employee();
        filip.setName("Filip");
        
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        
        em.persist(redHat);
        em.persist(filip);
        filip.setCompany(redHat);
        
        em.getTransaction().commit();
        em.close();
        
        em = emf.createEntityManager();
        em.getTransaction().begin();
        Company c = em.find(Company.class, redHat.getId());
        //Assume we know there is only one employee
        Employee e = c.getEmployees().iterator().next();
        /**
         * This delete will be unscheduled because of the 
         * cascade persist. See logs by 
         * org.hibernate.event.internal.DefaultPersistEventListener
         */
        em.remove(e);
        em.getTransaction().commit();
        em.close();
    }
}
