package kr1v.mcguieditor.client;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class JsonUtils {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    private static final Type LIST_TYPE = new TypeToken<List<Window>>() {}.getType();

    public static void writeToJson(List<Window> windowList, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            GSON.toJson(windowList, LIST_TYPE, writer);
        }
    }
    public static List<Window> readFromJson(String filePath) throws IOException {
        try (FileReader reader = new FileReader(filePath)) {
            return GSON.fromJson(reader, LIST_TYPE);
        }
    }

}
