package org.example.repository;

import org.example.database.Conexao;
import org.example.dto.EquipamentoContagemFalhasDTO;
import org.example.dto.FalhaDetalhadaDTO;
import org.example.dto.RelatorioParadaDTO;
import org.example.model.Equipamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RelatorioRepository {


    public List<RelatorioParadaDTO> gerarRelatorioTempoParada() throws SQLException {
        String query = """
                SELECT
                    e.id,
                    e.nome,
                    f.tempoParadaHoras
                FROM
                    Equipamento e
                JOIN
                    Falha f ON e.id = f.equipamentoId
                """;
        
        try(
                Connection connection = Conexao.conectar();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()
        ){
            List<RelatorioParadaDTO> paradaDTOList = new ArrayList<>();
            
            while(resultSet.next()){
                
                RelatorioParadaDTO relatorioParadaDTO = new RelatorioParadaDTO (
                        resultSet.getLong("id"),
                        resultSet.getString("nome"),
                        resultSet.getDouble("tempoParadaHoras")
                );
                
                paradaDTOList.add(relatorioParadaDTO);
                
            }
            
            return paradaDTOList;
            
        }
        
    }



    public List<EquipamentoContagemFalhasDTO> gerarRelatorioManutencaoPreventiva(int contagemMinimaFalhas) throws SQLException {
        String query = """
                SELECT
                    e.id,
                    e.nome,
                    count(f.id) contagemFalhas
                FROM
                    Equipamento e
                JOIN
                    Falha f ON e.id = f.equipamentoId
                """;

        try(
                Connection connection = Conexao.conectar();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()
        ){
            List<EquipamentoContagemFalhasDTO> equipamentoContagemFalhasDTOS = new ArrayList<>();

            while(resultSet.next()){
                int contagemFalhas = resultSet.getInt("contagemFalhas");

                if(contagemFalhas >= contagemMinimaFalhas){

                    EquipamentoContagemFalhasDTO contagemFalhasDTO = new EquipamentoContagemFalhasDTO (
                            resultSet.getLong("id"),
                            resultSet.getString("nome"),
                            contagemFalhas
                    );

                    equipamentoContagemFalhasDTOS.add(contagemFalhasDTO);

                }


            }

            return equipamentoContagemFalhasDTOS;

        }
    }
}
