package main;

import javax.persistence.*;

public class Main {
	
	private static EntityManagerFactory factory;

	public static void main(String[] args) {
		System.out.println("Hello World!");
		
		factory = Persistence.createEntityManagerFactory("DomainModelExample");
		EntityManager em = factory.createEntityManager();
		
		em.getTransaction().begin();
		

//		em.persist(turma);
//		em.getTransaction().commit();
		em.close(); 

	}

}
