package com.example.demo.repository;

import com.example.demo.entity.Film;
import com.example.demo.entity.Kunden;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmRepository extends JpaRepository<Film, Integer> {


}