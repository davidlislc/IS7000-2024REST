package org.is70002024.market.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.is70002024.market.domain.enumeration.INDEX;

/**
 * A MarketOverview.
 */
@Entity
@Table(name = "market_overview")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MarketOverview implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull
    @Column(name = "jhi_change", nullable = false)
    private Double change;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "ticker", nullable = false)
    private INDEX ticker;

    @NotNull
    @Column(name = "marketdate", nullable = false)
    private LocalDate marketdate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MarketOverview id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public MarketOverview name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return this.price;
    }

    public MarketOverview price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getChange() {
        return this.change;
    }

    public MarketOverview change(Double change) {
        this.setChange(change);
        return this;
    }

    public void setChange(Double change) {
        this.change = change;
    }

    public INDEX getTicker() {
        return this.ticker;
    }

    public MarketOverview ticker(INDEX ticker) {
        this.setTicker(ticker);
        return this;
    }

    public void setTicker(INDEX ticker) {
        this.ticker = ticker;
    }

    public LocalDate getMarketdate() {
        return this.marketdate;
    }

    public MarketOverview marketdate(LocalDate marketdate) {
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
        if (!(o instanceof MarketOverview)) {
            return false;
        }
        return getId() != null && getId().equals(((MarketOverview) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MarketOverview{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", change=" + getChange() +
            ", ticker='" + getTicker() + "'" +
            ", marketdate='" + getMarketdate() + "'" +
            "}";
    }
}
