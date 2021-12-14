package com.inetum.academy.embedded.data;

import org.bson.Document;

import java.util.Arrays;
import java.util.List;

public class SampleTolkienData {

    private static Document bilbo;
    private static Document frodo;
    private static Document legolas;
    private static Document elrond;
    private static Document boromir;
    private static Document aragorn;
    private static Document saruman;
    private static Document gandalf;

    public static List<Document> takeBilboFrodoLegolas() {
        return Arrays.asList(bilbo, frodo, legolas);
    }

    public static List<Document> takeAllHeroes() {
        return Arrays.asList(bilbo, frodo, legolas, elrond, boromir, aragorn, saruman, gandalf);
    }

    static {
        bilbo = new Document();
        bilbo.append("_id", 1);
        bilbo.append("name", "Bilbo");
        bilbo.append("age", 99);
        bilbo.append("race", "Hobbit");

        frodo = new Document();
        frodo.append("_id", 2);
        frodo.append("name", "Frodo");
        frodo.append("age", 33);
        frodo.append("race", "Hobbit");

        legolas = new Document();
        legolas.append("_id", 3);
        legolas.append("name", "Legolas");
        legolas.append("age", 150);
        legolas.append("race", "Elf");
        legolas.append("weapon", "bow");

    // poniższe tylko do zadań na wyszukiwanie

        elrond = new Document();
        elrond.append("_id", 4);
        elrond.append("name", "Elrond");
        elrond.append("age", 250);
        elrond.append("race", "Elf");
        elrond.append("weapon", "bow");
        elrond.append("magic", "white");

        boromir = new Document();
        boromir.append("_id", 5);
        boromir.append("name", "Boromir");
        boromir.append("age", 37);
        boromir.append("race", "Man");
        boromir.append("kingdom", "Gondor");
        boromir.append("weapon", Arrays.asList("shield", "sword"));

        aragorn = new Document();
        aragorn.append("_id", 6);
        aragorn.append("name", "Aragorn");
        aragorn.append("age", 48);
        aragorn.append("race", "Man");
        aragorn.append("kingdom", "Dunedain");
        aragorn.append("weapon", "sword");

        saruman = new Document();
        saruman.append("_id", 7);
        saruman.append("name", "Saruman");
        saruman.append("age", "???");
        saruman.append("race", "Man");
        saruman.append("magic", "black");

        gandalf = new Document();
        gandalf.append("_id", 8);
        gandalf.append("name", "Gandalf");
        gandalf.append("age", "???");
        gandalf.append("race", "Man");
        gandalf.append("magic", "white");
    }


}
