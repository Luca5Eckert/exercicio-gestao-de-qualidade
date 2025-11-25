package org.example.service.acaocorretiva;

import org.example.model.AcaoCorretiva;
import org.example.model.Falha;
import org.example.repository.AcaoCorretivaRepository;
import org.example.repository.EquipamentoRepository;
import org.example.repository.FalhaRepository;

import java.sql.SQLException;

public class AcaoCorretivaServiceImpl implements AcaoCorretivaService{

    private final AcaoCorretivaRepository acaoCorretivaRepository;
    private final FalhaRepository falhaRepository;
    private final EquipamentoRepository equipamentoRepository;

    public AcaoCorretivaServiceImpl(AcaoCorretivaRepository acaoCorretivaRepository, FalhaRepository falhaRepository, EquipamentoRepository equipamentoRepository) {
        this.acaoCorretivaRepository = acaoCorretivaRepository;
        this.falhaRepository = falhaRepository;
        this.equipamentoRepository = equipamentoRepository;
    }


    @Override
    public AcaoCorretiva registrarConclusaoDeAcao(AcaoCorretiva acao) throws SQLException {
        Falha falha = falhaRepository.findById(acao.getFalhaId())
                .orElseThrow( () -> new RuntimeException("Falha não encontrada!"));

        if(falha.getCriticidade().equals("CRITICA")){
            equipamentoRepository.updateStatus(
                    falha.getEquipamentoId(),
                    "OPERACIONAL"
            );
        }

        falha.setStatus("RESOLVIDA");
        falhaRepository.updateStatus(
                falha.getId(),
                falha.getStatus()
        );

        acaoCorretivaRepository.save(acao);

        return acao;
    }
}
