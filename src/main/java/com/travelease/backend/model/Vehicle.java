package com.travelease.backend.model;

import javax.persistence.*;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String type;

    private String image;

    private Integer price;

    private Integer capacity;

    private String features;

    private String description;

    @Column(name = "available")
    private Boolean available = true;

    public Vehicle() {}

    public Vehicle(String name, String type, String image, Integer price, Integer capacity, String features, String description) {
        this.name = name;
        this.type = type;
        this.image = image;
        this.price = price;
        this.capacity = capacity;
        this.features = features;
        this.description = description;
        this.available = true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public String getFeatures() { return features; }
    public void setFeatures(String features) { this.features = features; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Boolean getAvailable() { return available; }
    public void setAvailable(Boolean available) { this.available = available; }
}
