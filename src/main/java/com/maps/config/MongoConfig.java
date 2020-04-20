package com.maps.config;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

    @Override
    protected String getDatabaseName() {
        return "geodb";
    }

    @Override
    public MongoClient mongoClient() {
        return new MongoClient("mongodb", 27017);
    }

    @Override
    protected String getMappingBasePackage() {
        return "com.maps";
    }


}