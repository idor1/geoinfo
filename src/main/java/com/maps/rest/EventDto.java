package com.maps.rest;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EventDto {

    private Double lat;
    private Double lng;

    private Map<String, String> properties;
}
