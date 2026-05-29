package com.itis.cryptotracker.repository;

import com.itis.cryptotracker.model.Alert;
import com.itis.cryptotracker.model.enums.AlertDirection;
import com.itis.cryptotracker.model.enums.AlertStatus;

import java.util.List;

public interface AlertRepositoryCustom {

    List<Alert> searchUserAlerts(Long userId,
                                 AlertStatus status,
                                 AlertDirection direction,
                                 String coinSymbolQuery);

    List<Alert> findActiveAlertsByCoinIds(List<Long> coinIds);
}
