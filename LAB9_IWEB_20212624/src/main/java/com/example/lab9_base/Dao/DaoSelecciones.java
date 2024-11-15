package com.example.lab9_base.Dao;

import com.example.lab9_base.Bean.Estadio;
import com.example.lab9_base.Bean.Seleccion;

import java.sql.*;
import java.util.ArrayList;

public class DaoSelecciones extends DaoBase {
    public ArrayList<Seleccion> listarSelecciones() {

        ArrayList<Seleccion> selecciones = new ArrayList<>();
        /*
        Inserte su código aquí
        */
        String sql = "select * from seleccion";

        try(Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            DaoEstadio daoEstadio = new DaoEstadio();
            while (rs.next()) {
                Seleccion seleccion = new Seleccion();
                seleccion.setIdSeleccion(rs.getInt("idSeleccion"));
                seleccion.setNombre(rs.getString("nombre"));
                seleccion.setTecnico(rs.getString("tecnico"));


                Estadio estadio = daoEstadio.getEstadio(rs.getInt("estadio_idEstadio"));
                seleccion.setEstadio(estadio);
                selecciones.add(seleccion);

            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return selecciones;
    }
    public Seleccion obtenerSeleccion(int idSeleccion) {
        Seleccion seleccion = new Seleccion();

        String sql = "select * from seleccion where idSeleccion=?";

        try(Connection conn =getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idSeleccion);
            ResultSet rs = stmt.executeQuery();
            DaoEstadio daoEstadio = new DaoEstadio();
            while (rs.next()) {
                seleccion.setIdSeleccion(rs.getInt("idSeleccion"));
                seleccion.setNombre(rs.getString("nombre"));
                seleccion.setTecnico(rs.getString("tecnico"));


                Estadio estadio = daoEstadio.getEstadio(rs.getInt("estadio_idEstadio"));
                seleccion.setEstadio(estadio);

            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return seleccion;
    }

}
