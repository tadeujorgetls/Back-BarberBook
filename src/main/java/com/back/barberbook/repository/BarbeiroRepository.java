package com.back.barberbook.repository;

import com.back.barberbook.entity.Barbeiro;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BarbeiroRepository extends JpaRepository<Barbeiro, UUID> {

    @Query("""
        select b.id as id, b.nome as nome, b.telefone as telefone
        from Barbeiro b
        order by b.nome asc
    """)
    List<BarbeiroListView> findAllPublicView();
}