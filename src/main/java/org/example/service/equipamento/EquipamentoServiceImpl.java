package org.example.service.equipamento;

import org.example.model.Equipamento;
import org.example.repository.EquipamentoRepository;

import java.sql.SQLException;

public class EquipamentoServiceImpl implements EquipamentoService{

    private final EquipamentoRepository equipamentoRepository;

    public EquipamentoServiceImpl(EquipamentoRepository equipamentoRepository) {
        this.equipamentoRepository = equipamentoRepository;
    }

    @Override
    public Equipamento criarEquipamento(Equipamento equipamento) throws SQLException {
        equipamento.setStatusOperacional("OPERACIONAL");

        equipamentoRepository.save(equipamento);

        return equipamento;
    }

    @Override
    public Equipamento buscarEquipamentoPorId(Long id) throws SQLException {
        Equipamento equipamento = equipamentoRepository.findById(id)
                .orElseThrow( () -> new RuntimeException("Equipamento não encontrado!") );

        return equipamento;
    }

}
