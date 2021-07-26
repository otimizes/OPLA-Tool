package br.otimizes.oplatool.architecture.papyrus.touml;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Types
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class Types {

    public static final Type BOOLEAN = new Type() {
        public String getName() {
            return "Boolean";
        }
    };
    public static final Type BYTE = new Type() {
        public String getName() {
            return "Byte";
        }
    };
    public static final Type CHAR = new Type() {
        public String getName() {
            return "Char";
        }
    };
    public static final Type SHORT = new Type() {
        public String getName() {
            return "Short";
        }
    };
    public static final Type STRING = new Type() {
        public String getName() {
            return "String";
        }
    };
    public static final Type INTEGER = new Type() {
        public String getName() {
            return "Integer";
        }
    };
    public static final Type DOUBLE = new Type() {
        public String getName() {
            return "Double";
        }
    };
    public static final Type REAL = new Type() {
        public String getName() {
            return "Real";
        }
    };
    public static final Type FLOAT = new Type() {
        public String getName() {
            return "float";
        }
    };
    public static final Type LONG = new Type() {
        public String getName() {
            return "Longer";
        }
    };

    public static Type custom(final String customType) {
        return () -> customType;
    }

    public static boolean isCustomType(String userType) {
        boolean custom = true;
        for (Field type : getNativeTypes()) {
            try {
                if (type.getName().equalsIgnoreCase(userType)) {
                    custom = false;
                    break;
                }
            } catch (Exception ignored) {
            }
        }
        return custom;
    }

    private static List<Field> getNativeTypes() {
        List<Field> staticFields = new ArrayList<>();
        for (Field field : Types.class.getDeclaredFields())
            if (Modifier.isStatic(field.getModifiers()))
                staticFields.add(field);
        return staticFields;
    }

    public static Type getByName(final String type) {
        return () -> type;
    }

    public interface Type {
        String getName();
    }
}