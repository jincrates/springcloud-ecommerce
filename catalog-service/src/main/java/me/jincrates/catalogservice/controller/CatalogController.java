package me.jincrates.catalogservice.controller;

import lombok.RequiredArgsConstructor;
import me.jincrates.catalogservice.controller.response.ResponseCatalog;
import me.jincrates.catalogservice.jpa.CatalogEntity;
import me.jincrates.catalogservice.service.CatalogService;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/catalog-service")
public class CatalogController {

    private final Environment env;
    private final ModelMapper modelMapper;
    private final CatalogService catalogService;

    @GetMapping("/health")
    public String status() {
        return String.format("It's Working in Catalog Service on PORT %s",
                env.getProperty("local.server.port"));
    }

    @GetMapping("/catalogs")
    public ResponseEntity<List<ResponseCatalog>> getCatalogs() {
        Iterable<CatalogEntity> catalogs = catalogService.getAllCatalogs();

        List<ResponseCatalog> result = new ArrayList<>();
        catalogs.forEach(c -> {
            result.add(modelMapper.map(c, ResponseCatalog.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
