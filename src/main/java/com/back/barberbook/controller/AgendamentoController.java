package com.back.barberbook.controller;

import com.back.barberbook.dto.AgendamentoDTO;
import com.back.barberbook.service.AgendamentoService;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@Validated
public class AgendamentoController {

    private final AgendamentoService service;

    public AgendamentoController(AgendamentoService service) {
        this.service = service;
    }

    @PostMapping("/agendamentos")
    public ResponseEntity<?> criar(@RequestBody AgendamentoDTO.Request req) {
        try {
            AgendamentoDTO.Response resp = service.criar(req);
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/disponibilidade")
    public ResponseEntity<?> disponibilidade(
            @RequestParam("barbeiroId") UUID barbeiroId,
            @RequestParam("data") String dataIso
    ) {
        try {
            LocalDate dia = LocalDate.parse(dataIso);
            List<String> horarios = service.listarHorariosLivres(barbeiroId, dia);
            return ResponseEntity.ok(Map.of("horarios", horarios));
        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data inválida (use yyyy-MM-dd).");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}