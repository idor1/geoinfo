package com.maps.rest;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class PolyLineDto {

    private List<PolygonPointDto> polyLine;

    @Builder
    private PolyLineDto(List<PolygonPointDto> polyLine) {
        this.polyLine = polyLine;
    }
}
