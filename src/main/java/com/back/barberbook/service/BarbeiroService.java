package com.back.barberbook.service;

import com.back.barberbook.repository.BarbeiroListView;
import com.back.barberbook.repository.BarbeiroRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BarbeiroService {

    private final BarbeiroRepository repo;

    public BarbeiroService(BarbeiroRepository repo) {
        this.repo = repo;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<BarbeiroListView> listarBarbeirosPublicos() {
        return repo.findAllPublicView();
    }
}