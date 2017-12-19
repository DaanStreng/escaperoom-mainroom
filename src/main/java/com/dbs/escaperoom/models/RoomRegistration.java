package com.dbs.escaperoom.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Daan on 11-Oct-17.
 */
@Entity
public class RoomRegistration {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "VARCHAR(128)")
    private String uuid;

    @Column(unique = true)
    private String name;

    @JsonIgnore
    @Column(columnDefinition = "VARCHAR(128)",unique = true)
    private String secret;

    @Column(columnDefinition = "VARCHAR(128)")
    private String unlockKey = UUID.randomUUID().toString();

    private Date lastHeartbeat;

    private boolean online;

    @OneToOne
    private RoomRegistration opensRoom;

    private String endPoint;

    private ArrayList<String> completedPlayersUUIDS;

    public String getUuid() {
        return uuid;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }
    public void setEndPoint(String endPoint, RoomRegistration opensRoom) {
        this.endPoint = endPoint;
        this.opensRoom = opensRoom;
    }


    protected RoomRegistration(){}

    public RoomRegistration(String endpoint,String secret){
        this.endPoint = endpoint;
        this.secret = secret;
        this.setLastHeartbeat(new Date());
        this.completedPlayersUUIDS = new ArrayList<String>();
    }

    public String getUnlockKey() {
        return unlockKey;
    }

    public RoomRegistration getOpensRoom() {
        return opensRoom;
    }

    public void setOpensRoom(RoomRegistration opensRoom) {
        this.opensRoom = opensRoom;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public Date getLastHeartbeat() {
        return lastHeartbeat;
    }

    public void setLastHeartbeat(Date lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getCompletedPlayersUUIDS() {
        return completedPlayersUUIDS;
    }

    public void setCompletedPlayersUUIDS(ArrayList<String> completedPlayersUUIDS) {
        this.completedPlayersUUIDS = completedPlayersUUIDS;
    }
    public void playerCompletedRoom(String playerUUID){
        if (this.completedPlayersUUIDS == null)
            this.completedPlayersUUIDS = new ArrayList<String>();
        this.completedPlayersUUIDS.add(playerUUID);
    }
}
