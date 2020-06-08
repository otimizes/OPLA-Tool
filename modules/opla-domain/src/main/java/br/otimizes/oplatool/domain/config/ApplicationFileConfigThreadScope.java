package br.otimizes.oplatool.domain.config;

import br.otimizes.oplatool.domain.OPLAThreadScope;

public class ApplicationFileConfigThreadScope {
    public static String getDirectoryToSaveModels() {
        return OPLAThreadScope.config.get().getDirectoryToSaveModels();
    }

    public static String getDirectoryToExportModels() {
        return OPLAThreadScope.config.get().getDirectoryToExportModels();
    }

    public static String getPathToProfile() {
        return OPLAThreadScope.config.get().getPathToProfile();
    }

    public static String getPathToProfileConcern() {
        return OPLAThreadScope.config.get().getPathToProfileConcern();
    }

    public static String getPathToTemplateModelsDirectory() {
        return OPLAThreadScope.config.get().getPathToTemplateModelsDirectory();
    }

    public static boolean hasSmartyProfile() {
        return !OPLAThreadScope.config.get().getPathToProfile().isEmpty() && getPathToProfile() != null;
    }

    public static boolean hasConcernsProfile() {
        return !OPLAThreadScope.config.get().getPathToProfileConcern().isEmpty() && getPathToProfileConcern() != null;
    }

    public static boolean hasRelationsShipProfile() {
        return !OPLAThreadScope.config.get().getPathToProfileRelationships().isEmpty() && getPathToProfileRelationships() != null;
    }

    public static boolean hasPatternsProfile() {
        return !OPLAThreadScope.config.get().getPathToProfilePatterns().isEmpty() && getPathToProfilePatterns() != null;
    }

    public static String getPathToProfileRelationships() {
        return OPLAThreadScope.config.get().getPathToProfileRelationships();
    }

    public static String getPathToProfilePatterns() {
        return OPLAThreadScope.config.get().getPathToProfilePatterns();
    }

    public static String getPathSmarty() {
        return OPLAThreadScope.config.get().getPathSmarty();
    }
}