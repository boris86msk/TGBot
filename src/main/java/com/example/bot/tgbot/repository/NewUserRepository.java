package com.example.bot.tgbot.repository;

import com.example.bot.tgbot.entity.NewUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewUserRepository extends JpaRepository<NewUserEntity, Long>
 {
    NewUserEntity findByTgId(String tgId);
}
