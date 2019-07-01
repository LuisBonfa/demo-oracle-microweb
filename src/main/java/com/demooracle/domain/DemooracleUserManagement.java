package com.demooracle.domain;

import com.demooracle.controller.bean.CreateUserData;
import com.demooracle.domain.error.SimpleUserRegistrationException;
import com.demooracle.domain.error.UserRoleSetException;
import com.ultraschemer.microweb.entity.Role;
import com.ultraschemer.microweb.entity.User;
import com.ultraschemer.microweb.entity.User_Role;
import com.ultraschemer.microweb.error.StandardException;
import com.ultraschemer.microweb.persistence.EntityUtil;
import com.ultraschemer.microweb.utils.Security;
import com.ultraschemer.microweb.validation.Validator;
import org.hibernate.Session;

import javax.persistence.PersistenceException;
import java.util.UUID;

public class DemooracleUserManagement {
    public static void registerSimpleUser(CreateUserData u, String roleName) throws StandardException {
        Validator.ensure(u);

        try(Session session = EntityUtil.openTransactionSession()) {
            User newU = new User();
            User_Role newUR = new User_Role();
            Role role = session.createQuery("from Role where name = :name", Role.class)
                    .setParameter("name", roleName).getSingleResult();

            // Create new user:
            newU.setName(u.getName());
            newU.setAlias(u.getAlias());
            newU.setPassword(Security.hashade(u.getPassword()));
            newU.setStatus("new");
            session.persist(newU);

            // Link the user to his/her role:
            newUR.setUserId(newU.getId());
            newUR.setRoleId(role.getId());
            session.persist(newUR);

            // PErsist data in database:
            session.getTransaction().commit();

        } catch(Exception pe) {
            throw new SimpleUserRegistrationException("Unable to register user: " + pe.getLocalizedMessage());
        }
    }

    public static void setRoleToUser(String strUserId, String roleName) throws StandardException {
        try(Session session = EntityUtil.openTransactionSession()) {
            User_Role newUR = new User_Role();

            Role role = session.createQuery("from Role where name = :name", Role.class)
                    .setParameter("name", roleName).getSingleResult();

            newUR.setUserId(UUID.fromString(strUserId));
            newUR.setRoleId(role.getId());

            session.persist(newUR);
            session.getTransaction().commit();
        } catch(PersistenceException pe) {
            throw new UserRoleSetException("Unable to set role to user: " + pe.getLocalizedMessage());
        }
    }
}
