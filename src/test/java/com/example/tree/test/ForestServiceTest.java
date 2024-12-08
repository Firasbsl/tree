package com.example.tree.test;

import com.example.tree.Entity.Cell;
import com.example.tree.Repository.CellRepository;
import com.example.tree.Service.ForestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.List;

public class ForestServiceTest {
    @Mock
    private CellRepository cellRepository;

    @InjectMocks
    private ForestService forestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void testSimulateStepUpdatesFireToAsh() {
        Cell cellOnFire = new Cell();
        cellOnFire.setX(0);
        cellOnFire.setY(0);
        cellOnFire.setState(Cell.CellState.FIRE);

        when(cellRepository.findAll()).thenReturn(List.of(cellOnFire));

        forestService.simulateStep();

        assertEquals(Cell.CellState.ASH, cellOnFire.getState());
        verify(cellRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testFirePropagation() {
        Cell burningCell = new Cell();
        burningCell.setX(0);
        burningCell.setY(0);
        burningCell.setState(Cell.CellState.FIRE);

        Cell adjacentCell = new Cell();
        adjacentCell.setX(0);
        adjacentCell.setY(1);
        adjacentCell.setState(Cell.CellState.EMPTY);

        when(cellRepository.findAll()).thenReturn(List.of(burningCell));
        when(cellRepository.findByXAndY(0, 1)).thenReturn(Optional.of(adjacentCell));

        forestService.simulateStep();

        assertEquals(Cell.CellState.EMPTY, adjacentCell.getState());
    }
}
