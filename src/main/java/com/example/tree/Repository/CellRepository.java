package com.example.tree.Repository;

import com.example.tree.Entity.Cell;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CellRepository extends JpaRepository<Cell, Long> {
    Optional<Cell> findByXAndY(int x, int y);
}
