package com.back.barberbook.service;

import com.back.barberbook.entity.Agendamento;
import com.back.barberbook.repository.AgendamentoRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class DisponibilidadeService {

    private static final int SLOT_MINUTES = 30;

    private final AgendamentoRepository agendamentoRepository;

    public DisponibilidadeService(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    public List<String> listarHorarios(UUID barbeiroId, LocalDate data) {
        LocalDateTime inicioDia = data.atStartOfDay();
        LocalDateTime fimDiaExclusive = data.plusDays(1).atStartOfDay();

        List<Agendamento> ags = agendamentoRepository
            .findByBarbeiroIdAndDataHorarioBetween(barbeiroId, inicioDia, fimDiaExclusive);

        Set<String> ocupados = new HashSet<>(ags.size());
        for (Agendamento a : ags) {
            ocupados.add(fmtHM(a.getDataHorario().toLocalTime()));
        }

        List<String> grade = gerarGrade(LocalTime.of(9, 0), LocalTime.of(19, 0), SLOT_MINUTES);

        List<String> livres = new ArrayList<>(grade.size());
        for (String h : grade) {
            if (!ocupados.contains(h)) {
                livres.add(h);
            }
        }
        return livres;
    }

    private static List<String> gerarGrade(LocalTime inicioInclusive, LocalTime fimExclusive, int passoMin) {
        List<String> out = new ArrayList<>();
        for (LocalTime t = inicioInclusive; t.isBefore(fimExclusive); t = t.plusMinutes(passoMin)) {
            out.add(fmtHM(t));
        }
        return out;
    }

    private static String fmtHM(LocalTime t) {
        return String.format("%02d:%02d", t.getHour(), t.getMinute());
    }
}