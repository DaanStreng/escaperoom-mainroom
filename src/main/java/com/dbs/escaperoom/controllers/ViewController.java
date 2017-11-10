package com.dbs.escaperoom.controllers;

import com.dbs.escaperoom.models.RoomRegistration;
import com.dbs.escaperoom.repositories.RoomRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@RequestMapping(value = "/**",produces = MediaType.ALL_VALUE)
@Controller
public class ViewController {

    @Autowired
    private RoomRegistrationRepository roomRepository;

    @RequestMapping(method = RequestMethod.GET)
    public byte[] getIndex(HttpServletRequest request){
        try {
            ClassPathResource resource = new ClassPathResource("/static"+request.getServletPath());
            InputStream sr;
            if (resource.exists()&&resource.isReadable()&&request.getServletPath().length()>1) {
                sr = (resource.getInputStream());
            }
            else
                sr = (new ClassPathResource("/static/index.html").getInputStream());
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int read;
            byte[] input = new byte[4096];
            while ( -1 != ( read = sr.read( input ) ) ) {
                buffer.write( input, 0, read );
            }
            input = buffer.toByteArray();
            return input;
        }
        catch(Exception e){}
        return new byte[0];
    }

    @RequestMapping(method = RequestMethod.GET,path="/view/register", produces = MediaType.TEXT_HTML_VALUE)
    public String getRegisterPage(Model model){
        Iterable<RoomRegistration> rooms = roomRepository.findAll();
        model.addAttribute("rooms",rooms);
        return "register";

    }
}
