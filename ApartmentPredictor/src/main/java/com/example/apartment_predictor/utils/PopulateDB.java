package com.example.apartment_predictor.utils;

import com.example.apartment_predictor.model.Apartment;
import com.example.apartment_predictor.service.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class PopulateDB {

    @Autowired
    ApartmentService apartmentService;

    public int populateApartments(int qty) {
        int qtyApartmetnsCreated = 0;
        if (qty <= 0) return 0;

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
                System.out.println(
                        "Apartment #" + qtyApartmetnsCreated +
                         "/" + qty + " created populateDB: " + apartmentById);
            }

        }
        return qtyApartmetnsCreated;
    }
}
