package appmanaged;

import org.hibernate.SessionFactory;
import org.hibernate.stat.SecondLevelCacheStatistics;
import org.hibernate.stat.Statistics;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

import javax.persistence.CacheRetrieveMode;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import cz.nguyen.entity.Building;
import cz.nguyen.entity.Company;

public class SecondLevelCache {
    private static EntityManagerFactory emf;
    
    @BeforeClass 
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("default");
    }
      
    
    @Test
    public void findCacheable(){
        Company redHat = new Company();
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(redHat);
        em.getTransaction().commit();
        em.close();
        
        System.out.println("First find");
        em = emf.createEntityManager();
        Company c = em.find(Company.class, redHat.getId());
        System.out.println(System.identityHashCode(c));
        em.close();
          
        System.out.println("Second find");
        em = emf.createEntityManager();
        c = em.find(Company.class, redHat.getId());
        System.out.println(System.identityHashCode(c));
        em.close();
        
        emf.getCache().evictAll();
        
        System.out.println("Third find");
        em = emf.createEntityManager();
        em.find(Company.class, redHat.getId());
        em.close();        
        
        printStats();
    }
    
    @Test
    public void bypassCache(){
        Company redHat = new Company();
        EntityManager em = emf.createEntityManager();
//        em.setProperty("javax.persistence.cache.storeMode",  CacheStoreMode.BYPASS);
        em.getTransaction().begin();
        em.persist(redHat);
        em.getTransaction().commit();
        em.close();
        
        System.out.println("First find");
        em = emf.createEntityManager();
        em.find(Company.class, redHat.getId());
        em.close();
          
        System.out.println("About to find second time");
        em = emf.createEntityManager();
        
        em.setProperty("javax.persistence.cache.retrieveMode",  CacheRetrieveMode.BYPASS);
        em.find(Company.class, redHat.getId());
        em.close();
        
    }
    
    private void printStats() {
        Statistics statistics = emf.unwrap(SessionFactory.class).getStatistics();
        System.out.println("Hit Count: "+ statistics.getSecondLevelCacheHitCount());
        System.out.println("Miss Count: "+ statistics.getSecondLevelCacheMissCount());
        System.out.println(Arrays.asList(statistics.getSecondLevelCacheRegionNames()));
    }


    @Test
    public void findNonCacheable(){
        Building b1 = new Building();
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(b1);
        em.getTransaction().commit();
        em.close();
        
        System.out.println("First find");
        em = emf.createEntityManager();
        em.find(Building.class, b1.getId());
        em.close();
          
        System.out.println("About to find second time");
        em = emf.createEntityManager();
        em.find(Building.class, b1.getId());
        em.close();
    }
    
}
