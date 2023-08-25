package com.example.bot.tgbot.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name="users")
@Data
public class NewUserEntity {

    @Id
    @SequenceGenerator(name="usersSequence", sequenceName="users_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator="usersSequence")
    @Column(name = "id")
    private Long id;

    @Column(name="tg_id")
    private String tgId;

    @Column(name="user_name")
    private String userName;

    @Column(name="reg_date")
    private String registrationDate;
}
