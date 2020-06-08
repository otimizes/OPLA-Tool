package br.otimizes.oplatool.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "experiments")
public class Experiment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "algorithm", nullable = false)
    private String algorithm;


    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "hash")
    private String hash;

    public Experiment() {
    }

    public Experiment(String id) {
        this.id = id;
    }

    public Experiment(String name, String algorithm, String description) {
        this.name = name;
        this.algorithm = algorithm;
        this.description = description;
        this.createdAt = setCreatedAt();
    }

    public Experiment(String name, String algorithm, String description, String hash) {
        this.name = name;
        this.algorithm = algorithm;
        this.description = description;
        this.hash = hash;
        this.createdAt = setCreatedAt();
    }

    private String setCreatedAt() {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return dt.format(new Date());
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (!getClass().equals(other.getClass())) {
            return false;
        }
        Experiment castOther = (Experiment) other;
        return Objects.equals(id, castOther.id) && Objects.equals(name, castOther.name)
                && Objects.equals(algorithm, castOther.algorithm) && Objects.equals(createdAt, castOther.createdAt)
                && Objects.equals(description, castOther.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, algorithm, createdAt, description);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append("id", id).append("name", name)
                .append("algorithm", algorithm).append("createdAt", createdAt).append("description", description)
                .toString();
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
