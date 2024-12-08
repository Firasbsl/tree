package com.example.tree.Mapper;

import com.example.tree.Dto.CellDTO;
import com.example.tree.Entity.Cell;

public class CellMapper {
    public static CellDTO toDTO(Cell cell) {
        CellDTO dto = new CellDTO();
        dto.setX(cell.getX());
        dto.setY(cell.getY());
        dto.setState(cell.getState().name());
        return dto;
    }

    public static Cell toEntity(CellDTO dto) {
        Cell cell = new Cell();
        cell.setX(dto.getX());
        cell.setY(dto.getY());
        cell.setState(Cell.CellState.valueOf(dto.getState()));
        return cell;
    }
}
