package com.dbs.escaperoom.controllers;

import com.dbs.escaperoom.models.RoomRegistration;
import com.dbs.escaperoom.repositories.RoomRegistrationRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

@RequestMapping(value = "/**",produces = MediaType.ALL_VALUE)
@Controller
public class ViewController {



    @Autowired
    private RoomRegistrationRepository roomRepository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<byte[]> getIndex(HttpServletRequest request){
        try {
            ClassPathResource resource = new ClassPathResource("/static"+request.getServletPath());
            InputStream sr;
            if (resource.exists()&&resource.isReadable()&&request.getServletPath().length()>1) {
                sr = (resource.getInputStream());
            }
            else
                sr = (new ClassPathResource("/static/index.html").getInputStream());

            byte[] bytes = IOUtils.toByteArray(sr);
            HttpHeaders headers = new HttpHeaders();

            headers.setCacheControl(CacheControl.noCache().getHeaderValue());

            ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(bytes, headers, HttpStatus.OK);
            return responseEntity;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(new byte[0], headers, HttpStatus.OK);
        return responseEntity;
    }

    @RequestMapping(method = RequestMethod.GET,path="/view/register", produces = MediaType.TEXT_HTML_VALUE)
    public String getRegisterPage(Model model){
        Iterable<RoomRegistration> rooms = roomRepository.findAll();
        model.addAttribute("rooms",rooms);
        return "register";

    }
    @RequestMapping(method = RequestMethod.GET,path="/view/enter/{userUuid}", produces = MediaType.TEXT_HTML_VALUE)
    public String enterRoom(@PathVariable String userUuid,Model model){
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
        Iterable<RoomRegistration> rooms = roomRepository.getOnline(true);
        model.addAttribute("rooms",rooms);
        model.addAttribute("uuid",userUuid);
        ArrayList<String> completedRooms = new ArrayList<String>();
        for(RoomRegistration rr : rooms){
            if (rr.getCompletedPlayersUUIDS()!=null)
            if (rr.getCompletedPlayersUUIDS().contains(userUuid)){
                completedRooms.add(rr.getUuid());
            }
        }
        model.addAttribute("completedRooms",completedRooms.toArray());
        return "mainroom";

    }
}
