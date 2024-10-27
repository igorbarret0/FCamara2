package com.backend_challenge.backendChallenge.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tb_copies")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Copy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String copyCode;

    private boolean isAvailable;

    private LocalDate rentedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(mappedBy = "bookCopies")
    @JsonIgnore
    private List<Book> books;



}
