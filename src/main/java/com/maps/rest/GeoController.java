package com.maps.rest;

import com.maps.mongo.Event;
import com.maps.mongo.EventGeometry;
import com.maps.mongo.EventRepository;
import com.maps.mongo.GeoDoc;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeoController {

    private final EventRepository eventRepository;
    private final MongoTemplate mongoTemplate;

    public GeoController(
            EventRepository eventRepository,
            MongoTemplate mongoTemplate) {
        this.eventRepository = eventRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping("/eventsInRegion")
    @ResponseBody
    public List<EventDto> getEventsInRegion(@RequestParam String iso3Code) {
        Query query = new BasicQuery("{\"properties.ISO_A3\": \"" + iso3Code + "\", \"geometry.type\": \"Point\"}");
        return mongoTemplate.find(query, Event.class).stream().map(
                point -> new EventDto(point.getGeometry().getCoordinates().get(1),
                        point.getGeometry().getCoordinates().get(0),
                        point.getProperties())
        ).collect(Collectors.toList());
    }

    @GetMapping("/regionForEvent")
    public String getRegionForEvent(@RequestParam String lat, @RequestParam String lng) {
        return findEventRegion(new EventDto(Double.parseDouble(lat), Double.parseDouble(lng), Collections.EMPTY_MAP));
    }

    @RequestMapping(value = "/saveEvent", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveEvent(@RequestBody EventDto eventDto) {
        String eventRegion = findEventRegion(eventDto);
        Map<String, String> properties = Collections.singletonMap("ISO_A3", eventRegion);

        Event p = Event.builder()
                .geometry(EventGeometry.builder()
                        .coordinates(Arrays.asList(eventDto.getLng(), eventDto.getLat()))
                        .type("Point")
                        .build())
                .type("Feature")
                .properties(properties)
                .build();

        eventRepository.save(p);
    }

    @GetMapping("/getEventsAround")
    @ResponseBody
    public List<PolygonPointDto> findEventsAround(
            @RequestParam String lat,
            @RequestParam String lng,
            @RequestParam String radiusInMeters) {
        Query query = new BasicQuery(
                " { \"geometry\": {$near: { $geometry: {type: \"Point\", coordinates: [ " + lng + ", " + lat + "] }, "
                        + "$maxDistance: " + radiusInMeters + " } } } ");
        List<GeoDoc> geoDocs = mongoTemplate.find(query, GeoDoc.class);

        return geoDocs.stream()
                .map(point -> new PolygonPointDto(point.getGeometry().getCoordinates().get(0).get(0).get(0).get(1),
                        point.getGeometry().getCoordinates().get(0).get(0).get(0).get(0)))
                .collect(Collectors.toList());
    }

    @GetMapping("/loadBorders")
    @ResponseBody
    public List<PolyLineDto> loadBorders(
            @RequestParam String lat0,
            @RequestParam String lon0,
            @RequestParam String lat1,
            @RequestParam String lon1) {
        //frm the request comes the upper right and bottom left
        //in the query this should be bottom left and upper right. first lon then lat
        //points most fo in the direction of one hand rule
        Query query = new BasicQuery(
                " { $and : [{\"geometry\": { $geoIntersects: { $geometry: {"
                        + " \"type\": \"Polygon\","
                        + " \"coordinates\": [[ "
                        + " [ " + lon1 + ", " + lat0 + "],"
                        + " [ " + lon1 + ", " + lat1 + "],"
                        + " [ " + lon0 + ", " + lat1 + "],"
                        + " [ " + lon0 + ", " + lat0 + "],"
                        + " [ " + lon1 + ", " + lat0 + "]"
                        + " ]]"
                        + " } } } }, {}"
                        + "]}");
        List<GeoDoc> geoDocs = mongoTemplate.find(query, GeoDoc.class);

        List<PolyLineDto> polyLines = new LinkedList<>();
        for (GeoDoc doc : geoDocs) {

            if (doc.getGeometry().getType().equals("MultiPolygon")) {
                for (List<List<List<Double>>> coordinate : doc.getGeometry().getCoordinates()) {

                    for (List<List<Double>> points : coordinate) {
                        PolyLineDto polyLine = PolyLineDto.builder().polyLine(points.stream()
                                .map(point ->
                                        new PolygonPointDto(point.get(1), point.get(0)))
                                .collect(Collectors.toList()))
                                .build();
                        polyLines.add(polyLine);
                    }
                }
            }
            else {
                for (List<List<List<Double>>> coordinate : doc.getGeometry().getCoordinates()) {
                    List<PolygonPointDto> pointList = new LinkedList<>();
                    for (List<List<Double>> points : coordinate) {
                        Stack<Double> stack = new Stack<>();
                        for (int i = 0; i < points.size(); i++) {
                            if (i % 2 == 0) {
                                stack.push(points.get(i).get(0));
                            }
                            else {
                                pointList.add(new PolygonPointDto(stack.pop(), points.get(i).get(0)));
                            }
                        }
                    }
                    polyLines.add(PolyLineDto.builder().polyLine(pointList).build());
                }
            }
        }
        return polyLines;
    }

    private String findEventRegion(EventDto eventDto) {
        Query query = new BasicQuery(
                " {\n"
                        + "    geometry: {\n"
                        + "      $geoIntersects:\n"
                        + "        {\n"
                        + "          $geometry: {\n"
                        + "            \"type\": \"Point\",\n"
                        + "            \"coordinates\": [" + eventDto.getLng() + "," + eventDto.getLat() + "]\n"
                        + "          }\n"
                        + "        }\n"
                        + "    }\n"
                        + "  } ");
        GeoDoc geoDoc = mongoTemplate.findOne(query, GeoDoc.class);
        return geoDoc.getProperties().get("ISO_A3");
    }
}
