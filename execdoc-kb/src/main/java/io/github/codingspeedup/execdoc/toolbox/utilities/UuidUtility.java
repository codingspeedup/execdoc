package io.github.codingspeedup.execdoc.toolbox.utilities;

import com.devskiller.friendly_id.FriendlyId;

import java.util.UUID;

public class UuidUtility {

    public static String nextUuid() {
        return UUID.randomUUID().toString();
    }

    public static String nextCompactUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String nextFriendlyUuid() {
        return FriendlyId.toFriendlyId(UUID.randomUUID());
    }

}
