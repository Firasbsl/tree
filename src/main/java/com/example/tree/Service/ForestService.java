package com.example.tree.Service;

import com.example.tree.Entity.Cell;
import com.example.tree.Repository.CellRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ForestService {
    @Value("${forest.grid.height}")
    private int height;

    @Value("${forest.grid.width}")
    private int width;

    @Value("${forest.propagation.probability}")
    private double propagationProbability;

    private final CellRepository cellRepository;

    public ForestService(CellRepository cellRepository) {
        this.cellRepository = cellRepository;
    }

    // Initialisation de la forêt
    public void initializeForest(List<int[]> initialFirePositions) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = new Cell();
                cell.setX(x);
                cell.setY(y);
                int finalX = x;
                int finalY = y;

                // Si la position initiale est en feu, on met la cellule en feu
                cell.setState(initialFirePositions.stream()
                        .anyMatch(pos -> pos[0] == finalX && pos[1] == finalY)
                        ? Cell.CellState.FIRE
                        : Cell.CellState.EMPTY);

                cellRepository.save(cell);
            }
        }
    }

    // Simulation d'une étape
    public List<Cell> simulateStep() {
        List<Cell> cellsOnFire = cellRepository.findAll().stream()
                .filter(cell -> cell.getState() == Cell.CellState.FIRE)
                .collect(Collectors.toList());

        List<Cell> updatedCells = new ArrayList<>();
        Random random = new Random();

        // Propagation du feu et mise en cendres des cellules en feu
        cellsOnFire.forEach(cell -> {
            cell.setState(Cell.CellState.ASH);  // La cellule en feu devient en cendres
            updatedCells.add(cell);
            spreadFire(cell.getX(), cell.getY(), updatedCells, random);  // Propager le feu
        });

        // Sauvegarde des mises à jour
        return cellRepository.saveAll(updatedCells);
    }

    // Propagation du feu aux cellules adjacentes
    private void spreadFire(int x, int y, List<Cell> updatedCells, Random random) {
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};  // NORD, EST, SUD, OUEST

        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];

            // Vérifier que les nouvelles coordonnées sont dans la grille
            if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                Cell adjacentCell = cellRepository.findByXAndY(newX, newY).orElse(null);

                // Si la cellule adjacente est trouvée et est vide, alors on peut la brûler
                if (adjacentCell != null && adjacentCell.getState() == Cell.CellState.EMPTY) {
                    // Appliquer la probabilité de propagation
                    if (random.nextDouble() < propagationProbability) {
                        adjacentCell.setState(Cell.CellState.FIRE);  // La cellule devient en feu
                        updatedCells.add(adjacentCell);  // Ajouter la cellule à la liste des mises à jour
                    }
                }
            }
        }
    }
    public List<Cell> getAllCells() {
        return cellRepository.findAll();
    }
}
