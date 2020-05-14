package br.ufpr.dinf.gres.architecture.smarty;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Concern;

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

    /**
     * Create a new list using default concerns + new ones (from interface and class)
     *
     * @param architecture - input architecture
     */
    public void Generate(Architecture architecture){
        ArrayList<Concern> lstConcern = new ArrayList<>();

        Concern newCon;
        newCon = new Concern();
        newCon.setId("STEREOTYPE#1");
        newCon.setName("mandatory");
        newCon.setPrimitive(true);
        lstConcern.add(newCon);

        newCon = new Concern();
        newCon.setId("STEREOTYPE#2");
        newCon.setName("optional");
        newCon.setPrimitive(true);
        lstConcern.add(newCon);

        newCon = new Concern();
        newCon.setId("STEREOTYPE#3");
        newCon.setName("variationPoint");
        newCon.setPrimitive(true);
        lstConcern.add(newCon);

        newCon = new Concern();
        newCon.setId("STEREOTYPE#4");
        newCon.setName("alternative_OR");
        newCon.setPrimitive(true);
        lstConcern.add(newCon);

        newCon = new Concern();
        newCon.setId("STEREOTYPE#5");
        newCon.setName("alternative_XOR");
        lstConcern.add(newCon);

        newCon = new Concern();
        newCon.setId("STEREOTYPE#6");
        newCon.setName("requires");
        newCon.setPrimitive(true);
        lstConcern.add(newCon);


        newCon = new Concern();
        newCon.setId("STEREOTYPE#7");
        newCon.setName("mutex");
        newCon.setPrimitive(true);
        lstConcern.add(newCon);

        for (Concern c : architecture.getAllConcerns()) {
            if (!concernExist(lstConcern, c)) {
                c.setPrimitive(false);
                lstConcern.add(c);
            }
        }
        int id = 8;
        String newID = "STEREOTYPE#" + id;
        for (Concern c : lstConcern) {
            if (c.getId() == null) {
                while (concernIDExist(lstConcern, newID)) {
                    id++;
                    newID = "STEREOTYPE#" + id;
                }
                c.setId(newID);
            }
        }
        architecture.setLstConcerns(lstConcern);
    }

    /**
     * Verify if concern exist using concern name
     *
     * @param lstConcern - ArrayList of added concern
     * @param concern - new concern to be compared
     * @return  boolean true if list contains concern else false
     */
    private boolean concernExist(ArrayList<Concern> lstConcern, Concern concern) {
        for (Concern c : lstConcern) {
            if (c.getName().equals(concern.getName()))
                return true;
        }
        return false;
    }

    /**
     * Verify if concern exist using concern id
     *
     * @param lstConcern - ArrayList of added concern
     * @param id- id of new concern to be compared
     * @return  boolean true if list contains concern else false
     */
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
