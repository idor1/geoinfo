package com.maps.mongo;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@ToString
@Document(collection = "countries")
public class Event {

    @Id
    private String id;

    private EventGeometry geometry;
    private String type;

    private Map<String, String> properties;

    @Builder
    private Event(String id, EventGeometry geometry, Map<String, String> properties, String type) {
        this.id = id;
        this.type = type;
        this.geometry = geometry;
        this.properties = properties;
    }
}
