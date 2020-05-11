package br.ufpr.dinf.gres.architecture.builders;

import br.ufpr.dinf.gres.architecture.representation.Architecture;

public interface IArchitectureBuilder {
    Architecture create(String xmiFilePath);
}
