package com.logdownloader.controller;

import com.logdownloader.model.Credential;
import com.logdownloader.service.CredentialService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/credentials")
public class CredentialController {

    private final CredentialService credentialService;

    public CredentialController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @PostMapping
    public Credential createCredential(@RequestBody Credential credential) {
        return credentialService.createCredential(credential);
    }

    @GetMapping
    public List<Credential> getCredentials() {
        return credentialService.getCredentials();
    }
}