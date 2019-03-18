package br.ufpr.inf.opla.patterns.repositories;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.representation.Architecture;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArchitectureRepository {

    public static final String MICROWAVE_OVEN_SOFTWARE = "/Users/giovaniguizzo/NetBeansProjects/OPLA-Patterns/MicrowaveOvenSoftware/Papyrus/MicrowaveOvenSoftware.uml";
    public static final String SERVICE_AND_SUPPORT_SYSTEM = "/Users/giovaniguizzo/NetBeansProjects/OPLA-Patterns/ServiceAndSupportSystem/Papyrus/ServiceAndSupportSystem.uml";
    public static final String AGM = "/Users/giovaniguizzo/NetBeansProjects/OPLA-Patterns/agm/Papyrus/agm.uml";
    public static final String MOBILE_MEDIA = "/Users/giovaniguizzo/NetBeansProjects/OPLA-Patterns/MobileMedia/Papyrus/MobileMedia.uml";
    public static final String BET = "/Users/giovaniguizzo/NetBeansProjects/OPLA-Patterns/BeT/Papyrus/BeT.uml";

    //Arquivos de teste
    public static final String MICROWAVE_TEST = "test/br/ufpr/inf/opla/patterns/resources/microwave/MicrowaveOvenSoftware.uml";

    public static final String[] STRATEGY_MODELS = new String[]{
            "test/br/ufpr/inf/opla/patterns/resources/strategy/Verify.uml",
            "test/br/ufpr/inf/opla/patterns/resources/strategy/Verify2.uml",
            "test/br/ufpr/inf/opla/patterns/resources/strategy/Verify3.uml",
            "test/br/ufpr/inf/opla/patterns/resources/strategy/Apply1.uml",
            "test/br/ufpr/inf/opla/patterns/resources/strategy/Apply2.uml"
    };

    public static final String[] BRIDGE_MODELS = new String[]{
            "test/br/ufpr/inf/opla/patterns/resources/bridge/Verify.uml",
            "test/br/ufpr/inf/opla/patterns/resources/bridge/Apply.uml"
    };

    public static final String[] ADAPTER_MODELS = new String[]{
            "test/br/ufpr/inf/opla/patterns/resources/adapter/Verify.uml"
    };

    public static final String[] MEDIATOR_MODELS = new String[]{
            "test/br/ufpr/inf/opla/patterns/resources/mediator/Apply.uml"
    };

    public static final String[] OTHER_MODELS = new String[]{
            "test/br/ufpr/inf/opla/patterns/resources/other/Model1.uml",
            "test/br/ufpr/inf/opla/patterns/resources/other/Model2.uml",
            "test/br/ufpr/inf/opla/patterns/resources/other/Model3.uml",
            "test/br/ufpr/inf/opla/patterns/resources/other/Model4.uml",
            "test/br/ufpr/inf/opla/patterns/resources/other/Model5.uml",
            "test/br/ufpr/inf/opla/patterns/resources/other/Model6.uml",
            "test/br/ufpr/inf/opla/patterns/resources/other/Model7.uml",
            "test/br/ufpr/inf/opla/patterns/resources/other/Model8.uml",
            "test/br/ufpr/inf/opla/patterns/resources/other/Model9.uml",
            "test/br/ufpr/inf/opla/patterns/resources/other/Model10.uml"
    };

    public static final String[] OUTPUT = new String[]{
            "Output1",
            "Output2",
            "Output3",
            "Output4",
            "Output5",
            "Output6",
            "Output7",
            "Output8",
            "Output9",
            "Output10"
    };

    private static final ArchitectureBuilder ARCHITECTURE_BUILDER = new ArchitectureBuilder();
    private static Architecture CURRENT_ARCHITECTURE;

    public static File getOrCreateDirectory(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                System.out.println("Não foi possível criar o diretório " + path);
                System.exit(0);
            }
        }
        return directory;
    }

    public static Architecture getArchitecture(String path) {
        try {
            setCurrentArchitecture(ARCHITECTURE_BUILDER.create(path));
            return getCurrentArchitecture();
        } catch (Exception ex) {
            Logger.getLogger(ArchitectureRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String getPlaPath(String name) {
        switch (name) {
            case "agm":
                return AGM;
            case "MicrowaveOvenSoftware":
                return MICROWAVE_OVEN_SOFTWARE;
            case "ServiceAndSupportSystem":
                return SERVICE_AND_SUPPORT_SYSTEM;
            default:
                return null;
        }
    }

    public static Architecture getCurrentArchitecture() {
        return CURRENT_ARCHITECTURE;
    }

    public static void setCurrentArchitecture(Architecture currentArchitecture) {
        CURRENT_ARCHITECTURE = currentArchitecture;
    }

}
