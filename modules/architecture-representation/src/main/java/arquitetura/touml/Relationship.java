package arquitetura.touml;

import arquitetura.exceptions.NotSuppportedOperation;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public interface Relationship {

    public Relationship createRelation();

    public Relationship between(String idElement) throws NotSuppportedOperation;

    public Relationship and(String idElement) throws NotSuppportedOperation;

    public String build();

    public Relationship withMultiplicy(String string) throws NotSuppportedOperation;

}