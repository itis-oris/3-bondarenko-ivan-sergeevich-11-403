package com.itis.cryptotracker.repository;

import com.itis.cryptotracker.model.Coin;
import com.itis.cryptotracker.model.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CoinRepositoryImpl implements CoinRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Coin> searchByQueryAndTags(String query, List<String> tagNames) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Coin> cq = cb.createQuery(Coin.class);
        Root<Coin> coin = cq.from(Coin.class);
        coin.fetch("tags", JoinType.LEFT);
        cq.distinct(true);

        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.hasText(query)) {
            String pattern = "%" + query.trim().toLowerCase() + "%";
            Predicate byName = cb.like(cb.lower(coin.get("name")), pattern);
            Predicate bySymbol = cb.like(cb.lower(coin.get("symbol")), pattern);
            Predicate byGeckoId = cb.like(cb.lower(coin.get("coinGeckoId")), pattern);
            predicates.add(cb.or(byName, bySymbol, byGeckoId));
        }

        if (tagNames != null && !tagNames.isEmpty()) {
            Join<Coin, Tag> tagJoin = coin.join("tags");
            predicates.add(tagJoin.get("name").in(tagNames));
        }

        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[0]));
        }
        cq.orderBy(cb.asc(coin.get("name")));

        return em.createQuery(cq).getResultList();
    }
}
