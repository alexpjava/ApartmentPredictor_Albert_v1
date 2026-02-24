package com.example.apartment_predictor.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Apartment {

    @Id
    protected String id;
    private Long price;
    protected Integer area;
    protected Integer bedrooms;
    private Integer bathrooms;
    private Integer stories;
    private String mainroad;
    private String guestroom;
    private String basement;
    private String hotwaterheating;
    private String airconditioning;
    private Integer parking;
    private String prefarea;
    private String furnishingstatus;

    @OneToMany(mappedBy = "apartment",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Review> reviews = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(
            name = "APARTMENT_SCHOOL_JOIN_TABLE",
            joinColumns = @JoinColumn(name = "apartment_id"),
            inverseJoinColumns = @JoinColumn(name = "school_id")
    )
    private List<School> schools = new ArrayList<>();

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PropertyContract> contracts = new ArrayList<>();


    // Default constructor
    public Apartment() {
        this.id = UUID.randomUUID().toString();
    }

    // Constructor with all fields
    public Apartment(Long price, Integer area, Integer bedrooms, Integer bathrooms, 
                    Integer stories, String mainroad, String guestroom, String basement,
                    String hotwaterheating, String airconditioning,
                    Integer parking, String prefarea, String furnishingstatus) {
        this.id = UUID.randomUUID().toString();
        this.price = price;
        this.area = area;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.stories = stories;
        this.mainroad = mainroad;
        this.guestroom = guestroom;
        this.basement = basement;
        this.hotwaterheating = hotwaterheating;
        this.airconditioning = airconditioning;
        this.parking = parking;
        this.prefarea = prefarea;
        this.furnishingstatus = furnishingstatus;
    }

    // helper
    public void addContract(PropertyContract contract) {
        contracts.add(contract);
        contract.setApartment(this);
    }

    public double calculatePrice() {
        double basePrice = area * 120 + (bedrooms * 8000);
        return basePrice * (1 + (area * 0.04));
    }

    // helpers

    public void addReview(Review review) {
        reviews.add(review);
        review.setApartment(this);
    }

    public void removeReview(Review review) {
        reviews.remove(review);
        review.setApartment(null);
    }


    // Getters and Setters
    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public Integer getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(Integer bedrooms) {
        this.bedrooms = bedrooms;
    }

    public Integer getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(Integer bathrooms) {
        this.bathrooms = bathrooms;
    }

    public Integer getStories() {
        return stories;
    }

    public void setStories(Integer stories) {
        this.stories = stories;
    }

    public String getMainroad() {
        return mainroad;
    }

    public void setMainroad(String mainroad) {
        this.mainroad = mainroad;
    }

    public String getGuestroom() {
        return guestroom;
    }

    public void setGuestroom(String guestroom) {
        this.guestroom = guestroom;
    }

    public String getBasement() {
        return basement;
    }

    public void setBasement(String basement) {
        this.basement = basement;
    }

    public String getHotwaterheating() {
        return hotwaterheating;
    }

    public void setHotwaterheating(String hotwaterheating) {
        this.hotwaterheating = hotwaterheating;
    }

    public String getAirconditioning() {
        return airconditioning;
    }

    public void setAirconditioning(String airconditioning) {
        this.airconditioning = airconditioning;
    }

    public Integer getParking() {
        return parking;
    }

    public void setParking(Integer parking) {
        this.parking = parking;
    }

    public String getPrefarea() {
        return prefarea;
    }

    public void setPrefarea(String prefarea) {
        this.prefarea = prefarea;
    }

    public String getFurnishingstatus() {
        return furnishingstatus;
    }

    public void setFurnishingstatus(String furnishingstatus) {
        this.furnishingstatus = furnishingstatus;
    }

    public String getId() {
        return id;
    }

   /* public void setId(String id) {
        this.id = id;
    }*/

   public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<School> getSchools() {
        return schools;
    }

    public void setSchools(List<School> schools) {
        this.schools = schools;
    }

    public void addSchool(School school) {
        this.schools.add(school);
    }

    public void addSchools(List<School> schools ){
       this.schools.addAll(schools);
    }

    public List<PropertyContract> getContracts() {
        return contracts;
    }

    public void setContracts(List<PropertyContract> contracts) {
        this.contracts = contracts;
    }

    @Override
    public String toString() {
        return "Apartment{" +
                "id=" + id +
                ", price=" + price +
                ", area=" + area +
                ", bedrooms=" + bedrooms +
                ", bathrooms=" + bathrooms +
                ", stories=" + stories +
                ", mainroad='" + mainroad + '\'' +
                ", guestroom='" + guestroom + '\'' +
                ", basement='" + basement + '\'' +
                ", hotwater='" + hotwaterheating + '\'' +
                ", airconditioning='" + airconditioning + '\'' +
                ", parking=" + parking +
                ", prefarea='" + prefarea + '\'' +
                ", furnishingstatus='" + furnishingstatus + '\'' +
                ", reviews='" + reviews.size() + '\'' +
                '}';
    }
}
