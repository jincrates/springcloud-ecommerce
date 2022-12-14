package me.jincrates.catalogservice.service;

import me.jincrates.catalogservice.jpa.CatalogEntity;

public interface CatalogService {
    Iterable<CatalogEntity> getAllCatalogs();
}
