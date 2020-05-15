package br.ufpr.dinf.gres.architecture.papyrus.touml;

import br.ufpr.dinf.gres.architecture.exceptions.NotSuppportedOperation;

/**
 * Relationship
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public interface Relationship {

    Relationship createRelation();

    Relationship between(String idElement) throws NotSuppportedOperation;

    Relationship and(String idElement) throws NotSuppportedOperation;

    String build();

    Relationship withMultiplicy(String string) throws NotSuppportedOperation;

}