package com.dbs.escaperoom.controllers;

import com.dbs.escaperoom.models.RoomRegistration;
import com.dbs.escaperoom.models.SuccessMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;


@Controller
public class MainRoomController {
    @RequestMapping(method = RequestMethod.GET,path="/view/enter/{userUuid}", produces = MediaType.TEXT_HTML_VALUE)
    public String removeRoom(@PathVariable String userUuid,Model model){
        /*try {
            ClassPathResource resource = new ClassPathResource("/static/mainview.html");
            InputStream sr = resource.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int read;
            byte[] input = new byte[4096];
            while ( -1 != ( read = sr.read( input ) ) ) {
                buffer.write( input, 0, read );
            }
            input = buffer.toByteArray();
            return new String(input);
        }
        catch(Exception e){}
        return "";*/
        model.addAttribute("uuid",userUuid);
        return "mainroom";

    }
}
