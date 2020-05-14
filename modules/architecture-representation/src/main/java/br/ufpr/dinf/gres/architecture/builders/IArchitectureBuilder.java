package br.ufpr.dinf.gres.architecture.builders;

import br.ufpr.dinf.gres.architecture.representation.Architecture;

/**
 * Interface of architecture builder
 */
public interface IArchitectureBuilder {
    Architecture create(String xmiFilePath);
}
