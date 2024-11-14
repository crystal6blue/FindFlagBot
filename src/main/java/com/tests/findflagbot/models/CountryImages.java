package com.tests.findflagbot.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class CountryImages {

    public CountryImages() {
    }

    public CountryImages(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String imagePath;
}
