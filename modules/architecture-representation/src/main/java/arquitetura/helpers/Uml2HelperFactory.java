package arquitetura.helpers;


/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class Uml2HelperFactory {

    private static Uml2Helper instance;

    public static Uml2Helper getUml2Helper() {
        if (instance == null) {
            instance = Uml2Helper.getInstance();
            instance.setSMartyProfile();
        }
        return instance;
    }

}