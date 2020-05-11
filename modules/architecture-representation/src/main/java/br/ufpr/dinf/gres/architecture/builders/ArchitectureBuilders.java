package br.ufpr.dinf.gres.architecture.builders;

public enum ArchitectureBuilders implements IArchitectureBuilders {
    SMARTY {
        @Override
        public IArchitectureBuilder getBuilder() {
            return new ArchitectureBuilderSMarty();
        }
    },
    PAPYRUS {
        @Override
        public IArchitectureBuilder getBuilder() {
            return new ArchitectureBuilderPapyrus();
        }
    }
}
