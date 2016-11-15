package appmanaged;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import cz.nguyen.entity.Building;
import cz.nguyen.entity.Company;
import cz.nguyen.entity.Employee;

public class JpaNplusOne {
    private static EntityManagerFactory emf;
    
    private static Company microsoft = new Company("Microsoft");
    private static Company redhat = new Company("Red Hat");
    private static Company linkedin = new Company("LinkedIn");
    private static Company google = new Company("Google");
    private static Company lehman = new Company("Lehman Brothers");
    
    private static Employee filip = new Employee("Filip");
    private static Employee mike = new Employee("Michael");
    private static Employee gates = new Employee("Gates");
    private static Employee nadela = new Employee("Nadela");
    private static Employee weiner = new Employee("Weiner");
    private static Employee pichai = new Employee("Pichai");
    private static Employee page = new Employee("Larry Page");
    
    private static Building m1 = new Building("m1");
    private static Building m2 = new Building("m2");
    private static Building m3 = new Building("m3");
    private static Building r1 = new Building("r1");
    private static Building r2 = new Building("r2");
    private static Building r3 = new Building("r3");
    
    @BeforeClass 
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        
        
        em.persist(microsoft);
        em.persist(redhat);
        em.persist(linkedin);
        em.persist(google);
        em.persist(lehman);
        
        em.persist(filip);
        em.persist(mike);
        em.persist(gates);
        em.persist(nadela);
        em.persist(weiner);
        em.persist(pichai);
        em.persist(page);
        

        em.persist(m1);
        em.persist(m2);
        em.persist(m3);
        em.persist(r1);
        em.persist(r2);
        em.persist(r3);
//        
//        microsoft.addBuilding(m1);
//        microsoft.addBuilding(m2);
//        microsoft.addBuilding(m3);
//        redhat.addBuilding(r1);
//        redhat.addBuilding(r2);
//        redhat.addBuilding(r3);
        
        microsoft.addEmployee(gates);
        microsoft.addEmployee(nadela);
        
        redhat.addEmployee(filip);
        redhat.addEmployee(mike);
        
        linkedin.addEmployee(weiner);
        
        google.addEmployee(pichai);
        google.addEmployee(page);
        em.getTransaction().commit();
        em.close();
        System.out.println("\n\n***** DATA LOADED **********\n\n\n");
    }
    
    @Test
    public void findAll() {
        EntityManager em = startTransaction();
        List<Company> companies = em
            .createQuery("SELECT c FROM Company c WHERE c.name LIKE '%o%'", 
                Company.class)
            .getResultList();
        System.out.println(companies);
        commitTransaction(em);
    }
    
    @Test
    public void findAllCrit() {
        EntityManager em = startTransaction();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Company> query = cb.createQuery(Company.class);
        Root<Company> c = query.from(Company.class); // FROM Company c
        query.select(c); // SELECT c
        query.where(cb.like(c.<String>get("name"), "%o%"));//WHERE c.name LIKE '%o%'
        List<Company> companies = em.createQuery(query).getResultList();
        System.out.println(companies);
        commitTransaction(em);
    }

    
    @Test
    public void subselectFilipCompany() {
        EntityManager em = startTransaction();
        List<Company> companies = em
            .createQuery("SELECT c FROM Company c " +
                "LEFT JOIN FETCH c.employees " +
                "WHERE EXISTS (SELECT e FROM Employee e WHERE e.company = c and e.name = 'Filip')", 
                Company.class)
            .getResultList();
        System.out.println(companies);
        System.out.println(companies.get(0).getEmployees());
        commitTransaction(em);
    }
    
    @Test
    public void subselectFilipCompanyCrit() {
        EntityManager em = startTransaction();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Company> query = cb.createQuery(Company.class);
        Subquery<Employee> equery = query.subquery(Employee.class);
        Root<Employee> e = equery.from(Employee.class); // FROM Employee e
        Root<Company> c = query.from(Company.class); // FROM Company c
        equery.select(e);
        equery.where( // WHERE e.company = c and e.name = 'Filip'
            cb.and(
              cb.equal(e.<Company>get("company"), c), 
              cb.equal(e.<String>get("name"), "Filip")));
        
        query.select(c); 
        c.fetch("employees", JoinType.LEFT);//LEFT JOIN FETCH c.employees 
        query.where(cb.exists(equery)); //EXISTS...
        
        List<Company> companies = em.createQuery(query).getResultList();
        System.out.println(companies);
        System.out.println(companies.get(0).getEmployees());
        commitTransaction(em);
    }
    
    @Test
    public void subselectFilipCompanyJoin() {
        EntityManager em = startTransaction();
        List<Company> companies = em
            .createQuery("SELECT e.company FROM Employee e " +
                "LEFT JOIN FETCH e.company.employees " +
                "WHERE e.name = 'Filip'", 
                Company.class)
            .getResultList();
        System.out.println(companies);
        System.out.println(companies.get(0).getEmployees());
        commitTransaction(em);
    }

    @Test
    public void selectNew() {
        EntityManager em = startTransaction();
        List<CompanyEmployeePair> companies = em
            .createQuery("SELECT new appmanaged.CompanyEmployeePair(c, e) FROM Company c, Employee e",
                CompanyEmployeePair.class).getResultList();
        for (CompanyEmployeePair p : companies)
            System.out.println(p);
        commitTransaction(em);
    }

    @Test
    public void nPlusOne() {
        EntityManager em = startTransaction();
        List<Company> companies = em
            .createQuery("SELECT c FROM Company c" 
//                + " WHERE c.name like '%o%'" 
                , Company.class)
            .getResultList();
        System.out.println("**** QUERY DONE ******");
        for (Company c : companies) {
            System.out.println("Printing Employees");
            System.out.println("********  "  + c.getName()+" employees: "+ c.getEmployees());
        }
        commitTransaction(em);
    }
    

    @Test
    public void fetchJoinMultiple() {
        EntityManager em = startTransaction();
        List<Company> companies = em
            .createQuery("SELECT c FROM Company c" 
//                + " LEFT JOIN FETCH c.employees "
//                + " LEFT JOIN FETCH c.buildings "
                , Company.class)
            .getResultList();
        System.out.println("**** QUERY DONE ******");
        
        System.out.println("Result size: "+ companies.size());
        for (Company c : companies) {
            System.out.println("Printing Employees");
            System.out.println("********  "  + c.getName()+" employees: "+ c.getEmployees());
        }
        commitTransaction(em);
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
}
