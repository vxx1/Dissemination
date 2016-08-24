package com.outofmemory.entertainment.dissemination.util;

import java.util.List;
import java.util.Random;

public class RandomUtil {
    private static Random random = new Random();

    public static int range(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    public static <T> T element(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }
}
