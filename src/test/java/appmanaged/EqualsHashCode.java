package appmanaged;

import org.hibernate.jpa.internal.util.PersistenceUtilHelper;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import cz.nguyen.entity.Company;
import cz.nguyen.entity.Employee;

public class EqualsHashCode {
    private static EntityManagerFactory emf;
    
    @BeforeClass 
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("default");
    }
      
    @Test
    public void proxyHashCode() {
        EntityManager em = emf.createEntityManager();
        
        Employee filip = new Employee();
        filip.setName("Filip");
        em.getTransaction().begin();
        em.persist(filip);
        em.getTransaction().commit();
        em.close();
        
        em = emf.createEntityManager();
        em.getTransaction().begin();
        Employee f = em.getReference(Employee.class , filip.getId());
        System.out.println("Equals: "+filip.equals(f));
        em.getTransaction().commit();
        
    }
}
