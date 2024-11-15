package com.example.lab9_base.Dao;

import com.example.lab9_base.Bean.Estadio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoEstadio extends DaoBase{

    public Estadio getEstadio(int id){
        Estadio estadio = new Estadio();
        String sql = "select * from estadio where idEstadio = ?";

        try(Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                estadio.setIdEstadio(rs.getInt("idEstadio"));
                estadio.setNombre(rs.getString("nombre"));
                estadio.setProvincia(rs.getString("provincia"));
                estadio.setClub(rs.getString("club"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return estadio;
    }
}
