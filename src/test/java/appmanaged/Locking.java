package appmanaged;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;

import cz.nguyen.entity.Employee;

public class Locking {
    private static EntityManagerFactory emf;
    private static Employee filip;

    @BeforeClass 
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        filip = new Employee();
        filip.setName("Filip");
        filip.setRewardPoints(1000);
        em.persist(filip);
        em.getTransaction().commit();
        em.close();
    }
    
    private EntityManager startTransaction () {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        return em;
    }
    
    private void commitTransaction(EntityManager em) {
        em.getTransaction().commit();
        em.close();
    }
    
    /**
     * 1) no locking
     * 2) optimistic locking
     * 3) pessimistic locking
     * 4) shared locking
     * 
     */
    @Test 
    public void race() throws InterruptedException {
        
        /**
         * This first thread increases number of reward points by 20
         */
        Thread t1 = new Thread(
            new Runnable() {
                public void run() {
                    EntityManager em = startTransaction();
                    print("T1 loading employee");
                    Employee e = em.find(Employee.class, filip.getId());
                    
                    print("T1 loaded");
                    sleep(100); //some random length activity
                    e.setRewardPoints(e.getRewardPoints()+20);
                    print("T1 comitting");
                    try {
                        commitTransaction(em);
                    } catch (Exception ex){ 
                        print("Error comitting transaction T1");
                        throw ex;
                    }
                    print("T1 commit done");
                }
            }
        );
        t1.setName("T1");
        
        /**
         * This thread decreases number of reward points by 20
         */
        Thread t2 = new Thread(
            new Runnable() {
                public void run() {
                    EntityManager em = startTransaction();
                    print("T2 loading employee");
                    Employee e = em.find(Employee.class, filip.getId());
                    print("T2 loaded");
                    sleep(120);//some random length activity
                    e.setRewardPoints(e.getRewardPoints()-20);
                    print("T2 comitting");
                    try {
                        commitTransaction(em);
                    } catch (Exception ex){ 
                        print("Error comitting transaction T2");
                        throw ex;
                    }
                    print("T2 commit done");
                }
            }
        );
        t2.setName("T2");
        
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        
        EntityManager em = startTransaction();
        print(em.find(Employee.class, filip.getId()));
        commitTransaction(em);
    }
    
    private synchronized void print(Object o) {
        System.out.println(o);
    }

    private static void sleep(int miliseconds){
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
