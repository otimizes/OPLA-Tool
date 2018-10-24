package arquitetura.touml;


import org.eclipse.emf.common.util.Enumerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public enum VisibilityKind implements Enumerator {
    PUBLIC_LITERAL(0, "public", "public"),
    PRIVATE_LITERAL(1, "private", "private"),
    PROTECTED_LITERAL(2, "protected", "protected"),
    PACKAGE_LITERAL(3, "package", "package");

    public static final int PUBLIC = 0;
    public static final int PRIVATE = 1;
    public static final int PROTECTED = 2;
    public static final int PACKAGE = 3;

    private static final VisibilityKind[] VALUES_ARRAY = new VisibilityKind[]{
            PUBLIC_LITERAL, PRIVATE_LITERAL, PROTECTED_LITERAL, PACKAGE_LITERAL,};

    public static final List<VisibilityKind> VALUES = Collections
            .unmodifiableList(Arrays.asList(VALUES_ARRAY));
    private final int value;
    private final String name;
    private final String literal;

    private VisibilityKind(int value, String name, String literal) {
        this.value = value;
        this.name = name;
        this.literal = literal;
    }

    public static VisibilityKind get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            VisibilityKind result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    public static VisibilityKind getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            VisibilityKind result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    public static VisibilityKind get(int value) {
        switch (value) {
            case PUBLIC:
                return PUBLIC_LITERAL;
            case PRIVATE:
                return PRIVATE_LITERAL;
            case PROTECTED:
                return PROTECTED_LITERAL;
            case PACKAGE:
                return PACKAGE_LITERAL;
        }
        return null;
    }

    public int getValue() {
        return value;
    }


    public String getName() {
        return name;
    }


    public String getLiteral() {
        return literal;
    }

    @Override
    public String toString() {
        return literal;
    }

}