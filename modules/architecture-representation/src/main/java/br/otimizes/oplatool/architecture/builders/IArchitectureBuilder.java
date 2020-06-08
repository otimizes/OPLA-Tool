package br.otimizes.oplatool.architecture.builders;

import br.otimizes.oplatool.architecture.representation.Architecture;

/**
 * Interface of architecture builder
 */
public interface IArchitectureBuilder {
    Architecture create(String xmiFilePath);
}
