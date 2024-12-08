package com.example.tree.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data

public class Cell {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private int x;

    private int y;

    @Enumerated(EnumType.STRING)
    private CellState state;

    // Getters et setters
    public Long getId() {
        return id;
    }

    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    public CellState getState() {
        return state;
    }
    public void setState(CellState state) {
        this.state = state;
    }

    public enum CellState {
        EMPTY, // Vide (non brûlé)
        FIRE,  // En feu
        ASH    // Cendre (brûlé et éteint)
    }
}
