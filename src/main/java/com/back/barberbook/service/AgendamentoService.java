package com.back.barberbook.service;

import com.back.barberbook.dto.AgendamentoDTO;
import com.back.barberbook.entity.Agendamento;
import com.back.barberbook.repository.AgendamentoRepository;
import com.back.barberbook.repository.BarbeiroRepository;
import com.back.barberbook.repository.ServicoRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class AgendamentoService {

    private static final int SLOT_MINUTES = 30;

    private final AgendamentoRepository repo;
    private final ServicoRepository servicoRepo;
    private final BarbeiroRepository barbeiroRepo;
    private final UUID guestUserId;

    public AgendamentoService(
            AgendamentoRepository repo,
            ServicoRepository servicoRepo,
            BarbeiroRepository barbeiroRepo,
            @Value("${app.guest-user-id}") String guestUserIdProp
    ) {
        this.repo = repo;
        this.servicoRepo = servicoRepo;
        this.barbeiroRepo = barbeiroRepo;
        this.guestUserId = UUID.fromString(guestUserIdProp);
    }

    @Transactional
    public AgendamentoDTO.Response criar(AgendamentoDTO.Request req) {
        UUID servicoId = req.servicoId();
        UUID barbeiroId = req.barbeiroId();
        UUID userId = req.userId() != null ? req.userId() : this.guestUserId;

        if (servicoId == null || !servicoRepo.existsById(servicoId)) {
            throw new IllegalArgumentException("Serviço não encontrado.");
        }
        if (barbeiroId == null || !barbeiroRepo.existsById(barbeiroId)) {
            throw new IllegalArgumentException("Barbeiro não encontrado.");
        }
        if (req.dataHorarioIso() == null) {
            throw new IllegalArgumentException("Campo 'dataHorarioIso' é obrigatório.");
        }

        LocalDateTime dataHorario = req.dataHorarioIso();

        if (repo.existsByBarbeiroIdAndDataHorario(barbeiroId, dataHorario)) {
            throw new IllegalStateException("Horário indisponível para o barbeiro selecionado.");
        }

        Agendamento.FormaPagamento forma = normalizeFormaPagamento(req.formaPagamento());

        try {
            Agendamento ag = new Agendamento();
            ag.setUserId(userId);
            ag.setServicoId(servicoId);
            ag.setBarbeiroId(barbeiroId);
            ag.setDataHorario(dataHorario);
            ag.setFormaPagamento(forma);
            ag.setStatus(Agendamento.Status.PENDENTE);

            Agendamento saved = repo.save(ag);

            return new AgendamentoDTO.Response(
                saved.getId(),
                saved.getStatus().name(),
                saved.getDataHorario(),
                saved.getServicoId(),
                saved.getBarbeiroId(),
                saved.getUserId(),
                saved.getFormaPagamento().name()
            );
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Horário indisponível para o barbeiro selecionado.");
        }
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<String> listarHorariosLivres(UUID barbeiroId, LocalDate dia) {
        if (barbeiroId == null || !barbeiroRepo.existsById(barbeiroId)) {
            throw new IllegalArgumentException("Barbeiro inválido.");
        }
        if (dia == null) {
            throw new IllegalArgumentException("Data inválida.");
        }

        LocalDateTime start = LocalDateTime.of(dia, LocalTime.of(9, 0));
        LocalDateTime endExclusive = LocalDateTime.of(dia, LocalTime.of(19, 0));

        List<Agendamento> ags = repo.findByBarbeiroIdAndDataHorarioBetween(barbeiroId, start, endExclusive);

        Set<LocalTime> ocupados = new HashSet<>(ags.size());
        for (Agendamento a : ags) {
            ocupados.add(a.getDataHorario().toLocalTime().withSecond(0).withNano(0));
        }

        List<String> livres = new ArrayList<>();
        LocalDateTime cursor = start;
        while (cursor.isBefore(endExclusive)) {
            LocalTime t = cursor.toLocalTime();
            if (!ocupados.contains(t)) {
                livres.add(String.format("%02d:%02d", t.getHour(), t.getMinute()));
            }
            cursor = cursor.plusMinutes(SLOT_MINUTES);
        }
        return livres;
    }

    private Agendamento.FormaPagamento normalizeFormaPagamento(String raw) {
        if (raw == null) return Agendamento.FormaPagamento.LOJA;
        String up = raw.trim().toUpperCase(Locale.ROOT);
        return switch (up) {
            case "LOJA" -> Agendamento.FormaPagamento.LOJA;
            case "ONLINE" -> Agendamento.FormaPagamento.ONLINE;
            case "CARTAO" -> Agendamento.FormaPagamento.CARTAO;
            case "DINHEIRO" -> Agendamento.FormaPagamento.DINHEIRO;
            default -> Agendamento.FormaPagamento.LOJA;
        };
    }
}