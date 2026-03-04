package com.logdownloader.controller;

import com.logdownloader.model.Server;
import com.logdownloader.service.ServerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servers")
public class ServerController {

    private final ServerService serverService;

    public ServerController(ServerService serverService) {
        this.serverService = serverService;
    }

    @PostMapping
    public Server createServer(@RequestBody Server server) {
        return serverService.createServer(server);
    }

    @GetMapping
    public List<Server> getServers() {
        return serverService.getAllServers();
    }

}