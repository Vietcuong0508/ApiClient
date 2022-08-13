package com.example.apiwcrudclient.controller;

import com.example.apiwcrudclient.model.TblClass;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class ClassController {
    private final String REST_API_LIST = "http://localhost:8080/class";
    private final String REST_API_CREATE = "http://localhost:8080/createClass";
    private final String REST_API_DELETE = "http://localhost:8080/deleteClass/";
    private final String REST_API_UPDATE = "http://localhost:8080/updateC/";
    private final String REST_API_GET_ID = "";

    private static Client createJerseyRestClient() {
        ClientConfig clientConfig = new ClientConfig();

        // Config logging for client side
        clientConfig.register( //
                new LoggingFeature( //
                        Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME), //
                        Level.INFO, //
                        LoggingFeature.Verbosity.PAYLOAD_ANY, //
                        10000));

        return ClientBuilder.newClient(clientConfig);
    }

    @GetMapping(value = {"/listClass"})
    public String index(Model model) {
        Client client = createJerseyRestClient();
        WebTarget target = client.target(REST_API_LIST);
        List<TblClass> classes =  target.request(MediaType.APPLICATION_JSON_TYPE).get(List.class);
        model.addAttribute("list", classes);
        return "listClass";
    }

    @RequestMapping(value = "createClass")
    public String createUser() {
        return "createClass";
    }

    @RequestMapping(value = "saveClass")
    public String saveClass(@RequestParam String name, @RequestParam String note, @RequestParam String room) {
        TblClass aClass = new TblClass();
        aClass.setName(name);
        aClass.setNote(note);
        aClass.setRoom(room);

        String jsonUser = convertToJson(aClass);
        Client client = createJerseyRestClient();
        WebTarget target = client.target(REST_API_CREATE);
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(jsonUser, MediaType.APPLICATION_JSON));
        return "redirect:/listClass";
    }

    @RequestMapping(value = "deleteClass")
    public String deleteClass(Integer id) {
        Client client = createJerseyRestClient();
        WebTarget target = client.target(REST_API_DELETE + id);
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE).delete();
        return "redirect:/listClass";
    }

    private static String convertToJson(TblClass tblClass) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(tblClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
