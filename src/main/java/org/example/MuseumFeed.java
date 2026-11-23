package org.example;

/**
 * 练习配套的博物馆模拟器。它提供固定的读数并记录巡检开始事件，
 * 方便每次运行时复现同一个场景。
 */
public final class MuseumFeed {
    // 一个模拟小时对应的现实时间，单位为毫秒。
    public static final int MILLIS_PER_HOUR = 3000;

    // 每一列对应一个展厅，数组下标就是展厅编号。
    private static final String[] GALLERIES = {"Maps", "Textiles", "Prints", "Ceramics"};

    // 每一行对应一个模拟小时。湿度单位为百分比，-1 表示该次读数缺失。
    private static final double[][] HUMIDITY = {
        {50.0, 50.0, 50.0, 50.0},
        {62.0, 50.0, 50.0, 50.0},
        {63.0, 38.0, 50.0, 50.0},
        {60.0, 39.0, 50.0, 50.0},
        {55.0, 40.0, 50.0, 50.0},
        {55.0, 45.0, 52.0, 49.0},
        {54.0, 49.0, 52.0, 50.0},
        {50.0, 49.0, 59.0, 65.0},
        {50.0, 55.0, 61.0, -1.0},
        {50.0, 55.0, 60.0, 65.0},
        {50.0, 55.0, 59.0, 60.0},
        {50.0, 50.0, 50.0, 50.0}
    };

    // 光照单位为勒克斯；与湿度表使用相同的展厅顺序和时间周期。
    private static final int[][] LIGHT = {
        {120, 120, 120, 120},
        {120, 220, 120, 120},
        {120, 230, 120, 120},
        {120, 220, 120, 120},
        {120, 200, 120, 120},
        {240, 175, 215, 180},
        {220, 180, 220, 180},
        {180, 180, 230, 180},
        {180, 180, 190, 180},
        {175, 175, 180, 205},
        {190, 190, 180, 210},
        {120, 120, 120, 120}
    };

    // 题目提供的是静态接口，无需创建模拟器对象。
    private MuseumFeed() { }

    /** 返回展厅名的副本，下标保持不变。 */
    public static String[] getGalleryNames() {
        return GALLERIES.clone();
    }

    /** 返回指定展厅、指定小时的湿度。 */
    public static double getHumidity(int galleryId, int hour) {
        checkInputs(galleryId, hour);
        return HUMIDITY[hour % HUMIDITY.length][galleryId];
    }

    /** 返回指定展厅、指定小时的光照强度。 */
    public static int getLight(int galleryId, int hour) {
        checkInputs(galleryId, hour);
        return LIGHT[hour % LIGHT.length][galleryId];
    }

    /** 学生程序在派工时调用。这里只记录事件，巡检员的忙闲状态由学生程序管理。 */
    public static void requestVisit(int hour, int galleryId, String specialist) {
        checkInputs(galleryId, hour);
        if (!"Climate".equals(specialist) && !"Lighting".equals(specialist)) {
            throw new IllegalArgumentException("Unknown specialist: " + specialist);
        }
        System.out.printf("Hour %d | %s visit started in %s%n", hour, specialist, GALLERIES[galleryId]);
    }

    /** 检查展厅编号和模拟小时，便于发现参数顺序写错等问题。 */
    private static void checkInputs(int galleryId, int hour) {
        if (galleryId < 0 || galleryId >= GALLERIES.length) {
            throw new IllegalArgumentException("Unknown gallery ID: " + galleryId);
        }
        if (hour < 0) {
            throw new IllegalArgumentException("Hour must be at least zero.");
        }
    }
}
