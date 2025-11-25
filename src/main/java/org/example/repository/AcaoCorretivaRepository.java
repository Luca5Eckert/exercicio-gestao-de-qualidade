package org.example.repository;

import org.example.database.Conexao;
import org.example.model.AcaoCorretiva;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AcaoCorretivaRepository {

    public void save(AcaoCorretiva acaoCorretiva) {

        String query = """
                INSERT INTO
                    AcaoCorretiva
                (
                    falhaId
                    ,dataHoraInicio
                    ,dataHoraFim
                    ,responsavel
                    ,descricaoAcao
                )
                VALUES
                (
                    ?,
                    ?,
                    ?,
                    ?,
                    ?
                )
                """;

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){

            stmt.setLong(1,acaoCorretiva.getFalhaId());
            stmt.setTimestamp(2, Timestamp.valueOf(acaoCorretiva.getDataHoraInicio()));
            stmt.setTimestamp(3, Timestamp.valueOf(acaoCorretiva.getDataHoraFim()));
            stmt.setString(4, acaoCorretiva.getResponsavel());
            stmt.setString(5, acaoCorretiva.getDescricaoArea());

            stmt.executeUpdate();

            try(ResultSet rs = stmt.getGeneratedKeys()){
                if(rs.next()){
                    acaoCorretiva.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public List<String> findAllByFalhaId(long falhaId) throws SQLException {
        List<String> acoes = new ArrayList<>();

        String query = """
                SELECT
                    descricaoAcao
                WHERE
                    falhaId = ?
                """;

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){

            stmt.setLong(1, falhaId);

            ResultSet rs = stmt.getGeneratedKeys();

            while(rs.next()){
                String descricaoAcao = rs.getString("descricaoAcao");
                acoes.add(descricaoAcao);
            }

        }

        return acoes;
    }
}