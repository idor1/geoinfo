package com.maps.mongo;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class EventGeometry {

    private String type;
    private List<Double> coordinates;

    @Builder
    public EventGeometry(String type, List<Double> coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }
}
