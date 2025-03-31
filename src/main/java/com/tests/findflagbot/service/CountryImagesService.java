package com.tests.findflagbot.service;

import com.tests.findflagbot.models.CountryImages;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class CountryImagesService {

    private final List<CountryImages> countryImagesList;

    public CountryImagesService() {
        try {
            this.countryImagesList = loadImages();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<CountryImages> loadImages() throws IOException {
        List<CountryImages> imagesList = new ArrayList<>();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        Resource[] resources = resolver.getResources("classpath:/CountryImages/*.{png,jpg,jpeg,gif}");

        for (Resource resource : resources) {
            String filename = resource.getFilename();

            assert filename != null;
            String countryName = filename.replaceFirst("\\.[^.]+$", "");

            File tempFile = File.createTempFile("flag-", "-" + filename);
            try (InputStream is = resource.getInputStream();
                 OutputStream os = new FileOutputStream(tempFile)) {
                is.transferTo(os);
            }

            tempFile.deleteOnExit();

            imagesList.add(new CountryImages(countryName, tempFile));
        }

        return imagesList;
    }

    public List<CountryImages> getAllCountryImages() {
        return new ArrayList<>(countryImagesList);
    }

    public CountryImages getCountryImagesById(int id) {
        if (id >= 0 && id < countryImagesList.size()) {
            return countryImagesList.get(id);
        }
        return null;
    }

    public Optional<Integer> getRandomCountryImagesId(Set<Integer> excludedIds) {
        int random = new Random().nextInt(countryImagesList.size());

        while(excludedIds.contains(random)){
            random = new Random().nextInt(countryImagesList.size());
        }

        return Optional.of(random);
    }

}