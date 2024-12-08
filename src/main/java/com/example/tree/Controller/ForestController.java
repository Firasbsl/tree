package com.example.tree.Controller;

import com.example.tree.Dto.CellDTO;
import com.example.tree.Mapper.CellMapper;
import com.example.tree.Service.ForestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forest")
public class ForestController {
    private final ForestService forestService;

    public ForestController(ForestService forestService) {
        this.forestService = forestService;
    }

    // Endpoint pour initialiser la forêt avec des positions de feu
    @PostMapping("/initialize")
    public String initializeForest(@RequestBody List<int[]> initialFirePositions) {
        try {
            forestService.initializeForest(initialFirePositions);
            return "Forest initialized successfully";
        } catch (Exception e) {
            return "Error initializing forest: " + e.getMessage();
        }
    }

    // Endpoint pour simuler une étape du feu
    @GetMapping("/simulate")
    public List<CellDTO> simulateStep() {
        try {
            return forestService.simulateStep().stream()
                    .map(CellMapper::toDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error during simulation step: " + e.getMessage());
        }
    }

    // Endpoint pour récupérer l'état actuel de la forêt
    @GetMapping("/status")
    public List<CellDTO> getForestStatus() {
        try {
            return forestService.getAllCells().stream()
                    .map(CellMapper::toDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching forest status: " + e.getMessage());
        }
    }

    // Gestion d'exception pour retourner des messages d'erreur personnalisés
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e) {
        return "An error occurred: " + e.getMessage();
    }
}
