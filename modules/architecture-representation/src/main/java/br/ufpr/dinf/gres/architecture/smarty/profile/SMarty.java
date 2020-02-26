package br.ufpr.dinf.gres.architecture.smarty.profile;

import br.ufpr.dinf.gres.architecture.exceptions.ModelNotFoundException;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class SMarty {

    public static void main(String[] args) throws ModelNotFoundException {
        new ProfileSMarty("smarty.profile");
    }

}
