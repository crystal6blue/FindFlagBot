package com.tests.findflagbot.service;

import com.tests.findflagbot.models.CountryImages;
import com.tests.findflagbot.repository.CountryImagesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryImagesService {

    private final CountryImagesRepository countryImagesRepository;

    public CountryImagesService(CountryImagesRepository countryImagesRepository) {
        this.countryImagesRepository = countryImagesRepository;
    }

    public List<CountryImages> getAllCountryImages() {
        return countryImagesRepository.findAll();
    }
    public Optional<CountryImages> getCountryImagesById(long id) {
        return countryImagesRepository.findById(id);
    }
    public Optional<CountryImages> getRandomCountryImages(Integer num) {
        int randN = (int)(Math.random()*220)+1;

        while(randN == num) {
            randN = (int)(Math.random()*220)+1;
        }

        CountryImages t = getCountryImagesById(randN).get();

        return Optional.of(t);
    }
    public Optional<CountryImages> getCountryImagesById(long id, CountryImages countryImages) {
        List<CountryImages> countryImagesList = countryImagesRepository.findAll();

        for(CountryImages countryImage: countryImagesList) {
            if(countryImage.getId() == id){
                return Optional.of(countryImage);
            }
        }
        return null;
    }
}
