package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.example.entity.Passport;
import org.example.entity.Person;
import org.example.persistence.PersistenceUnitInfo;
import org.hibernate.jpa.HibernatePersistenceProvider;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        Map<String,String> props = new HashMap<>();

        props.put("hibernate.show_sql","true");
        props.put("hibernate.hbm2ddl.auto","create");


//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit");
        EntityManagerFactory emf = new HibernatePersistenceProvider()
                .createContainerEntityManagerFactory(new PersistenceUnitInfo(),props);
        EntityManager entityManager = emf.createEntityManager();//represent the context
        try {
            entityManager.getTransaction().begin();

            Passport passport = new Passport();
            passport.setNumber("1223322");
            Person person = new Person();
            person.setName("Pasindu");
            person.setPassport(passport);
            passport.setPerson(person);
//            entityManager.persist(passport);
            entityManager.persist(person);

            TypedQuery<Person> query = entityManager.createQuery("select p from Person p where p.passport.number= :number", Person.class);
            query.setParameter("number","1223322");

            System.out.println(query.getResultList());

            entityManager.getTransaction().commit();
        }finally {
            entityManager.close();
        }
    }
}