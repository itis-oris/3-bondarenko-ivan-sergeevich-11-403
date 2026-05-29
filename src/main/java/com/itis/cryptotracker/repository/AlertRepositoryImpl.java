package com.itis.cryptotracker.repository;

import com.itis.cryptotracker.model.Alert;
import com.itis.cryptotracker.model.enums.AlertDirection;
import com.itis.cryptotracker.model.enums.AlertStatus;
import com.itis.cryptotracker.model.Coin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AlertRepositoryImpl implements AlertRepositoryCustom {

    @PersistenceContext
    private EntityManager em;


    @Override
    public List<Alert> searchUserAlerts(Long userId,
                                        AlertStatus status,
                                        AlertDirection direction,
                                        String coinSymbolQuery) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Alert> cq = cb.createQuery(Alert.class);
        Root<Alert> alert = cq.from(Alert.class);

        alert.fetch("coin", JoinType.INNER);
        Join<Alert, Coin> coin = alert.join("coin");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(alert.get("user").get("id"), userId));

        if (status != null) {
            predicates.add(cb.equal(alert.get("status"), status));
        }
        if (direction != null) {
            predicates.add(cb.equal(alert.get("direction"), direction));
        }
        if (StringUtils.hasText(coinSymbolQuery)) {
            String pattern = "%" + coinSymbolQuery.trim().toLowerCase() + "%";
            predicates.add(cb.or(
                    cb.like(cb.lower(coin.get("symbol")), pattern),
                    cb.like(cb.lower(coin.get("name")), pattern)
            ));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.desc(alert.get("createdAt")));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public List<Alert> findActiveAlertsByCoinIds(List<Long> coinIds) {
        if (coinIds == null || coinIds.isEmpty()) {
            return List.of();
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Alert> cq = cb.createQuery(Alert.class);
        Root<Alert> alert = cq.from(Alert.class);

        alert.fetch("coin", JoinType.INNER);

        cq.where(
                cb.equal(alert.get("status"), AlertStatus.ACTIVE),
                alert.get("coin").get("id").in(coinIds)
        );
        return em.createQuery(cq).getResultList();
    }
}