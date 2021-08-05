package br.otimizes.oplatool.architecture.smarty;


import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.architecture.representation.*;

import java.util.ArrayList;

/**
 * Create a new list of types using default types + new types created by elements
 */
public class GenerateTypeSMarty {

    public GenerateTypeSMarty() {
    }

    private static final GenerateTypeSMarty INSTANCE = new GenerateTypeSMarty();

    public static GenerateTypeSMarty getInstance() {
        return INSTANCE;
    }

    public void generate(Architecture architecture) {
        ArrayList<TypeSmarty> typeSmarties = new ArrayList<>();
        typeSmarties.add(getTypeSmarty("TYPE#1", "", "boolean", "false", true, true));
        typeSmarties.add(getTypeSmarty("TYPE#2", "", "byte", "'0'", true, true));
        typeSmarties.add(getTypeSmarty("TYPE#3", "", "char", "' '", true, true));
        typeSmarties.add(getTypeSmarty("TYPE#4", "", "double", "0.0d", true, true));
        typeSmarties.add(getTypeSmarty("TYPE#5", "", "float", "0.0f", true, true));
        typeSmarties.add(getTypeSmarty("TYPE#6", "", "int", "0", true, true));
        typeSmarties.add(getTypeSmarty("TYPE#7", "", "long", "0.0l", true, true));
        typeSmarties.add(getTypeSmarty("TYPE#8", "", "short", "0", true, true));
        typeSmarties.add(getTypeSmarty("TYPE#9", "", "void", "", true, true));
        typeSmarties.add(getTypeSmarty("TYPE#10", "java.lang", "Boolean", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#11", "java.lang", "Byte", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#12", "java.lang", "Character", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#13", "java.lang", "Double", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#14", "java.lang", "Enum", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#15", "java.lang", "Exception", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#16", "java.lang", "Float", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#17", "java.lang", "Integer", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#18", "java.lang", "Long", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#19", "java.lang", "Math", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#20", "java.lang", "Number", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#21", "java.lang", "Object", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#22", "java.lang", "Package", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#23", "java.lang", "Short", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#24", "java.lang", "String", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#25", "java.lang", "StringBuffer", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#26", "java.lang", "StringBuilder", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#27", "java.lang", "Thread", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#28", "java.lang", "Void", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#29", "java.util", "Arrays", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#30", "java.util", "Collections", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#31", "java.util", "Date", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#32", "java.util", "EnumMap", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#33", "java.util", "EnumSet", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#34", "java.util", "EventListener", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#35", "java.util", "HashMap", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#36", "java.util", "HashSet", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#37", "java.util", "HashTable", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#38", "java.util", "LinkedHashMap", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#39", "java.util", "LinkedHashSet", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#40", "java.util", "LinkedList", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#41", "java.util", "List", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#42", "java.util", "Map", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#43", "java.util", "Queue", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#44", "java.util", "Random", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#45", "java.util", "Scanner", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#46", "java.util", "Set", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#47", "java.util", "Stack", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#48", "java.util", "Timer", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#49", "java.util", "TreeMap", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#50", "java.util", "TreeSet", "null", false, true));
        typeSmarties.add(getTypeSmarty("TYPE#51", "java.util", "Vector", "null", false, true));
        for (Class clazz : architecture.getClasses()) {
            typeSmarties.add(getTypeSmarty(clazz.getId(), clazz.getName(), clazz.getName(), "null", false, false));
        }
        for (Package pkg : architecture.getAllPackages()) {
            for (Class clazz : pkg.getAllClasses()) {
                typeSmarties.add(getTypeSmarty(clazz.getId(), clazz.getName(), clazz.getName(), "null", false, false));
            }
            for (Interface clazz : pkg.getAllInterfaces()) {
                typeSmarties.add(getTypeSmarty(clazz.getId(), clazz.getName(), clazz.getName(), "null", false, false));
            }
        }
        for (Interface clazz : architecture.getInterfaces()) {
            typeSmarties.add(getTypeSmarty(clazz.getId(), clazz.getName(), clazz.getName(), "null", false, false));
        }
        architecture.setTypes(typeSmarties);
    }

    private TypeSmarty getTypeSmarty(String id, String path, String name, String value, Boolean primitive, Boolean standard) {
        TypeSmarty smartyType = new TypeSmarty();
        smartyType.setId(id);
        smartyType.setPath(path);
        smartyType.setName(name);
        smartyType.setValue(value);
        smartyType.setPrimitive(primitive);
        smartyType.setStandard(standard);
        return smartyType;
    }
}
