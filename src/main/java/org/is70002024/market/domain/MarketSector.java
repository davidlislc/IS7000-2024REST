package org.is70002024.market.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.is70002024.market.domain.enumeration.SECTOR;

/**
 * A MarketSector.
 */
@Entity
@Table(name = "market_sector")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MarketSector implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private SECTOR name;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull
    @Column(name = "jhi_change", nullable = false)
    private Double change;

    @NotNull
    @Column(name = "marketdate", nullable = false)
    private LocalDate marketdate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MarketSector id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SECTOR getName() {
        return this.name;
    }

    public MarketSector name(SECTOR name) {
        this.setName(name);
        return this;
    }

    public void setName(SECTOR name) {
        this.name = name;
    }

    public Double getPrice() {
        return this.price;
    }

    public MarketSector price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getChange() {
        return this.change;
    }

    public MarketSector change(Double change) {
        this.setChange(change);
        return this;
    }

    public void setChange(Double change) {
        this.change = change;
    }

    public LocalDate getMarketdate() {
        return this.marketdate;
    }

    public MarketSector marketdate(LocalDate marketdate) {
        this.setMarketdate(marketdate);
        return this;
    }

    public void setMarketdate(LocalDate marketdate) {
        this.marketdate = marketdate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MarketSector)) {
            return false;
        }
        return getId() != null && getId().equals(((MarketSector) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MarketSector{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", change=" + getChange() +
            ", marketdate='" + getMarketdate() + "'" +
            "}";
    }
}
