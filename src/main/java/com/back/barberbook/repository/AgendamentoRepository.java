package com.back.barberbook.repository;

import com.back.barberbook.entity.Agendamento;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendamentoRepository extends JpaRepository<Agendamento, UUID> {

    boolean existsByBarbeiroIdAndDataHorario(UUID barbeiroId, LocalDateTime dataHorario);

    List<Agendamento> findByBarbeiroIdAndDataHorarioBetween(
        UUID barbeiroId,
        LocalDateTime startInclusive,
        LocalDateTime endExclusive
    );
}