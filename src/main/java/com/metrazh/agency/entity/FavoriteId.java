package com.metrazh.agency.entity;

import java.io.Serializable;
import java.util.Objects;

/** Складений первинний ключ (client_id, object_id) для таблиці favorites. */
public class FavoriteId implements Serializable {

    private Integer client;
    private Integer realEstate;

    public FavoriteId() {
    }

    public FavoriteId(Integer client, Integer realEstate) {
        this.client = client;
        this.realEstate = realEstate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavoriteId)) return false;
        FavoriteId that = (FavoriteId) o;
        return Objects.equals(client, that.client) && Objects.equals(realEstate, that.realEstate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(client, realEstate);
    }
}
