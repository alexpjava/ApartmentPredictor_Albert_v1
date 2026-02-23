package com.example.apartment_predictor.utils;

import com.example.apartment_predictor.model.*;
import com.example.apartment_predictor.repository.ReviewRepository;
import com.example.apartment_predictor.repository.ReviewerRepository;
import com.example.apartment_predictor.repository.SchoolRepository;
import com.example.apartment_predictor.service.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class PopulateDB {

    @Autowired
    ApartmentService apartmentService;
    @Autowired
    SchoolRepository schoolRepository;
    @Autowired
    ReviewerRepository reviewerRepository;
    @Autowired
    ReviewRepository reviewRepository;


    //todo: REFACTOR > all methods MUST return the objects created
    //todo: define our pattern, orchestrator
    //todo: define steps for our orchestrator > populateAll()

    // --------- ORCHESTRATOR ------------------------------------------------
    public int populateAll(int qty) {

        // 1 populate Apartments > List
        List<Apartment> plainApartments = populatePlainApartments(qty);
        // 2 populate Schools > List
        List<School> schools = populateSchools(qty);
        // 3 assignSchoolsToApartments
        List<Apartment> plainApartmentsWithSchools =
                assignSchoolsToApartments(plainApartments, schools);


        // 4 populate Reviewers > List
        List<Reviewer> reviewers = populateReviewers(qty);
        // 5 create Reviews (very general description, valid for all apartments) and assign Reviewers
        // DO NOT SAVE to db!
        List<Review> plainReviews = createPlainReviews(qty);
        // 6 assign Reviewers to Reviews
        List<Review> reviews = assignReviewersToReviews(reviewers, plainReviews);
        // 7 assign Reviews to Apartments
        List<Apartment> plainApartmentsWithSchoolsAndReviews =
                assignReviewsToApartments(reviews, plainApartmentsWithSchools);


        // 8 populate Owners
        List<Owner> owners = populateOwners(qty);
        // 9 populate PlainPropertyContracts
        List<PropertyContract> plainPropertyContracts = populatePlainPropertyContracts(qty);
        // 10 populate PropertyContracts assign Owners and Apartments
        List<PropertyContract> plainPropertyContractsAssigned =
                assignPropertyContracts(qty, plainPropertyContracts, owners);
        // 11 check and return qty of created objects


        return 0;
    }

    // --------- POPULATE apartments and schools ------------------------------

    public List<School> populateSchools(int qty) {
        int qtySchoolsCreated = 0;
        List<School> schools = new ArrayList<>();
        if (qty <= 0) return null;

        ThreadLocalRandom rnd = ThreadLocalRandom.current();

        String[] schoolTypes = {"public", "private", "religious"};
        String[] locations = {"Downtown", "Uptown", "Suburbs", "East Side", "West Side"};
        String[] namePrefixes = {"Green", "Oak", "River", "Hill", "Sunrise", "Cedar", "Lakeside"};
        String[] nameSuffixes = {"Academy", "School", "Institute", "High School", "College"};

        for (int i = 0; i < qty; i++) {
            String type = schoolTypes[rnd.nextInt(schoolTypes.length)];
            String location = locations[rnd.nextInt(locations.length)];
            int rating = rnd.nextInt(1, 6);
            boolean isPublic = "public".equals(type);

            String name = namePrefixes[rnd.nextInt(namePrefixes.length)] + " " + nameSuffixes[rnd.nextInt(nameSuffixes.length)];

            School school = new School(name, type, location, rating, isPublic);
            schoolRepository.save(school);

            School schoolById = schoolRepository.findById(school.getId()).orElse(null);
            if (schoolById != null) {
                qtySchoolsCreated++;
                schools.add(schoolById);
                System.out.println(
                        "School #" + qtySchoolsCreated +
                                "/" + qty + " created populateDB: " + schoolById);
            }
        }

        return schools;
    }

    public List<Apartment> populatePlainApartments(int qty) {
        int qtyApartmetnsCreated = 0;
        List<Apartment> apartments = new ArrayList<>();
        if (qty <= 0) return null;

        //Faker faker = new Faker(new Locale("en-US"));
        ThreadLocalRandom rnd = ThreadLocalRandom.current();

        String[] furnishingOptions = {"furnished", "semi-furnished", "unfurnished"};

        for (int i = 0; i < qty; i++) {
            Long price = rnd.nextLong(30_000, 600_001);          // adjust range if you want
            Integer area = rnd.nextInt(300, 5001);              // adjust range if you want
            Integer bedrooms = rnd.nextInt(1, 7);
            Integer bathrooms = rnd.nextInt(1, 5);
            Integer stories = rnd.nextInt(1, 5);

            String mainroad = rnd.nextBoolean() ? "yes" : "no";
            String guestroom = rnd.nextBoolean() ? "yes" : "no";
            String basement = rnd.nextBoolean() ? "yes" : "no";
            String hotwaterheating = rnd.nextBoolean() ? "yes" : "no";
            String airconditioning = rnd.nextBoolean() ? "yes" : "no";
            Integer parking = rnd.nextInt(0, 4);
            String prefarea = rnd.nextBoolean() ? "yes" : "no";

            String furnishingstatus = furnishingOptions[rnd.nextInt(furnishingOptions.length)];

            Apartment apartment = new Apartment(
                    price,
                    area,
                    bedrooms,
                    bathrooms,
                    stories,
                    mainroad,
                    guestroom,
                    basement,
                    hotwaterheating,
                    airconditioning,
                    parking,
                    prefarea,
                    furnishingstatus
            );

            apartmentService.createApartment(apartment);

            Apartment apartmentById = apartmentService.findApartmentById(apartment.getId());
            if (apartmentById != null) {
                qtyApartmetnsCreated++;
                apartments.add(apartmentById);
                System.out.println(
                        "Apartment #" + qtyApartmetnsCreated +
                         "/" + qty + " created populateDB: " + apartmentById);
            }

        }
        return apartments;
    }

    public List<Apartment> assignSchoolsToApartments(List<Apartment> apartments,
                                             List<School> schools) {
        if (apartments == null || apartments.isEmpty() || schools == null || schools.isEmpty()) {
            return null;
        }

        ThreadLocalRandom rnd = ThreadLocalRandom.current();

        for (Apartment apartment : apartments) {
            // Randomly assign 1 to 4 schools
            int numSchoolsToAssign = rnd.nextInt(1, 5); // 1, 2, 3, or 4

            // Don't assign more schools than available
            numSchoolsToAssign = Math.min(numSchoolsToAssign, schools.size());

            // Randomly select schools
            List<School> selectedSchools = new ArrayList<>();
            List<School> availableSchools = new ArrayList<>(schools);

            for (int i = 0; i < numSchoolsToAssign && !availableSchools.isEmpty(); i++) {
                int randomIndex = rnd.nextInt(availableSchools.size());
                School selectedSchool = availableSchools.remove(randomIndex);
                selectedSchools.add(selectedSchool);
            }

            // Assign schools to apartment
            if (!selectedSchools.isEmpty()) {
                apartment.addSchools(selectedSchools);
                apartmentService.updateApartment(apartment);

                System.out.println("Assigned " + selectedSchools.size() + " schools to apartment " + apartment.getId());
            }
        }

        return apartments;
    }

    // ---------- POPULATE reviews, reviewers ------------------------------

    /*public List<Person> populatePeople(int qty){return null;}*/

    public List<Reviewer> populateReviewers(int qty) {
        int qtyReviewersCreated = 0;
        List<Reviewer> reviewers = new ArrayList<>();
        if (qty <= 0) return null;

        ThreadLocalRandom rnd = ThreadLocalRandom.current();

        String[] firstNames = {"John", "Jane", "Michael", "Sarah", "David", "Emily", "Robert", "Lisa", "James", "Mary"};
        String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez"};
        String[] domains = {"gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "example.com"};
        String[] xAccounts = {"john_reviewer", "jane_rates", "mike_reviews", "sarah_says", "david_deals", "emily_evaluates"};
        String[] websites = {"reviewhub.com", "rateit.net", "opinions.io", "feedback.org", "viewsite.com"};

        for (int i = 0; i < qty; i++) {
            String firstName = firstNames[rnd.nextInt(firstNames.length)];
            String lastName = lastNames[rnd.nextInt(lastNames.length)];
            String fullName = firstName + " " + lastName;
            String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@" + domains[rnd.nextInt(domains.length)];
            String password = "password123"; // We must generate random passwords
            boolean isActive = rnd.nextBoolean();
            boolean isBusiness = rnd.nextBoolean();
            String xAccount = xAccounts[rnd.nextInt(xAccounts.length)] + rnd.nextInt(100);
            String webURL = "https://www." + websites[rnd.nextInt(websites.length)] + "/" + firstName.toLowerCase();
            int qtyReviews = rnd.nextInt(0, 51); // 0 to 50 reviews
            LocalDate birthDate = LocalDate.now().minusYears(rnd.nextInt(18, 71)); // 18 to 70 years old

            Reviewer reviewer = new Reviewer(fullName, email, password, birthDate, isActive, isBusiness, xAccount, webURL, qtyReviews);
            reviewerRepository.save(reviewer);

            Reviewer reviewerById = reviewerRepository.findById(reviewer.getId()).orElse(null);
            if (reviewerById != null) {
                qtyReviewersCreated++;
                reviewers.add(reviewerById);
                System.out.println(
                        "Reviewer #" + qtyReviewersCreated +
                                "/" + qty + " created populateDB: " + reviewerById);
            }
        }

        return reviewers;
    }

    public List<Review> createPlainReviews(int qty) {
        int qtyReviewsCreated = 0;
        List<Review> reviews = new ArrayList<>();
        if (qty <= 0) return null;

        ThreadLocalRandom rnd = ThreadLocalRandom.current();

        String[] reviewTitles = {
                "Great place to live!", "Excellent location", "Would recommend", "Amazing apartment",
                "Perfect for families", "Wonderful experience", "Highly recommended", "Fantastic value",
                "Beautiful apartment", "Outstanding service", "Cozy and comfortable", "Spacious and modern"
        };

        String[] reviewContents = {
                "I had an amazing experience living here. The location is perfect and the amenities are great.",
                "This apartment exceeded my expectations. Clean, spacious, and well-maintained.",
                "Great value for money. The neighborhood is safe and convenient.",
                "Excellent communication with management and quick response to issues.",
                "Beautifully designed apartment with modern finishes and plenty of natural light.",
                "Perfect location for commuting. Close to public transportation and shopping.",
                "Spacious rooms and great layout. The building has excellent facilities.",
                "Quiet and peaceful neighborhood. Very satisfied with my stay here.",
                "Well-maintained property with attentive staff. Would definitely recommend.",
                "Great community atmosphere. Friendly neighbors and safe environment.",
                "Modern appliances and updated fixtures. Move-in ready condition.",
                "Excellent property management. Professional and responsive service."
        };

        for (int i = 0; i < qty; i++) {
            String title = reviewTitles[rnd.nextInt(reviewTitles.length)];
            String content = reviewContents[rnd.nextInt(reviewContents.length)];
            int rating = rnd.nextInt(1, 6); // 1 to 5 stars
            LocalDate reviewDate = LocalDate.now().minusDays(rnd.nextInt(0, 365)); // Within the last year

            Review review = new Review(title, content, rating, reviewDate);

            qtyReviewsCreated++;
            reviews.add(review);
            System.out.println(
                    "Review #" + qtyReviewsCreated +
                            "/" + qty + " created populateDB: " + review);
        }

        return reviews;
    }

    public List<Review> assignReviewersToReviews(List<Reviewer> reviewers,
                                                 List<Review> reviews){
        int qtyReviewsAssigned = 0;
        List<Review> assignedReviews = new ArrayList<>();

        if (reviewers == null || reviewers.isEmpty() || reviews == null || reviews.isEmpty()) {
            return null;
        }

        ThreadLocalRandom rnd = ThreadLocalRandom.current();

        for (Review review : reviews) {
            // Randomly select a reviewer
            Reviewer randomReviewer = reviewers.get(rnd.nextInt(reviewers.size()));

            // Assign the reviewer to the review
            review.setReviewer(randomReviewer);

            // Save the review to database
            reviewRepository.save(review);

            // Verify by fetching from database
            Review reviewById = reviewRepository.findById(review.getId()).orElse(null);
            if (reviewById != null) {
                qtyReviewsAssigned++;
                assignedReviews.add(reviewById);
                System.out.println(
                        "Review #" + qtyReviewsAssigned +
                                " assigned to reviewer " + randomReviewer.getFullName() +
                                " and saved to DB: " + reviewById);
            }
        }

        return assignedReviews;
    }

    public List<Apartment> assignReviewsToApartments(List<Review> reviews,
                                                     List<Apartment> apartments){
        int qtyApartmentsUpdated = 0;
        List<Apartment> updatedApartments = new ArrayList<>();

        if (reviews == null || reviews.isEmpty() || apartments == null || apartments.isEmpty()) {
            return null;
        }

        ThreadLocalRandom rnd = ThreadLocalRandom.current();

        for (Apartment apartment : apartments) {
            // Randomly assign 1 to 3 reviews to each apartment
            int numReviewsToAssign = rnd.nextInt(1, 4); // 1, 2, or 3 reviews

            // Don't assign more reviews than available
            numReviewsToAssign = Math.min(numReviewsToAssign, reviews.size());

            // Randomly select reviews
            List<Review> selectedReviews = new ArrayList<>();
            List<Review> availableReviews = new ArrayList<>(reviews);

            for (int i = 0; i < numReviewsToAssign && !availableReviews.isEmpty(); i++) {
                int randomIndex = rnd.nextInt(availableReviews.size());
                Review selectedReview = availableReviews.remove(randomIndex);

                // Assign the apartment to the review
                selectedReview.setApartment(apartment);
                selectedReviews.add(selectedReview);
            }

            // Save the reviews to database (they already have reviewers assigned)
            for (Review review : selectedReviews) {
                reviewRepository.save(review);
            }

            // Update the apartment and save it
            apartmentService.updateApartment(apartment);

            // Verify by fetching from database
            Apartment apartmentById = apartmentService.findApartmentById(apartment.getId());
            if (apartmentById != null) {
                qtyApartmentsUpdated++;
                updatedApartments.add(apartmentById);
                System.out.println(
                        "Apartment #" + qtyApartmentsUpdated +
                                " assigned " + selectedReviews.size() + " reviews and updated in DB: " + apartmentById);
            }
        }

        return updatedApartments;
    }

    // ---------- POPULATE owners, property contracts ------------------------------

    public List<Owner> populateOwners(int qty) {
        return null;
    }

    public List<PropertyContract> populatePlainPropertyContracts(int qty) {
        return null;
    }

    public List<PropertyContract> assignPropertyContracts(
            int qty, List<PropertyContract> plainPropertyContracts, List<Owner> owners) {
        return null;
    }
}
