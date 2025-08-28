package com.back.barberbook.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public class AgendamentoDTO {

    public record Request(
        UUID userId,
        @NotNull UUID servicoId,
        @NotNull UUID barbeiroId,
        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime dataHorarioIso,
        String formaPagamento
    ) {}

    public record Response(
        UUID id,
        String status,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime dataHorario,
        UUID servicoId,
        UUID barbeiroId,
        UUID userId,
        String formaPagamento
    ) {}
}