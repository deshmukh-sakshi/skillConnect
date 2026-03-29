package com.skillconnect.backend.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Skills {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "freelancerSkill")
    private Set<Freelancer> freelancers = new HashSet<>();
}
