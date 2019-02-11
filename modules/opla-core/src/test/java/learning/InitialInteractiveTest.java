package learning;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.representation.Architecture;
import arquitetura.representation.Package;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class InitialInteractiveTest {


    public static final Logger LOGGER = Logger.getLogger(InitialInteractiveTest.class);

    @Test
    public void countElements() throws Exception {

        List<String> xmis = Arrays.asList(
                "/home/wmfsystem/workspace/plas/agm/agm.uml",
                "/home/wmfsystem/workspace/plas/vinicius/2/sem_interacao/VAR_All_agm-6168413134.uml",
                "/home/wmfsystem/workspace/plas/vinicius/2/sem_interacao/VAR_All_agm-7845213531.uml",
                "/home/wmfsystem/workspace/plas/vinicius/2/sem_interacao/VAR_All_agm-9181131839.uml",
                "/home/wmfsystem/workspace/plas/vinicius/2/sem_interacao/VAR_All_agm-9184713578.uml",
                "/home/wmfsystem/workspace/plas/vinicius/2/experimento_choma/VAR_All_agm-1983211558.uml",
                "/home/wmfsystem/workspace/plas/vinicius/2/experimento_choma/VAR_All_agm-2139224476.uml",
                "/home/wmfsystem/workspace/plas/vinicius/2/experimento_choma/VAR_All_agm-4399228137.uml",
                "/home/wmfsystem/workspace/plas/vinicius/2/experimento_choma/VAR_All_agm-5399919845.uml",
                "/home/wmfsystem/workspace/plas/vinicius/2/experimento_choma/VAR_All_agm-6886372127.uml",
                "/home/wmfsystem/workspace/plas/vinicius/2/experimento_choma/VAR_All_agm-7117618645.uml",
                "/home/wmfsystem/workspace/plas/vinicius/2/experimento_choma/VAR_All_agm-7125411918.uml"
        );


        ArchitectureBuilder architectureBuilder = new ArchitectureBuilder();
        List<Architecture> arrayList = xmis.stream().map(x -> {
            try {
                return architectureBuilder.create(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());


        for (Architecture architecture : arrayList) {
            System.out.println();
            System.out.println(architecture.getName());
            for (Package allPackage : architecture.getAllPackages()) {
                System.out.println(allPackage.getName() + ": " + allPackage.getAllConcerns().toString());
            }
        }
    }

    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(date.getTime());
    }
}



