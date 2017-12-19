package com.dbs.escaperoom.controllers;

import com.dbs.escaperoom.models.RoomRegistration;
import com.dbs.escaperoom.models.SuccessMessage;
import com.dbs.escaperoom.repositories.RoomRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;


@RequestMapping(value = "/rooms",produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class MainRoomController {

    @Autowired
    private RoomRegistrationRepository roomRepository;

    @RequestMapping(method = RequestMethod.POST,path="/playercompleted/{unlockkey}/{userUuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean completeRoom(@PathVariable String unlockkey, @PathVariable String userUuid){
        RoomRegistration rr = roomRepository.findRegistrationByUnlockKey(unlockkey);
        rr.playerCompletedRoom(userUuid);
        roomRepository.save(rr);
        return true;
    }

    @RequestMapping(method = RequestMethod.GET,path="/iscompleted/{unlockkey}/{userUuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean getRoomCompleted(@PathVariable String unlockkey, @PathVariable String userUuid){
        RoomRegistration rr = roomRepository.findRegistrationByUnlockKey(unlockkey);
        if (rr.getCompletedPlayersUUIDS().contains(userUuid))
            return true;
        else return false;
    }
}
