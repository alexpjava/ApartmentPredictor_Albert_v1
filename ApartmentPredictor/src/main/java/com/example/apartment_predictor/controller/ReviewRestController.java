package com.example.apartment_predictor.controller;

import com.example.apartment_predictor.model.Review;
import com.example.apartment_predictor.model.Reviewer;
import com.example.apartment_predictor.model.School;
import com.example.apartment_predictor.utils.PopulateDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/review")
public class ReviewRestController {

    @Autowired
    PopulateDB populateDB;

    // Create qty reviews, not populateDB
    @GetMapping("/create")
    public ResponseEntity<String> createReviews(@RequestParam int qty) {
        List<Review> reviews = populateDB.createPlainReviews(qty);
        if (reviews.size() > 0)
            return ResponseEntity.ok("Populated reviews: " + reviews.size());
        else
            return ResponseEntity.badRequest().body("Failed to populate reviews");
    }

    @GetMapping("/populate")
    public ResponseEntity<String> populateReviews(@RequestParam int qty) {
        List<Reviewer> reviewers = populateDB.populateReviewers(qty);
        List<Review> reviews = populateDB.createPlainReviews(qty);
        List<Review> reviewsAssigned = populateDB.assignReviewersToReviews(reviewers, reviews );


        if (reviewsAssigned.size() > 0)
            return ResponseEntity.ok("Populated reviews: " + reviewsAssigned.size());
        else
            return ResponseEntity.badRequest().body("Failed to populate reviews");
    }


}
