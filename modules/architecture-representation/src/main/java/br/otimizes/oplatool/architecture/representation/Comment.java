package br.otimizes.oplatool.architecture.representation;

import br.otimizes.isearchai.learning.MLElement;

import java.util.Collection;
import java.util.HashSet;

public class Comment extends Element {

    private String value;

    public Comment(String name, String typeElement) {
        super(name, null, typeElement, null, null);
        if (typeElement == null) super.setTypeElement("comment");
    }

    public Comment(String name) {
        super(name, null, "comment", null, null);
    }

    @Override
    public Collection<Concern> getAllConcerns() {
        return new HashSet<>();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
