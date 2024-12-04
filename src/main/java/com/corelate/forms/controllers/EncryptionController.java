package com.corelate.forms.controllers;

import com.corelate.forms.service.impl.EncryptDecryptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Seth Hernandez
 */

@RestController
public class EncryptionController {
    @Autowired

    EncryptDecryptService encryptDecryptService;

    @GetMapping("/createkeys")
    public void createKeys(){
        encryptDecryptService.createKeys();
    }

    @PostMapping("/encrypt")
    public void encryptMessage(@RequestBody String text){
        encryptDecryptService.encryptMessage(text);
    }

    @PostMapping("/decrypt")
    public void decryptMessage(@RequestBody String text){
        encryptDecryptService.decryptMessage(text);
    }
}
