package com.dbs.escaperoom.repositories;

import com.dbs.escaperoom.models.RoomRegistration;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Daan on 17-Oct-17.
 */
public interface RoomRegistrationRepository extends CrudRepository<RoomRegistration, String> {

    @Query("SELECT t FROM RoomRegistration t WHERE t.unlockKey = ?1")
    RoomRegistration findRegistrationByUnlockKey(String UnlockKey);

    @Query("SELECT t FROM RoomRegistration t WHERE t.secret = ?1")
    RoomRegistration findRegistrationBySecret(String secret);

    @Query("SELECT  rr FROM RoomRegistration rr WHERE rr.lastHeartbeat < ?1 AND rr.online = true")
    List<RoomRegistration> getWhereHeartbeatDue(Date dueTime);
}
