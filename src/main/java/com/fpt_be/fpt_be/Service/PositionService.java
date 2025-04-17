package com.fpt_be.fpt_be.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpt_be.fpt_be.Dto.PositionDto;
import com.fpt_be.fpt_be.Entity.Position;
import com.fpt_be.fpt_be.Repository.PositionRepository;

@Service
public class PositionService {
    
    @Autowired
    private PositionRepository positionRepository;

    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }

    public Position createPosition(PositionDto positionDto) {
        Position position = new Position();
        position.setTenChucVu(positionDto.getTenChucVu());
        position.setTinhTrang(positionDto.getTinhTrang());
        return positionRepository.save(position);
    }

    public Position updatePosition(Long id, PositionDto positionDto) {
        Optional<Position> existingPosition = positionRepository.findById(id);
        if (existingPosition.isPresent()) {
            Position position = existingPosition.get();
            position.setTenChucVu(positionDto.getTenChucVu());
            position.setTinhTrang(positionDto.getTinhTrang());
            return positionRepository.save(position);
        }
        throw new RuntimeException("Không tìm thấy chức vụ với id: " + id);
    }

    public void deletePosition(Long id) {
        if (!positionRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy chức vụ với id: " + id);
        }
        positionRepository.deleteById(id);
    }

    public Position getPositionById(Long id) {
        return positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chức vụ với id: " + id));
    }
} 