package com.logdownloader.service;

import com.logdownloader.model.Server;
import com.logdownloader.repository.ServerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServerService {

    private final ServerRepository serverRepository;

    public ServerService(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    public Server createServer(Server server) {
        return serverRepository.save(server);
    }

    public List<Server> getAllServers() {
        return serverRepository.findAll();
    }

}