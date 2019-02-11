package arquitetura.touml;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class Argument {

    private final String name;
    private final Types.Type type;
    private final String direction;

    private Argument(String name, Types.Type type, String direction) {
        this.name = name;
        this.type = type;
        this.direction = direction;
    }

    public static Argument create(String name, Types.Type type, String direction) {
        return new Argument(name, type, direction);
    }

    public String getName() {
        return name;
    }

    public Types.Type getType() {
        return type;
    }

    public String getDirection() {
        return direction;
    }
}