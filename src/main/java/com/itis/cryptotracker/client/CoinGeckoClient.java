package com.itis.cryptotracker.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itis.cryptotracker.config.CoinDetails;
import com.itis.cryptotracker.config.RedisCacheConfig;
import com.itis.cryptotracker.exception.ExternalServiceException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
@RequiredArgsConstructor
public class CoinGeckoClient {

    private final OkHttpClient http;
    private final ObjectMapper json;

    @Value("${app.coingecko.base-url}")
    private String baseUrl;

    @Value("${app.coingecko.api-key}")
    private String apiKey;


    @Cacheable(cacheNames = RedisCacheConfig.CACHE_COIN_PRICES, key = "#coinGeckoIds.toString()")
    public List<CoinPriceDto> fetchPrices(List<String> coinGeckoIds) {
        if (coinGeckoIds == null || coinGeckoIds.isEmpty()) {
            return List.of();
        }

        HttpUrl url = HttpUrl.parse(baseUrl + "/simple/price").newBuilder()
                .addQueryParameter("ids", String.join(",", coinGeckoIds))
                .addQueryParameter("vs_currencies", "usd")
                .addQueryParameter("x_cg_demo_api_key", apiKey)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .get()
                .build();

        try (Response response = http.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new ExternalServiceException("CoinGecko returned HTTP %d".formatted(response.code()));
            }
            ResponseBody body = response.body();
            if (body == null) {
                throw new ExternalServiceException("CoinGecko returned empty body");
            }
            JsonNode root = json.readTree(body.byteStream());

            List<CoinPriceDto> result = new ArrayList<>();
            Iterator<Map.Entry<String, JsonNode>> it = root.fields();
            while (it.hasNext()) {
                Map.Entry<String, JsonNode> entry = it.next();
                JsonNode priceNode = entry.getValue().get("usd");
                if (priceNode != null && !priceNode.isNull()) {
                    result.add(new CoinPriceDto(entry.getKey(), new BigDecimal(priceNode.asText())));
                }
            }
            log.debug("Fetched {} prices from CoinGecko", result.size());
            return result;
        } catch (IOException e) {
            throw new ExternalServiceException("CoinGecko request failed", e);
        }
    }


    @Cacheable(cacheNames = RedisCacheConfig.CACHE_COIN_DETAILS, key = "#coinGeckoId")
    public CoinDetails fetchDetails(String coinGeckoId) {
        HttpUrl url = HttpUrl.parse(baseUrl + "/coins/" + coinGeckoId).newBuilder()
                .addQueryParameter("localization", "false")
                .addQueryParameter("x_cg_demo_api_key", apiKey)
                .addQueryParameter("tickers", "false")
                .addQueryParameter("market_data", "false")
                .addQueryParameter("community_data", "false")
                .addQueryParameter("developer_data", "false")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .get()
                .build();

        try (Response response = http.newCall(request).execute()) {
            if (response.code() == 404) {
                throw new ExternalServiceException("Unknown coin id: %s".formatted(coinGeckoId));
            }
            if (!response.isSuccessful()) {
                throw new ExternalServiceException("CoinGecko returned HTTP %d".formatted(response.code()));
            }
            ResponseBody body = response.body();
            if (body == null) {
                throw new ExternalServiceException("Empty response from CoinGecko");
            }
            JsonNode root = json.readTree(body.byteStream());

            String symbol = textOrNull(root.get("symbol"));
            String name = textOrNull(root.get("name"));
            String image = root.path("image").path("small").asText(null);
            return new CoinDetails(coinGeckoId, symbol, name, image);
        } catch (IOException e) {
            throw new ExternalServiceException("CoinGecko request failed", e);
        }
    }

    private static String textOrNull(JsonNode node) {
        return (node == null || node.isNull()) ? null : node.asText();
    }

}
