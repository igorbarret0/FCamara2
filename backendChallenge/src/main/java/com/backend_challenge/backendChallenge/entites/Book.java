package com.backend_challenge.backendChallenge.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "tb_books")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String authorName;

    private String ISBN;

    private Integer timesRented;

    private Integer timesDelayed;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Copy> bookCopies;

}
