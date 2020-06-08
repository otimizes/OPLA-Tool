package br.otimizes.oplatool.patterns.repositories;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.otimizes.oplatool.architecture.builders.ArchitectureBuilderPapyrus;
import br.otimizes.oplatool.architecture.representation.Architecture;

/**
 * The Class ArchitectureRepository.
 *
 * @author arthur
 */
public class ArchitectureRepository {

    /** The Constant MICROWAVE_OVEN_SOFTWARE. */
    public static final String MICROWAVE_OVEN_SOFTWARE = "/Users/giovaniguizzo/NetBeansProjects/OPLA-Patterns/MicrowaveOvenSoftware/Papyrus/MicrowaveOvenSoftware.uml";
    
    /** The Constant SERVICE_AND_SUPPORT_SYSTEM. */
    public static final String SERVICE_AND_SUPPORT_SYSTEM = "/Users/giovaniguizzo/NetBeansProjects/OPLA-Patterns/ServiceAndSupportSystem/Papyrus/ServiceAndSupportSystem.uml";
    
    /** The Constant AGM. */
    public static final String AGM = "/Users/giovaniguizzo/NetBeansProjects/OPLA-Patterns/agm/Papyrus/agm.uml";
    
    /** The Constant MOBILE_MEDIA. */
    public static final String MOBILE_MEDIA = "/Users/giovaniguizzo/NetBeansProjects/OPLA-Patterns/MobileMedia/Papyrus/MobileMedia.uml";
    
    /** The Constant BET. */
    public static final String BET = "/Users/giovaniguizzo/NetBeansProjects/OPLA-Patterns/BeT/Papyrus/BeT.uml";

    /** The Constant MICROWAVE_TEST. */
    //Arquivos de teste
    public static final String MICROWAVE_TEST = "test/br/ufpr/inf/opla/br.otimizes.oplatool.patterns/resources/microwave/MicrowaveOvenSoftware.uml";

    /** The Constant STRATEGY_MODELS. */
    public static final String[] STRATEGY_MODELS = new String[]{
            "test/br/ufpr/inf/opla/br.otimizes.oplatool.patterns/resources/strategy/Verify.uml",
            "test/br/ufpr/inf/opla/br.otimizes.oplatool.patterns/resources/strategy/Verify2.uml",
            "test/br/ufpr/inf/opla/br.otimizes.oplatool.patterns/resources/strategy/Verify3.uml",
            "test/br/ufpr/inf/opla/br.otimizes.oplatool.patterns/resources/strategy/Apply1.uml",
            "test/br/ufpr/inf/opla/br.otimizes.oplatool.patterns/resources/strategy/Apply2.uml"
    };

    /** The Constant BRIDGE_MODELS. */
    public static final String[] BRIDGE_MODELS = new String[]{
            "test/br/ufpr/inf/opla/br.otimizes.oplatool.patterns/resources/bridge/Verify.uml",
            "test/br/ufpr/inf/opla/br.otimizes.oplatool.patterns/resources/bridge/Apply.uml"
    };

    /** The Constant ADAPTER_MODELS. */
    public static final String[] ADAPTER_MODELS = new String[]{
            "test/br/ufpr/inf/opla/br.otimizes.oplatool.patterns/resources/adapter/Verify.uml"
    };

    /** The Constant MEDIATOR_MODELS. */
    public static final String[] MEDIATOR_MODELS = new String[]{
            "test/br/ufpr/inf/opla/br.otimizes.oplatool.patterns/resources/mediator/Apply.uml"
    };

    /** The Constant OTHER_MODELS. */
    public static final String[] OTHER_MODELS = new String[]{
            "test/br/ufpr/inf/opla/br.otimizes.oplatool.patterns/resources/other/Model1.uml",
            "test/br/ufpr/inf/opla/br.otimizes.oplatool.patterns/resources/other/Model2.uml",
            "test/br/ufpr/inf/opla/br.otimizes.oplatool.patterns/resources/other/Model3.uml",
            "test/br/ufpr/inf/opla/br.otimizes.oplatool.patterns/resources/other/Model4.uml",
            "test/br/ufpr/inf/opla/br.otimizes.oplatool.patterns/resources/other/Model5.uml",
            "test/br/ufpr/inf/opla/br.otimizes.oplatool.patterns/resources/other/Model6.uml",
            "test/br/ufpr/inf/opla/br.otimizes.oplatool.patterns/resources/other/Model7.uml",
            "test/br/ufpr/inf/opla/br.otimizes.oplatool.patterns/resources/other/Model8.uml",
            "test/br/ufpr/inf/opla/br.otimizes.oplatool.patterns/resources/other/Model9.uml",
            "test/br/ufpr/inf/opla/br.otimizes.oplatool.patterns/resources/other/Model10.uml"
    };

    /** The Constant OUTPUT. */
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

    /** The Constant ARCHITECTURE_BUILDER. */
    private static final ArchitectureBuilderPapyrus ARCHITECTURE_BUILDER = new ArchitectureBuilderPapyrus();
    
    /** The current architecture. */
    private static Architecture CURRENT_ARCHITECTURE;

    /**
     * Gets the or create directory.
     *
     * @param path the path
     * @return the or create directory
     */
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

    /**
     * Gets the architecture.
     *
     * @param path the path
     * @return the architecture
     */
    public static Architecture getArchitecture(String path) {
        try {
            setCurrentArchitecture(ARCHITECTURE_BUILDER.create(path));
            return getCurrentArchitecture();
        } catch (Exception ex) {
            Logger.getLogger(ArchitectureRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Gets the pla path.
     *
     * @param name the name
     * @return the pla path
     */
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

    /**
     * Gets the current architecture.
     *
     * @return the current architecture
     */
    public static Architecture getCurrentArchitecture() {
        return CURRENT_ARCHITECTURE;
    }

    /**
     * Sets the current architecture.
     *
     * @param currentArchitecture the new current architecture
     */
    public static void setCurrentArchitecture(Architecture currentArchitecture) {
        CURRENT_ARCHITECTURE = currentArchitecture;
    }

}
