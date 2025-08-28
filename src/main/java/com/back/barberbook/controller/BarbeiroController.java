package com.back.barberbook.controller;

import com.back.barberbook.repository.BarbeiroListView;
import com.back.barberbook.service.BarbeiroService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/barbeiros")
public class BarbeiroController {

    private final BarbeiroService service;

    public BarbeiroController(BarbeiroService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<BarbeiroListView>> listar() {
        return ResponseEntity.ok(service.listarBarbeirosPublicos());
    }
}