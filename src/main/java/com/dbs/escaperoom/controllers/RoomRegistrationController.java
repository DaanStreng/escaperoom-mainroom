package com.dbs.escaperoom.controllers;
import com.dbs.escaperoom.models.ExceptionMessage;
import com.dbs.escaperoom.models.RoomRegistration;
import com.dbs.escaperoom.models.SuccessMessage;
import com.dbs.escaperoom.monitoring.HeartbeatMonitor;
import com.dbs.escaperoom.repositories.RoomRegistrationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.PathParam;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Daan on 11-Oct-17.
 */
@RequestMapping(value = "/registration",produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class RoomRegistrationController  {

    @Autowired
    private RoomRegistrationRepository roomRepository;

    @RequestMapping(method = RequestMethod.POST,path = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public RoomRegistration registerRoom(@RequestParam String endpoint, @RequestParam String secret, @RequestParam String name){
        try{
            URL u = new URL(endpoint);
            u.toURI();
            if (!endpoint.contains(".")){
                throw new IllegalArgumentException();
            }
        }catch(Exception ex){
            throw new IllegalArgumentException("Thats not a valid url");
        }
        RoomRegistration rr = roomRepository.findRegistrationBySecret(secret);
        if (rr == null){
            rr = new RoomRegistration(endpoint,secret);
            rr.setName(name);
        }

        roomRepository.save(rr);
        return rr;
    }

    @RequestMapping(method = RequestMethod.POST,path="/removeroom")
    public SuccessMessage removeRoom(@RequestParam String secret){
        RoomRegistration rr = this.getRegistrationBySecret(secret);
        roomRepository.delete(rr);
        return new SuccessMessage();
    }

    @RequestMapping(method = RequestMethod.POST,path="/setoffline")
    public SuccessMessage setOffline(@RequestParam String secret){
        RoomRegistration rr = this.getRegistrationBySecret(secret);
        rr.setOnline(false);
        roomRepository.save(rr);
        return new SuccessMessage();
    }
    @RequestMapping(method = RequestMethod.POST,path="/setonline")
    public SuccessMessage setOnline(@RequestParam String secret){
        RoomRegistration rr = this.getRegistrationBySecret(secret);
        rr.setOnline(true);
        roomRepository.save(rr);
        return new SuccessMessage();
    }

    @RequestMapping(method = RequestMethod.POST,path="/linkroom")
    public SuccessMessage linkRoom(@RequestParam String secret, @RequestParam String uuid){
        RoomRegistration rr = this.getRegistrationBySecret(secret);
        RoomRegistration r2 = roomRepository.findOne(uuid);
        if (r2 == null){
            throw new IllegalArgumentException("no room for uuid");
        }
        rr.setOpensRoom(r2);
        return new SuccessMessage();
    }

    @RequestMapping(method = RequestMethod.POST,path="/delete")
    public SuccessMessage deleteRoom(@RequestParam String secret){
        RoomRegistration rr = this.getRegistrationBySecret(secret);
        roomRepository.delete(rr);
        return new SuccessMessage();
    }

    @RequestMapping(method = RequestMethod.GET,path = "/getbykey/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RoomRegistration getRoomByKey(@PathVariable String key){
        RoomRegistration rr = roomRepository.findRegistrationByUnlockKey(key);
        return rr;
    }

    @RequestMapping(method = RequestMethod.GET,path = "/getbyuuid/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RoomRegistration getRoomByUuid(@PathVariable String uuid){
        RoomRegistration rr = roomRepository.findOne(uuid);
        return rr;
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List handleException(IllegalArgumentException ex){
        return Arrays.asList(new ExceptionMessage(ex));
    }

    private RoomRegistration getRegistrationBySecret(String secret){
        try{
            RoomRegistration rr = roomRepository.findRegistrationBySecret(secret);
            if (rr == null){
                throw new IllegalArgumentException("invalid secret");
            }
            return rr;
        }catch(Exception ex){
            throw new IllegalArgumentException("invalid secret");
        }
    }
}
