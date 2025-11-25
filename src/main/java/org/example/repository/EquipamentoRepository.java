package org.example.repository;

import org.example.database.Conexao;
import org.example.model.Equipamento;

import java.sql.*;
import java.util.Optional;

public class EquipamentoRepository {

    public void save(Equipamento equipamento) throws SQLException {
        String query = """
                INSERT INTO
                    Equipamento
                (
                    nome,
                    numeroDeSerie,
                    areaSetor,
                    statusOperacional
                )
                VALUES
                (
                    ?,
                    ?,
                    ?,
                    ?
                )
                """;

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){

            stmt.setString(1,equipamento.getNome());
            stmt.setString(2,equipamento.getNumeroDeSerie());
            stmt.setString(3,equipamento.getAreaSetor());
            stmt.setString(4,equipamento.getStatusOperacional());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if(rs.next()){
                equipamento.setId(rs.getLong(1));
            }

        }

    }

    public Optional<Equipamento> findById(Long id) throws SQLException {
        String query = """
                SELECT
                        id
                       , nome
                       , numeroDeSerie
                       , areaSetor
                       , statusOperacional
                FROM
                    Equipamento
                WHERE
                    id = ?
                """;

        try(Connection conn = Conexao.conectar();

            PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setLong(1,id);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                Equipamento equipamento =  new Equipamento(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("numeroDeSerie"),
                        rs.getString("areaSetor"),
                        rs.getString("statusOperacional")
                );
                return Optional.of(equipamento);
            }

            return Optional.empty();

        }

    }


}
