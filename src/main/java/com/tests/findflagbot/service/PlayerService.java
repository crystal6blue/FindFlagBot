package com.tests.findflagbot.service;

import com.tests.findflagbot.models.Player;
import com.tests.findflagbot.repository.PlayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    public void save(Player player) {
        playerRepository.save(player);
    }
    public void delete(Player player) {
        playerRepository.delete(player);
    }

    public Player findByChatId(Long chatId) {
        List<Player> players = playerRepository.findAll();
        for(Player player : players){
            if(player.getPlayerTelegramId().equals(chatId)){
                return player;
            }
        }
        return null;
    }

    public Optional<Player> addPointToPlayer(Integer id) {
        Optional<Player> existingPlayerOptional = playerRepository.findById(id.longValue());

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
