package com.tests.findflagbot.repository;

import com.tests.findflagbot.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Player findPLayerByPlayerTelegramId(Long aLong);
}
