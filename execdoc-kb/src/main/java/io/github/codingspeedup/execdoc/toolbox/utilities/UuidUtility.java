package io.github.codingspeedup.execdoc.toolbox.utilities;

import com.devskiller.friendly_id.FriendlyId;

import java.util.UUID;
import java.util.stream.IntStream;

public class UuidUtility {

    public static String nextUuid() {
        return UUID.randomUUID().toString();
    }

    public static String nextCompactUuid() {
        return toCompactUuid(UUID.randomUUID());
    }

    public static String nextFriendlyUuid() {
        return toFriendlyUuid(UUID.randomUUID());
    }

    public static String toCompactUuid(UUID uuid) {
        return uuid.toString().replace("-", "");
    }

    public static String toFriendlyUuid(UUID uuid) {
        return FriendlyId.toFriendlyId(uuid);
    }

    public static void main(String[] args) {
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);
        System.out.println(toCompactUuid(uuid));
        System.out.println(toFriendlyUuid(uuid));
        System.out.println();

        IntStream.range(0, 2000).forEach(i -> System.out.println(nextFriendlyUuid()));
    }

}
