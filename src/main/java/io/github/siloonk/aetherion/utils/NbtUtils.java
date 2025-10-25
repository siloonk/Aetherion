package io.github.siloonk.aetherion.utils;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.jglrxavpok.hephaistos.nbt.*;

import java.util.Map;

public final class NbtUtils {

    private NbtUtils() {} // Utility class

    public static NBTCompound fromJson(JsonObject json) {



        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();

            if (value.isJsonObject()) {
                // Nested object → NBTCompound
                builder.put(key, fromJson(value.getAsJsonObject()));
            } else if (value.isJsonArray()) {
                JsonArray array = value.getAsJsonArray();

                if (!array.isEmpty() && array.get(0).isJsonPrimitive()) {
                    JsonPrimitive first = array.get(0).getAsJsonPrimitive();
                    if (first.isNumber()) {
                        int[] intArray = new int[array.size()];
                        for (int i = 0; i < array.size(); i++) {
                            intArray[i] = array.get(i).getAsInt();
                        }
                        builder.put(key, new NBTIntArray(intArray));
                    } else if (first.isString()) {
                        String[] strArray = new String[array.size()];
                        for (int i = 0; i < array.size(); i++) {
                            strArray[i] = array.get(i).getAsString();
                        }
                        builder.put(key, new NBTList<>(strArray));
                    }
                }
            } else if (value.isJsonPrimitive()) {
                JsonPrimitive prim = value.getAsJsonPrimitive();
                if (prim.isNumber()) {
                    if (prim.getAsString().contains(".")) {
                        builder.put(key, new NBTFloat(prim.getAsFloat()));
                    } else {
                        builder.put(key, new NBTInt(prim.getAsInt()));
                    }
                } else if (prim.isBoolean()) {
                    builder.put(key, new NBTByte((byte) (prim.getAsBoolean() ? 1 : 0)));
                } else if (prim.isString()) {
                    builder.put(key, new NBTString(prim.getAsString()));
                }
            }
        }

        return builder.build();
    }
}
