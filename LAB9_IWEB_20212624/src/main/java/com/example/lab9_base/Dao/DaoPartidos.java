package com.example.lab9_base.Dao;

import com.example.lab9_base.Bean.Arbitro;
import com.example.lab9_base.Bean.Partido;
import com.example.lab9_base.Bean.Seleccion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DaoPartidos extends DaoBase {
    public ArrayList<Partido> listaDePartidos() {

        ArrayList<Partido> partidos = new ArrayList<>();
        /*
        Inserte su código aquí
        */
        String sql = "SELECT * FROM partido";

        try(Connection conn=getConnection();
        PreparedStatement pst=conn.prepareStatement(sql);
        ResultSet rs=pst.executeQuery()) {
            DaoArbitros daoArbitros = new DaoArbitros();
            DaoSelecciones daoSelecciones = new DaoSelecciones();

            while (rs.next()) {
                Partido partido = new Partido();
                partido.setIdPartido(rs.getInt("idPartido"));
                partido.setFecha(rs.getString("fecha"));
                partido.setNumeroJornada(rs.getInt("numeroJornada"));




                Seleccion seleccionLocal = daoSelecciones.obtenerSeleccion(rs.getInt("seleccionLocal"));
                partido.setSeleccionLocal(seleccionLocal);
                System.out.println(partido.getSeleccionLocal().getNombre());

                Seleccion seleccionVisitante = daoSelecciones.obtenerSeleccion(rs.getInt("seleccionVisitante"));
                partido.setSeleccionVisitante(seleccionVisitante);
                System.out.println(partido.getSeleccionVisitante().getNombre());

                Arbitro arbitro = daoArbitros.buscarArbitro(rs.getInt("arbitro"));
                partido.setArbitro(arbitro);


                partidos.add(partido);

            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return partidos;
    }

    public Partido obtenerPartido(int id){
        Partido partido = new Partido();

        String sql = "SELECT * FROM partido WHERE idPartido = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return partido;
    }

    public void crearPartido(Partido partido) {

        /*
        Inserte su código aquí
        */

        String sql = "INSERT INTO partido (seleccionLocal, seleccionVisitante, arbitro,fecha,numeroJornada) VALUES (?,?,?,?,?)";

        try(Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, partido.getSeleccionLocal().getIdSeleccion());
            pstmt.setInt(2, partido.getSeleccionVisitante().getIdSeleccion());
            pstmt.setDouble(3, partido.getArbitro().getIdArbitro());
            pstmt.setString(4, partido.getFecha());
            pstmt.setInt(5, partido.getNumeroJornada());
            pstmt.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public boolean existePartido(int idLocal, int idVisitante, int jornada) {
        String sql = "SELECT COUNT(*) FROM partido WHERE numeroJornada = ? " +
                "AND ((seleccionLocal = ? AND seleccionVisitante = ?) " +
                "     OR (seleccionLocal = ? AND seleccionVisitante = ?))";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, jornada);
            stmt.setInt(2, idLocal);
            stmt.setInt(3, idVisitante);
            stmt.setInt(4, idVisitante);
            stmt.setInt(5, idLocal);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al verificar la existencia del partido", e);
        }
        return false;
    }
}
