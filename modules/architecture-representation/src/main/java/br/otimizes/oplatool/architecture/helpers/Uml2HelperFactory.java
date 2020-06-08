package br.otimizes.oplatool.architecture.helpers;


/**
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class Uml2HelperFactory {

    public static ThreadLocal<Uml2Helper> instance = ThreadLocal.withInitial(() -> {
        Uml2Helper instance = Uml2Helper.instance.get();
        instance.setSMartyProfile();
        return instance;
    });

}