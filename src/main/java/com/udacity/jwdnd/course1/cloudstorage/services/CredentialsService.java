package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialsService {
    private final EncryptionService encryptionService;
    private final CredentialsMapper credentialsMapper;

    public CredentialsService(CredentialsMapper credentialsMapper, EncryptionService encryptionService) {
        this.credentialsMapper = credentialsMapper;
        this.encryptionService = encryptionService;
    }

    public List<Credentials> getUserCredentials(int userId) {
        return credentialsMapper.getUserCredentials(userId);
    }

    public Credentials getCredentialsById(Integer credentialId) {
        return credentialsMapper.getCredentialsById(credentialId);
    }

    public void addCredentials(Credentials credentials, int userId) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credentials.getPassword(), encodedKey);

        Credentials newCredentials = new Credentials();
        newCredentials.setUrl(credentials.getUrl());
        newCredentials.setKey(credentials.getKey());
        newCredentials.setUsername(credentials.getUsername());
        newCredentials.setPassword(encryptedPassword);
        newCredentials.setKey(encodedKey);
        newCredentials.setUserId(userId);

        credentialsMapper.addCredentials(newCredentials);
    }

    public int deleteCredentials (Integer credentialId) {
        return credentialsMapper.deleteCredentials(credentialId);
    }

    public void updateCredentials (Credentials credentials) {
        Credentials storedCredentials = credentialsMapper.getCredentialsById(credentials.getCredentialId());

        credentials.setKey(storedCredentials.getKey());
        String encryptedPassword = encryptionService.encryptValue(credentials.getPassword(), credentials.getKey());
        credentials.setPassword(encryptedPassword);

        credentialsMapper.updateCredentials(credentials);
    }

}
