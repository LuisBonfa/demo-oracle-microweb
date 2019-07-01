package com.demooracle.domain;

import com.ultraschemer.microweb.entity.Role;
import com.ultraschemer.microweb.persistence.EntityUtil;
import org.hibernate.Session;

import javax.persistence.PersistenceException;

public class RoleManagement {
    public static void initializeDefault() {
        try(Session session = EntityUtil.openTransactionSession()) {
            Role role = new Role();
            role.setName("user");
            session.persist(role);
            session.getTransaction().commit();
        } catch (PersistenceException pe) {
            System.out.println("Default role already registered. Continuing...");
        }
        try(Session session = EntityUtil.openTransactionSession()) {
            Role role = new Role();
            role.setName("user-manager");
            session.persist(role);
            session.getTransaction().commit();
        } catch (PersistenceException pe) {
            System.out.println("Default role already registered. Continuing...");
        }
        try(Session session = EntityUtil.openTransactionSession()) {
            Role role = new Role();
            role.setName("system-manager");
            session.persist(role);
            session.getTransaction().commit();
        } catch (PersistenceException pe) {
            System.out.println("Default role already registered. Continuing...");
        }
    }
}
