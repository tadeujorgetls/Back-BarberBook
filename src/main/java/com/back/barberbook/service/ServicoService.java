package com.back.barberbook.service;

import com.back.barberbook.entity.Servico;
import com.back.barberbook.repository.ServicoRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ServicoService {

    private final ServicoRepository repo;

    public ServicoService(ServicoRepository repo) {
        this.repo = repo;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Servico> listarServicos() {
        return repo.findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }
}