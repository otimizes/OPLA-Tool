package br.otimizes.oplatool.domain.util;

import org.apache.commons.lang.RandomStringUtils;

/**
 * @author elf
 */
public class IdUtil {

    private static final int ID_LENGTH = 10;

    public static String generateUniqueId() {
        String id = RandomStringUtils.randomNumeric(ID_LENGTH);
        if (id.contains("0"))
            return id.replaceAll("0", "1");
        return id;
    }
}
