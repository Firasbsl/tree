package com.example.tree.test;

import com.example.tree.Controller.ForestController;
import com.example.tree.Entity.Cell;
import com.example.tree.Repository.CellRepository;
import com.example.tree.Service.ForestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

public class ForestServiceTest {
    @Mock
    private CellRepository cellRepository;
    @InjectMocks
    private ForestController forestController;
    @InjectMocks
    private ForestService forestService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(forestController).build();
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
    @Test
    void testGetForestStatus() throws Exception {
        // Mocking the behavior of the service
        Cell cell1 = new Cell();
        cell1.setX(0);
        cell1.setY(0);
        cell1.setState(Cell.CellState.FIRE);

        Cell cell2 = new Cell();
        cell2.setX(1);
        cell2.setY(1);
        cell2.setState(Cell.CellState.EMPTY);

        when(forestService.getAllCells()).thenReturn(List.of(cell1, cell2));

        // Perform GET request and assert the response
        mockMvc.perform(
                get("/api/forest/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].x").value(0))
                .andExpect(jsonPath("$[0].y").value(0))
                .andExpect(jsonPath("$[0].state").value("FIRE"))
                .andExpect(jsonPath("$[1].x").value(1))
                .andExpect(jsonPath("$[1].y").value(1))
                .andExpect(jsonPath("$[1].state").value("EMPTY"));

        // Verify that the service was called
        verify(forestService, times(1)).getAllCells();
    }
}
