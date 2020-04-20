package com.maps.mongo;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Geometry {

    private String type;

    private List<List<List<List<Double>>>> coordinates;

    @Builder
    private Geometry(String type, List<List<List<List<Double>>>> coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }
}
