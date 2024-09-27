package com.project.superleague.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Matches")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Match extends AbstractEntity {
    @Column(name = "MatchDate", nullable = false)
}
