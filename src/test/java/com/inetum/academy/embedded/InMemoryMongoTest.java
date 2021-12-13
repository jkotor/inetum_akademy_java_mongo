package com.inetum.academy.embedded;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InMemoryMongoTest {

    private MongodFactory mongo;
    private MongoClient client;

    @SneakyThrows
    @BeforeEach
    protected void before() {
        mongo = new MongodFactory();
        client = mongo.newMongo();
    }

    @SneakyThrows
    @AfterEach
    protected void after() {
        mongo.shutdown();
    }

    @Test
    void listDatabases() {
        // Wylistowac wszystkie bazy danych
        MongoCursor<String> iterator = client.listDatabaseNames().iterator();
        System.out.print("Existing databases: ");
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();
    }

}
