package com.back.barberbook.controller;

import com.back.barberbook.dto.ClienteDTO;
import com.back.barberbook.dto.ViewsDTO;
import com.back.barberbook.repository.UsuarioRepository;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
@Validated
public class ClienteController {

    private final UsuarioRepository repo;

    public ClienteController(UsuarioRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MappingJacksonValue> getCliente(
            @PathVariable UUID id,
            @RequestParam(name = "view", defaultValue = "public") String view
    ) {
        return repo.findById(id)
            .map(u -> {
                var dto = new ClienteDTO.Response(
                    u.getId(), u.getNome(), u.getEmail(), u.getTelefone(), u.getCpf()
                );

                var wrapper = new MappingJacksonValue(dto);
                var targetView = "details".equalsIgnoreCase(view)
                    ? ViewsDTO.Details.class
                    : ViewsDTO.Public.class;

                wrapper.setSerializationView(targetView);
                return ResponseEntity.ok(wrapper);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MappingJacksonValue> updateCliente(
            @PathVariable UUID id,
            @RequestBody ClienteDTO.UpdateRequest req
    ) {
        return repo.findById(id)
            .map(u -> {
                if (req.nome() != null) u.setNome(req.nome().trim());
                if (req.email() != null) u.setEmail(req.email().trim());
                if (req.telefone() != null) u.setTelefone(req.telefone().trim());
                repo.save(u);

                var dto = new ClienteDTO.Response(
                    u.getId(), u.getNome(), u.getEmail(), u.getTelefone(), u.getCpf()
                );

                var wrapper = new MappingJacksonValue(dto);
                wrapper.setSerializationView(ViewsDTO.Public.class);
                return ResponseEntity.ok(wrapper);
            })
            .orElse(ResponseEntity.notFound().build());
    }
}