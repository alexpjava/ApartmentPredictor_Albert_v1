package com.example.apartment_predictor.controller;

import com.example.apartment_predictor.model.Owner;
import com.example.apartment_predictor.model.School;
import com.example.apartment_predictor.repository.OwnerRepository;
import com.example.apartment_predictor.utils.PopulateDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/owner")
public class OwnerRestController {

    @Autowired
    OwnerRepository ownerRepository;
    @Autowired
    PopulateDB populateDB;

    @GetMapping("/getAll")
    public ResponseEntity<Iterable<Owner>> getAllOwners(){
        return ResponseEntity.ok().body(ownerRepository.findAll());
    }

    @GetMapping("/populate")
    public ResponseEntity<String> populateOwners(@RequestParam int qty) {
        List<Owner> owners = populateDB.populateOwners(qty);
        if (owners.size() > 0)
            return ResponseEntity.ok("Populated owners: " + owners.size());
        else
            return ResponseEntity.badRequest().body("Failed to populate owners");
    }


}
