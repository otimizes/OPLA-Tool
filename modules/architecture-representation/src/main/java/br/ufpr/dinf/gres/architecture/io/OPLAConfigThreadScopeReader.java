package br.ufpr.dinf.gres.architecture.io;

/**
 * Classe responsável por acesso ao arquivo de configuração
 * <b>application.yaml</b>/
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class OPLAConfigThreadScopeReader {
    /**
     * Diretorio onde a br.ufpr.dinf.gres.arquitetura sera salva para manipulacao Este diretorio
     * pode ser qualquer um com acesso de escrita e leitura.
     *
     * @return
     */
    public static String getDirectoryToSaveModels() {
        return OPLAConfigThreadScope.config.get().getDirectoryToSaveModels();
    }

    public static void setDirectoryToExportModels(String path) {
        OPLAConfigThreadScope.config.get().setDirectoryToExportModels(path);
    }

    /**
     * Diretório onde a br.ufpr.dinf.gres.arquitetura será exportada para que possa ser utilizada.
     * Resultado final
     *
     * @return
     */
    public static String getDirectoryToExportModels() {
        return OPLAConfigThreadScope.config.get().getDirectoryToExportModels();
    }

    /**
     * Path pra o arquivo de profile do SMarty
     *
     * @return
     */
    public static String getPathToProfile() {
        return OPLAConfigThreadScope.config.get().getPathToProfile();
    }

    public static void setPathToProfile(String path) {
        OPLAConfigThreadScope.config.get().setPathToProfile(path);
    }

    /**
     * Path para o arquivo de profile contendo os concerns.
     *
     * @return
     */
    public static String getPathToProfileConcern() {
        return OPLAConfigThreadScope.config.get().getPathToProfileConcern();
    }

    public static void setPathToProfileConcern(String path) {
        OPLAConfigThreadScope.config.get().setPathToProfileConcern(path);
    }

    /**
     * Path para um diretorio contendo os tres arquivos que são usados como
     * template para geração da br.ufpr.dinf.gres.arquitetura. Estes arquivos contem somente um
     * esqueleto. Estes arquivos se encontram na raiz do projeto na pasta
     * filesTemplates. Você pode copiar os arquivos e colocar em qualquer
     * diretório com permissão de leitura.
     *
     * @return
     */
    public static String getPathToTemplateModelsDirectory() {
        return OPLAConfigThreadScope.config.get().getPathToTemplateModelsDirectory();
    }

    public static void setPathToTemplateModelsDirectory(String path) {
        OPLAConfigThreadScope.config.get().setPathToTemplateModelsDirectory(path);
    }

    /**
     * Verifica se existe o perfil smarty configurado
     *
     * @return boolean
     */
    public static boolean hasSmartyProfile() {
        return !OPLAConfigThreadScope.config.get().getPathToProfile().isEmpty() && getPathToProfile() != null;
    }

    /**
     * Verifica se existe o perfil concerns configurado
     *
     * @return boolean
     */
    public static boolean hasConcernsProfile() {
        return !OPLAConfigThreadScope.config.get().getPathToProfileConcern().isEmpty() && getPathToProfileConcern() != null;
    }

    /**
     * Verifica se existe o perfil concerns configurado
     *
     * @return boolean
     */
    public static boolean hasRelationsShipProfile() {
        return !OPLAConfigThreadScope.config.get().getPathToProfileRelationships().isEmpty() && getPathToProfileRelationships() != null;
    }

    public static boolean hasPatternsProfile() {
        return !OPLAConfigThreadScope.config.get().getPathToProfilePatterns().isEmpty() && getPathToProfilePatterns() != null;
    }

    public static String getPathToProfileRelationships() {
        return OPLAConfigThreadScope.config.get().getPathToProfileRelationships();
    }

    public static String getPathToProfilePatterns() {
        return OPLAConfigThreadScope.config.get().getPathToProfilePatterns();
    }

    public static void setPathToProfileRelationships(String path) {
        OPLAConfigThreadScope.config.get().setPathToProfileRelationships(path);
    }

}