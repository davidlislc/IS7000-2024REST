package org.is70002024.market.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Giftcard.
 */
@Entity
@Table(name = "giftcard")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Giftcard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "giftcardamount", nullable = false)
    private Double giftcardamount;

    @NotNull
    @Column(name = "add_date", nullable = false)
    private LocalDate addDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Giftcard id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Giftcard name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getGiftcardamount() {
        return this.giftcardamount;
    }

    public Giftcard giftcardamount(Double giftcardamount) {
        this.setGiftcardamount(giftcardamount);
        return this;
    }

    public void setGiftcardamount(Double giftcardamount) {
        this.giftcardamount = giftcardamount;
    }

    public LocalDate getAddDate() {
        return this.addDate;
    }

    public Giftcard addDate(LocalDate addDate) {
        this.setAddDate(addDate);
        return this;
    }

    public void setAddDate(LocalDate addDate) {
        this.addDate = addDate;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Giftcard user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Giftcard)) {
            return false;
        }
        return getId() != null && getId().equals(((Giftcard) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Giftcard{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", giftcardamount=" + getGiftcardamount() +
            ", addDate='" + getAddDate() + "'" +
            "}";
    }
}
