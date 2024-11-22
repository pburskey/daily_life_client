package com.burskey.dailylife;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class LambdaResourceLoader {

    public static final String ResourceURI = "classpath:resources.txt";
    public final Map<Thing, String> map = new HashMap<>();

    public String get(Thing thing) {
        return this.map.get(thing);
    }

    public enum Thing {

        PartySave("party/"),
        PartyFindByID("party/"),
        CommunicationSave("party/communication"),
        CommunicationFindByPartyAndCommunication("party/communication/"),
        CommunicationFindByParty("party/communications/");

        String extension = null;

        Thing(String extension) {
            this.extension = extension;
        }

        public String assemble(String base) {
            return base + extension;
        }


    }

    private LambdaResourceLoader(Map<Thing, String> amap) {
        this.map.putAll(amap);
    }

    public static String ReadFile(String path) {
        try {
            File file = ResourceUtils.getFile(path);
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static File FindFile(String path) {

        try {
            File file = ResourceUtils.getFile(path);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static LambdaResourceLoader Build(String stage) {
        Map<Thing, String> map = new HashMap<>();

        String data = ReadFile(ResourceURI);
        if (data != null) {
            String[] strings = data.split("\n");
            for (String string : strings) {
                if (string.contains(stage)) {
                    if (string.endsWith("/" + stage + "/")) {
                        map.put(Thing.PartyFindByID, string);
                    } else if (string.endsWith("/" + stage + "/save")) {
                        map.put(Thing.PartySave, string);
                    }
                }
            }
        }

        return new LambdaResourceLoader(map);


    }


    public static LambdaResourceLoader BuildUsingBaseURI(String base) {
        Map<Thing, String> map = new HashMap<>();

        if (base != null) {
            for (Thing thing : Thing.values()) {
                map.put(thing, thing.assemble(base));
            }
        }

        return new LambdaResourceLoader(map);


    }


}
