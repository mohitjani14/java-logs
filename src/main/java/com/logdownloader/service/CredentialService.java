package com.logdownloader.service;

import com.logdownloader.model.Credential;
import com.logdownloader.repository.CredentialRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {

    private final CredentialRepository credentialRepository;

    public CredentialService(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    public Credential createCredential(Credential credential) {
        return credentialRepository.save(credential);
    }

    public List<Credential> getCredentials() {
        return credentialRepository.findAll();
    }
}