package com.example.apartment_predictor.controller;

import com.example.apartment_predictor.model.Apartment;
import com.example.apartment_predictor.model.School;
import com.example.apartment_predictor.repository.SchoolRepository;
import com.example.apartment_predictor.service.ApartmentService;
import com.example.apartment_predictor.utils.PopulateDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("api/apartment")
public class ApartmentRestController {

    @Autowired
    ApartmentService apartmentService;

    @Autowired
    PopulateDB populateDB;

    @Autowired
    SchoolRepository schoolRepository;

    @GetMapping("/getAll")
    public ResponseEntity<Iterable<Apartment>> getAllApartments(){

       HttpHeaders headers = new HttpHeaders();
        headers.add("Status", "getAllApartments executed");
        headers.add("version", "1.0 Api Rest Apartment Object");
        headers.add("active", "true");
        headers.add("author", "Albert");

        return ResponseEntity.ok().headers(headers).body(apartmentService.findAll());
        //return apartmentService.findAll();
    }

    @GetMapping("/getById")
    public Apartment getApartmentById(@RequestParam String id){
        return apartmentService.findApartmentById(id);
    }

    @PostMapping("/create")
    public Apartment createApartment(@RequestBody Apartment apartment){
        //apartment.setId(UUID.randomUUID().toString());
        return apartmentService.createApartment(apartment);
    }

    @PostMapping("/update")
    public Apartment updateApartment(@RequestBody Apartment apartment){
        return apartmentService.updateApartment(apartment);
    }

    @DeleteMapping("/deleteById")
    public void deleteApartmentById(@RequestParam String id){
        apartmentService.deleteApartment(id);
    }

    @PutMapping("/updateById")
    public Apartment updateApartmentById(@RequestParam String id, @RequestBody Apartment apartment){
        return apartmentService.updateApartmentById(id, apartment);
    }

    @GetMapping("/populate")
    public ResponseEntity<String> populateApartments(@RequestParam int qty) {
        // CALL to PopulateDB.populateApartments() method
        // to populate the database with random apartments
        int qtyApartmentsCreated = populateDB.populatePlainApartments(qty).size();
        if (qtyApartmentsCreated > 0)
            return ResponseEntity.ok("Populated apartments: " + qtyApartmentsCreated);
        else
            return ResponseEntity.badRequest().body("Failed to populate apartments");
    }

    @PutMapping("/assignSchoolsToApartment")
    public ResponseEntity<Apartment> assignSchoolsToApartment(
            @RequestParam String apartmentId,
            @RequestParam List<String> schoolIds
    ) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Status", "assignSchoolsToApartment executed");
        headers.add("version", "1.0 Api Rest Apartment Object");
        headers.add("active", "true");
        headers.add("author", "Albert");

        Apartment apartment = apartmentService.findApartmentById(apartmentId);
        if (apartment == null) {
            headers.add("Status", "assignSchoolsToApartment failed: apartment not found");
            return ResponseEntity.
                    badRequest().headers(headers).
                    body(null);
        }

        if (schoolIds == null || schoolIds.isEmpty()) {
            headers.add("Status", "assignSchoolsToApartment failed: schoolIds is null or empty");
            apartment.setSchools(new ArrayList<>());
            return ResponseEntity.badRequest().headers(headers).body(null);
        }

        Iterable<School> found = schoolRepository.findAllById(schoolIds);
        List<School> schoolsFound = StreamSupport.stream(found.spliterator(), false)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (schoolsFound.size() != schoolIds.size()) {
            headers.add("Status", "assignSchoolsToApartment failed: schools not found");
            return ResponseEntity.badRequest().headers(headers).body(null);
        }

        apartment.addSchools(schoolsFound);
        headers.add("Status", "assignSchoolsToApartment success");
        Apartment apartmentSaved = apartmentService.updateApartment(apartment);
        return ResponseEntity.ok().headers(headers).body(apartmentSaved);
    }
    

}
