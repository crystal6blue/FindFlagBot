package com.tests.findflagbot.service;

import com.tests.findflagbot.models.Player;
import com.tests.findflagbot.repository.PlayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public void save(Player player) {
        playerRepository.save(player);
    }

    public Player findByChatId(Long chatId) {
        Player player = playerRepository.findPLayerByPlayerTelegramId(chatId);
        if(player == null) {
            throw new RuntimeException("Could not find player with chat id " + chatId);
        }
        return player;
    }

    public Optional<Player> addPointToPlayer(Long id) {
        Optional<Player> existingPlayerOptional = playerRepository.findById(id);

        if (existingPlayerOptional.isPresent()) {
            Player existingPlayer = existingPlayerOptional.get();

            existingPlayer.setPoints(existingPlayer.getPoints() + 1);

            return Optional.of(playerRepository.save(existingPlayer));
        } else {
            log.warn("Player with ID {} not found.", id);
        }
        return Optional.empty();
    }
}
