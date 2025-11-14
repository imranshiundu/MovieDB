package com.example.moviesapi.converter;

import java.time.LocalDate;
import java.util.Optional;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, String> {

    @Override
    public String convertToDatabaseColumn(LocalDate localDate) {
        return Optional.ofNullable(localDate)
                .map(LocalDate::toString)
                .orElse(null);
    }

    @Override
    public LocalDate convertToEntityAttribute(String dateString) {
        return Optional.ofNullable(dateString)
                .map(LocalDate::parse)
                .orElse(null);
    }
}