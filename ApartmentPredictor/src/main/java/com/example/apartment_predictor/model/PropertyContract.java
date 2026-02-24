package com.example.apartment_predictor.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class PropertyContract {

    @Id
    private String id;
    private String propertyContractCode;
    private String urlContractPropertyDocument;
    private LocalDate contractDate;
    private long valuePropertyContract;
    private String typeProperty;
    private String address;
    private boolean isActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "OWNER_FK")
    private Owner owner;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "APARTMENT_FK")
    private Apartment apartment;

    public PropertyContract() {
        this.id = UUID.randomUUID().toString();
    }

    public PropertyContract(String propertyContractCode,String urlContractPropertyDocument, LocalDate contractDate, long valuePropertyContract, String typeProperty, String address, boolean isActive) {
        this.id = UUID.randomUUID().toString();
        this.propertyContractCode = propertyContractCode;
        this.urlContractPropertyDocument = urlContractPropertyDocument;
        this.contractDate = contractDate;
        this.valuePropertyContract = valuePropertyContract;
        this.typeProperty = typeProperty;
        this.address = address;
        this.isActive = isActive;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPropertyContractCode() {
        return propertyContractCode;
    }

    public void setPropertyContractCode(String propertyContractCode) {
        this.propertyContractCode = propertyContractCode;
    }

    public String getUrlContractPropertyDocument() {
        return urlContractPropertyDocument;
    }

    public void setUrlContractPropertyDocument(String urlContractPropertyDocument) {
        this.urlContractPropertyDocument = urlContractPropertyDocument;
    }

    public LocalDate getContractDate() {
        return contractDate;
    }

    public void setContractDate(LocalDate contractDate) {
        this.contractDate = contractDate;
    }

    public long getValuePropertyContract() {
        return valuePropertyContract;
    }

    public void setValuePropertyContract(long valuePropertyContract) {
        this.valuePropertyContract = valuePropertyContract;
    }

    public String getTypeProperty() {
        return typeProperty;
    }

    public void setTypeProperty(String typeProperty) {
        this.typeProperty = typeProperty;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    @Override
    public String toString() {
        return "PropertyContract{" +
                "id='" + id + '\'' +
                ", propertyContractCode='" + propertyContractCode + '\'' +
                ", urlContractPropertyDocument='" + urlContractPropertyDocument + '\'' +
                ", contractDate=" + contractDate +
                ", valuePropertyContract=" + valuePropertyContract +
                ", typeProperty='" + typeProperty + '\'' +
                ", address='" + address + '\'' +
                ", isActive=" + isActive +
                ", owners=" + owner.getId() +
                ", apartments=" + apartment.getId() +
                '}';
    }
}