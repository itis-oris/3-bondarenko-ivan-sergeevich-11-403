package com.itis.cryptotracker.repository;

import com.itis.cryptotracker.model.Coin;

import java.util.List;

public interface CoinRepositoryCustom {

    List<Coin> searchByQueryAndTags(String query, List<String> tagNames);
}
