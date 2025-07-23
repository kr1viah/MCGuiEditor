package kr1v.mcguieditor.client;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class JsonUtils {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    private static final Type MAP_TYPE = new TypeToken<Map<String, List<Window>>>() {}.getType();

    public static void writeToJson(Map<String, List<Window>> windowMap, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            GSON.toJson(windowMap, MAP_TYPE, writer);
        }
    }
    public static Map<String, List<Window>> readFromJson(String filePath) throws IOException {
        try (FileReader reader = new FileReader(filePath)) {
            return GSON.fromJson(reader, MAP_TYPE);
        }
    }

}
