package com.maps.mongo;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@ToString
@Document(collection = "countries")
public class GeoDoc {

    private String type;

    private Geometry geometry;

    private Map<String, String> properties;

    @Builder
    private GeoDoc(String type, Geometry geometry, Map<String, String> properties) {
        this.type = type;
        this.geometry = geometry;
        this.properties = properties;
    }
}
