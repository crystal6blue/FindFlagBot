package com.tests.findflagbot.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long playerTelegramId;

    private String name;

    private Integer points;

    private LocalDateTime dateOfRegistration;

    public Player() {
    }

    public Player(Long playerTelegramId, String name, Integer points, LocalDateTime dateOfRegistration) {
        this.playerTelegramId = playerTelegramId;
        this.name = name;
        this.points = points;
        this.dateOfRegistration = dateOfRegistration;
    }
}
