package org.example.repository;

import org.example.database.Conexao;
import org.example.model.Falha;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FalhaRepository {

    public void save(Falha falha)  throws SQLException {
        String query = """
                INSERT INTO
                    Falha
                (
                    equipamentoId,
                    dataHoraOcorrencia,
                    descricao,
                    criticidade,
                    status,
                    tempoParadaHoras
                )
                VALUES
                (
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?
                )
                """;

        try(
                Connection conn = Conexao.conectar();
                PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ){

            stmt.setLong(1, falha.getEquipamentoId());
            stmt.setTimestamp(2, Timestamp.valueOf(falha.getDataHoraOcorrencia()));
            stmt.setString(3, falha.getDescricao());
            stmt.setString(4, falha.getCriticidade());
            stmt.setString(5, falha.getStatus());
            stmt.setBigDecimal(6,falha.getTempoParadaHoras());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if(rs.next()){
                falha.setId(rs.getLong(1));
            }

        }

    }

    public List<Falha> findCriticasAbertas() throws SQLException {
        List<Falha> falhas = new ArrayList<>();

        String query = """
                SELECT
                    id
                    ,equipamentoId
                    ,dataHoraOcorrencia
                    ,descricao
                    ,criticidade
                    ,status
                    ,tempoParadaHoras
                FROM
                    Falha
                WHERE
                    criticidade = 'CRITICA'
                AND
                    status  = 'ABERTA'
                """;

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(query)){
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){

                Long id = rs.getLong("id");
                Long equipamentoId = rs.getLong("equipamentoId");
                LocalDateTime dataHoraOcorrencia = rs.getTimestamp("dataHoraOcorrencia").toLocalDateTime();
                String descricao = rs.getString("descricao");
                String criticidade = rs.getString("criticidade");
                String status = rs.getString("status");
                BigDecimal tempoParadaHoras = rs.getBigDecimal("tempoParadaHoras");

                Falha falha = new Falha(
                        id,
                        equipamentoId,
                        dataHoraOcorrencia,
                        descricao,
                        criticidade,
                        status,
                        tempoParadaHoras
                );

                falhas.add(falha);

            }

        }

        return falhas;

    }


}
