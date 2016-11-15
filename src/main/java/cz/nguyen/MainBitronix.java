package cz.nguyen;

import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

import bitronix.tm.TransactionManagerServices;
import cz.nguyen.entity.Employee;

public class MainBitronix {
	private static EntityManagerFactory emf;

	public static void main(String[] args) throws Exception {
		TransactionManagerServices.getConfiguration().setResourceConfigurationFilename("src/main/resources/resourceconfig.cfg");
		TransactionManagerServices.getTransactionManager();
		
		InitialContext context = new InitialContext();
		UserTransaction utx = (UserTransaction) context.lookup("java:comp/UserTransaction");
		context.lookup("d");
		/**
		 * TODO debug this. How is it possible that exporter cannot run in distributed TX?
		 * How is it possible that bitronix can actually disallow it locally for thi sparticular case?
		 * How this works omg! :-D
		 */
		emf = Persistence.createEntityManagerFactory("bitro");
//		TransactionManagerServices.getTransactionManager()
		utx.begin();
		EntityManager em = emf.createEntityManager();
		Employee p = new Employee();
		em.persist(p);
		utx.commit();
//		System.out.println(em.find(Product.class, p.getId()));
//		
//		em.close();
//		emf.close();
	}
}
