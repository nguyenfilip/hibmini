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

public class Lifecycle {
    private static EntityManagerFactory emf;
    
   
//    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
//    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    
    @BeforeClass 
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("default");
    }
      
    /**
     *  UUID
     *  em.contains()
     */
    @Test
    public void uuidPersist(){
        Company redHat = new Company();
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        System.out.println("Contains: "+em.contains(redHat));
        em.persist(redHat);
        System.out.println("Contains: "+em.contains(redHat));
        System.out.println("********** After Persist!");
        em.getTransaction().commit();
        System.out.println("********** After Commit!");
        em.close();
    }
    
    /**
     * Detach after persist     * 
     *  show merge()
     */
    @Test
    public void identityPersist(){
        Employee filip = new Employee();
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(filip);
        //em.merge(filip)
//        System.out.println("Generated ID: "+filip.getId());
        System.out.println("********** After Persist!");
        em.getTransaction().commit();
        System.out.println("********** After Commit!");
        em.close();
    }
    
    /**
     * Name change, persistence context vs transaction
     * detach by closing em, merge
     */
    @Test
    public void updatingEntityAndDetach(){
        Employee filip = new Employee();
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        filip.setName("Filip");
        em.persist(filip);
        em.getTransaction().commit();
        
        System.out.println("Contains: "+ em.contains(filip));
        filip.setName("FILIP");
    }
    
    @Test
    public void persistOutOfTx(){
        Company redHat = new Company();
        EntityManager em = emf.createEntityManager();
        em.persist(redHat);
        em.getTransaction().begin();        
        em.getTransaction().commit();
    }
        
    @Test
    public void flushNoTxCommit(){
        Employee emp  = new Employee();
        emp.setName("Filig");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();        
        em.persist(emp);
        em.getTransaction().commit();
        em.close();
        
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            emp = em.merge(emp);
            emp.setName("Filip");
            em.flush();
            em.getTransaction().rollback();
            em.getTransaction().commit();
            em.close();
        } catch (Exception e) {
            System.out.println("Empty catch block");
        }
        
        em = emf.createEntityManager();
        System.out.println(em.find(Employee.class, emp.getId()).getName());
    }
    
    
    @Test
    public void sameInstance(){
        Company redHat = new Company();
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();        
        redHat.setName("Red Hat");
        em.persist(redHat);
        em.getTransaction().commit();
        em.close();
        
        em = emf.createEntityManager();
        Company c1 = em.find(Company.class, redHat.getId());
//        em.clear();
        Company c2 = em.find(Company.class, redHat.getId());
        
        System.out.println("****** INSTANCE IDS *********");
        System.out.println(System.identityHashCode(redHat));
        System.out.println(System.identityHashCode(c1));
        System.out.println(System.identityHashCode(c2));
    }
       
    
    
    @Test
    public void flushMode(){
        Company redHat = new Company();
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();        
        em.setFlushMode(FlushModeType.AUTO);
        redHat.setName("Red Hat");
        em.persist(redHat);
        System.out.println("******** BEFORE SELECT");
        System.out.println("SIZE: "+ em.createQuery("SELECT count(c) FROM Company c", Long.class).getSingleResult());
        System.out.println("******** AFTER SELECT");
        em.getTransaction().commit();
        em.close();
    }
}
