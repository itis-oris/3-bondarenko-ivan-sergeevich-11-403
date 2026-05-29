package com.itis.cryptotracker.mapper;

import com.itis.cryptotracker.model.Coin;
import com.itis.cryptotracker.model.Tag;
import com.itis.cryptotracker.dto.response.CoinDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CoinDtoMapper {

    @Mapping(source = "tags", target = "tags")
    CoinDto toDto(Coin coin);

    default Set<String> tagsToNames(Set<Tag> tags) {
        if (tags == null) return Set.of();
        return tags.stream()
                .map(Tag::getName)
                .collect(Collectors.toUnmodifiableSet());
    }
}
