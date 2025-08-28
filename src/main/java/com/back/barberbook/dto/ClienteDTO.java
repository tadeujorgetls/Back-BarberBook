package com.back.barberbook.dto;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.UUID;

public class ClienteDTO {

    public record Response(
        @JsonView(ViewsDTO.Public.class)  UUID id,
        @JsonView(ViewsDTO.Public.class)  String nome,
        @JsonView(ViewsDTO.Details.class) String email,
        @JsonView(ViewsDTO.Details.class) String telefone,
        @JsonView(ViewsDTO.Details.class) String cpf
    ) {}

    public record UpdateRequest(String nome, String email, String telefone) {}
}