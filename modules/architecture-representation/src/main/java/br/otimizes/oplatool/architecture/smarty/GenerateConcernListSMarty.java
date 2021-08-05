package br.otimizes.oplatool.architecture.smarty;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Concern;

import java.util.ArrayList;

/**
 * Create a new list using default concerns + new ones
 *
 */
public class GenerateConcernListSMarty {

    public GenerateConcernListSMarty() {
    }

    private static final GenerateConcernListSMarty INSTANCE = new GenerateConcernListSMarty();

    public static GenerateConcernListSMarty getInstance() {
        return INSTANCE;
    }

    public void Generate(Architecture architecture){
        ArrayList<Concern> concerns = new ArrayList<>();
        Concern newConcern = new Concern();
        newConcern.setId("STEREOTYPE#1");
        newConcern.setName("mandatory");
        newConcern.setPrimitive(true);
        concerns.add(newConcern);

        newConcern = new Concern();
        newConcern.setId("STEREOTYPE#2");
        newConcern.setName("optional");
        newConcern.setPrimitive(true);
        concerns.add(newConcern);

        newConcern = new Concern();
        newConcern.setId("STEREOTYPE#3");
        newConcern.setName("variationPoint");
        newConcern.setPrimitive(true);
        concerns.add(newConcern);

        newConcern = new Concern();
        newConcern.setId("STEREOTYPE#4");
        newConcern.setName("alternative_OR");
        newConcern.setPrimitive(true);
        concerns.add(newConcern);

        newConcern = new Concern();
        newConcern.setId("STEREOTYPE#5");
        newConcern.setName("alternative_XOR");
        concerns.add(newConcern);

        newConcern = new Concern();
        newConcern.setId("STEREOTYPE#6");
        newConcern.setName("requires");
        newConcern.setPrimitive(true);
        concerns.add(newConcern);

        newConcern = new Concern();
        newConcern.setId("STEREOTYPE#7");
        newConcern.setName("mutex");
        newConcern.setPrimitive(true);
        concerns.add(newConcern);

        for (Concern concern : architecture.getAllConcerns()) {
            if (!concernExist(concerns, concern)) {
                concern.setPrimitive(false);
                concerns.add(concern);
            }
        }
        int id = 8;
        String newID = "STEREOTYPE#" + id;
        for (Concern concern : concerns) {
            if (concern.getId() == null) {
                while (concernIDExist(concerns, newID)) {
                    id++;
                    newID = "STEREOTYPE#" + id;
                }
                concern.setId(newID);
            }
        }
        architecture.setConcerns(concerns);
    }

    private boolean concernExist(ArrayList<Concern> lstConcern, Concern concern) {
        for (Concern c : lstConcern) {
            if (c.getName().equals(concern.getName()))
                return true;
        }
        return false;
    }

    private boolean concernIDExist(ArrayList<Concern> lstConcern, String id) {
        for (Concern c : lstConcern) {
            if (c.getId() != null) {
                if (c.getId().equals(id))
                    return true;
            }
        }
        return false;
    }
}
