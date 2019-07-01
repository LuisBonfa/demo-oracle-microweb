package com.ultraschemer.microweb.persistence;

import io.vertx.core.json.Json;
import org.hibernate.Session;

import javax.persistence.MappedSuperclass;
import javax.persistence.PersistenceException;
import javax.persistence.PostPersist;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

/**
 * This class performs automatic logging on all actions on database entities.
 */
@MappedSuperclass
public abstract class Loggable implements Serializable {
    private byte[] getIdAsByte(UUID uuid)
    {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    @SuppressWarnings("unused")
    @PostPersist
    public void logEntity() {
        try(Session session = EntityUtil.openTransactionSession()) {
            session.doWork(connection -> {
                String logEntityStr =
                        "insert into entity_history(id, entity_name, entity_id, entity_data)" +
                        "values ((select max(id)+1 from entity_history), ?, ?, ?)";
                try(PreparedStatement stmt = connection.prepareStatement(logEntityStr)) {
                    stmt.setString(1, this.getClass().getCanonicalName());

                    if(this instanceof Identifiable) {
                        stmt.setBytes(2, getIdAsByte(((Identifiable)this).getId()));
                    } else {
                        stmt.setBytes(2, getIdAsByte(UUID.randomUUID()));
                    }

                    stmt.setString(3, Json.encode(this));
                    stmt.executeUpdate();
                    connection.commit();
                } catch(SQLException se) {
                    // Just print stack trace - to show the developer an error occurred.
                    // No other error handling is expected.
                    se.printStackTrace();
                }
            });
        } catch(PersistenceException pe) {
            // Just print stack trace - to show the developer an error occurred.
            // No other error handling is expected.
            pe.printStackTrace();
        }
    }}
