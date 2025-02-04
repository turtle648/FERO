package com.ssafy.db.entity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
@Entity
@Getter
@Setter
@Table(name = "tutorial_types")
public class TutorialType extends BaseEntity {
    @Column(name = "tutorial_name", nullable = false, unique = true, length = 50)
    private String tutorialName;
}