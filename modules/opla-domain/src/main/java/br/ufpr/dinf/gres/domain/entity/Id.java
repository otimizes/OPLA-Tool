package br.ufpr.dinf.gres.domain.entity;

import org.apache.commons.lang.RandomStringUtils;

/**
 * @author elf
 */
public class Id {

    private static final int ID_LENGTH = 10;

    public static String generateUniqueId() {
        String id = RandomStringUtils.randomNumeric(ID_LENGTH);
        if (id.contains("0"))
            return id.replaceAll("0", "1"); // remover 0 pois dava conflito com o br.ufpr.dinf.gres.opla.db na hora do select
        return id;
    }

}
