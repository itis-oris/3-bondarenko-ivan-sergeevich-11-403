package com.itis.cryptotracker.scheduler;

import com.itis.cryptotracker.repository.PriceSnapshotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapshotCleanupScheduler {

    private final PriceSnapshotRepository snapshotRepository;

    @Scheduled(cron = "0 0 3 * * *")
    public void clean() {
        Instant cutoff = Instant.now().minus(7, ChronoUnit.DAYS);
        int deleted = snapshotRepository.deleteOlderThan(cutoff);
        log.info("Snapshot cleanup: deleted {} records older than {}", deleted, cutoff);

    }
}
