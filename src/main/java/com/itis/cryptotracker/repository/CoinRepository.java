package com.itis.cryptotracker.repository;

import com.itis.cryptotracker.model.Coin;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CoinRepository extends JpaRepository<Coin, Long>, CoinRepositoryCustom {

    @EntityGraph(attributePaths = "tags")
    Optional<Coin> findByCoinGeckoId(String coinGeckoId);

    @Query("select distinct c from Coin c left join fetch c.tags")
    List<Coin> findAllWithTags();

    @EntityGraph(attributePaths = "tags")
    Optional<Coin> findById(Long id);



    List<Coin> findAllByCoinGeckoIdIn(List<String> coinGeckoIds);
}
