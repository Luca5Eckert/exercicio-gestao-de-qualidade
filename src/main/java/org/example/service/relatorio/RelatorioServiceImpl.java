package org.example.service.relatorio;

import org.example.dto.EquipamentoContagemFalhasDTO;
import org.example.dto.FalhaDetalhadaDTO;
import org.example.dto.RelatorioParadaDTO;
import org.example.model.Equipamento;
import org.example.model.Falha;
import org.example.repository.AcaoCorretivaRepository;
import org.example.repository.EquipamentoRepository;
import org.example.repository.FalhaRepository;
import org.example.repository.RelatorioRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class RelatorioServiceImpl implements RelatorioService{

    private final RelatorioRepository relatorioRepository;

    private final FalhaRepository falhaRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final AcaoCorretivaRepository acaoCorretivaRepository;

    public RelatorioServiceImpl(RelatorioRepository relatorioRepository, FalhaRepository falhaRepository, EquipamentoRepository equipamentoRepository, AcaoCorretivaRepository acaoCorretivaRepository) {
        this.relatorioRepository = relatorioRepository;
        this.falhaRepository = falhaRepository;
        this.equipamentoRepository = equipamentoRepository;
        this.acaoCorretivaRepository = acaoCorretivaRepository;
    }


    @Override
    public List<RelatorioParadaDTO> gerarRelatorioTempoParada() throws SQLException {
        return relatorioRepository.gerarRelatorioTempoParada();
    }

    @Override
    public List<Equipamento> buscarEquipamentosSemFalhasPorPeriodo(LocalDate dataInicio, LocalDate datafim) throws SQLException {
        return equipamentoRepository.buscarEquipamentosSemFalhasPorPeriodo(dataInicio, datafim);
    }

    @Override
    public Optional<FalhaDetalhadaDTO> buscarDetalhesCompletosFalha(long falhaId) throws SQLException {
        Falha falha = falhaRepository.findById(falhaId)
                .orElseThrow( () -> new RuntimeException("Falha não encontrada!"));

        Equipamento equipamento = equipamentoRepository.findById(falha.getEquipamentoId())
                .orElseThrow( () -> new RuntimeException("Equipamento não encontrado"));

        List<String> acoesCorretivas = acaoCorretivaRepository.findAllByFalhaId(falhaId);

        FalhaDetalhadaDTO falhaDetalhadaDTO = new FalhaDetalhadaDTO(
                falha,
                equipamento,
                acoesCorretivas
        );

        return Optional.of(falhaDetalhadaDTO);

    }

    @Override
    public List<EquipamentoContagemFalhasDTO> gerarRelatorioManutencaoPreventiva(int contagemMinimaFalhas) throws SQLException {
        if(contagemMinimaFalhas <= 0) throw new RuntimeException("O número de contagem deve ser positivo");

        return relatorioRepository.gerarRelatorioManutencaoPreventiva(contagemMinimaFalhas);
    }
}
