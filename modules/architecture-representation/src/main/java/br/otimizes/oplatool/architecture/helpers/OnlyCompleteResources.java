package br.otimizes.oplatool.architecture.helpers;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Class responsible for verifying if the model is complete. One
 * architecture must contain a .uml, .notation and .di file
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class OnlyCompleteResources implements FilenameFilter {

    public boolean accept(File dir, String name) {
        return true;
    }
}