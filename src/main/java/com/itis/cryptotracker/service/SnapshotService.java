package com.itis.cryptotracker.service;

import com.itis.cryptotracker.model.PriceSnapshot;
import com.itis.cryptotracker.repository.PriceSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SnapshotService {

    private final PriceSnapshotRepository snapshotRepository;

    public List<PriceSnapshot> getAllByCoinId(Long coinId, Pageable pageable) {
        return snapshotRepository.findAllByCoinIdOrderByRecordedAtDesc(coinId, pageable);
    }
}
