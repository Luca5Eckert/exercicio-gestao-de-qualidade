package org.example.service.falha;

import org.example.model.Equipamento;
import org.example.model.Falha;
import org.example.repository.EquipamentoRepository;
import org.example.repository.FalhaRepository;

import java.sql.SQLException;
import java.util.List;

public class FalhaServiceImpl implements FalhaService{

    private final EquipamentoRepository equipamentoRepository;
    private final FalhaRepository falhaRepository;

    public FalhaServiceImpl(EquipamentoRepository equipamentoRepository, FalhaRepository falhaRepository) {
        this.equipamentoRepository = equipamentoRepository;
        this.falhaRepository = falhaRepository;
    }


    @Override
    public Falha registrarNovaFalha(Falha falha) throws SQLException {
        equipamentoRepository.findById(falha.getEquipamentoId())
                .orElseThrow( () -> new IllegalArgumentException("Equipamento não encontrado!") );

        String status = falha.getCriticidade().equals("CRITICA") ? "EM_MANUTENCAO" : "ABERTA";

        falha.setStatus(status);

        falhaRepository.save(falha);

        return falha;
    }

    @Override
    public List<Falha> buscarFalhasCriticasAbertas() throws SQLException {
        return falhaRepository.findCriticasAbertas();
    }
}
