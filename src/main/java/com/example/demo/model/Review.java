package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "Отзывы")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @lombok.Getter
    private Long id;

    @lombok.Setter
    @lombok.Getter
    //@JsonBackReference
    @ManyToOne
    @JoinColumn(name = "обозреватель_id")
    private User reviewer;

    @lombok.Setter
    @lombok.Getter
    //@JsonBackReference
    @ManyToOne
    @JoinColumn(name = "обозреваемый_id")
    private User reviewedUser;

    @lombok.Setter
    @lombok.Getter
    private String content;

    @lombok.Setter
    @lombok.Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ModerationStatus status = ModerationStatus.PENDING;

    public Review() {
        super();
    }

    public Review(User reviewer, User reviewedUser, String content) {
        super();
        this.reviewer = reviewer;
        this.reviewedUser = reviewedUser;
        this.content = content;
    }
}