package com.tests.findflagbot.repository;

import com.tests.findflagbot.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
