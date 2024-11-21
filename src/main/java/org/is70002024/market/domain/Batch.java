package org.is70002024.market.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.is70002024.market.domain.enumeration.Status;

/**
 * A Batch.
 */
@Entity
@Table(name = "batch")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Batch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "job", nullable = false)
    private String job;

    @NotNull
    @Column(name = "rundate", nullable = false)
    private LocalDate rundate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "batchstatus", nullable = false)
    private Status batchstatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Batch id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Batch name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return this.job;
    }

    public Batch job(String job) {
        this.setJob(job);
        return this;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public LocalDate getRundate() {
        return this.rundate;
    }

    public Batch rundate(LocalDate rundate) {
        this.setRundate(rundate);
        return this;
    }

    public void setRundate(LocalDate rundate) {
        this.rundate = rundate;
    }

    public Status getBatchstatus() {
        return this.batchstatus;
    }

    public Batch batchstatus(Status batchstatus) {
        this.setBatchstatus(batchstatus);
        return this;
    }

    public void setBatchstatus(Status batchstatus) {
        this.batchstatus = batchstatus;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Batch user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Batch)) {
            return false;
        }
        return getId() != null && getId().equals(((Batch) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Batch{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", job='" + getJob() + "'" +
            ", rundate='" + getRundate() + "'" +
            ", batchstatus='" + getBatchstatus() + "'" +
            "}";
    }
}
