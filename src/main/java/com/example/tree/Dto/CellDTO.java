package com.example.tree.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class CellDTO {
    private int x;
    private int y;
    private String state;
    // Getters et setters
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
