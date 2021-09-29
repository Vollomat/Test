package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class KinoSaale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int kinoSaalID;

    private int kinoID;

    public int getKinoSaalID() {
        return kinoSaalID;
    }

    public void setKinoSaalID(int kinoSaalID) {
        this.kinoSaalID = kinoSaalID;
    }

    public int getKino() {
        return kinoID;
    }

    public void setKino(int kino) {
        this.kinoID = kino;
    }
}
