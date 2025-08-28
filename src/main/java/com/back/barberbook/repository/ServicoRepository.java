package com.back.barberbook.repository;

import com.back.barberbook.entity.Servico;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicoRepository extends JpaRepository<Servico, UUID> {
	
}