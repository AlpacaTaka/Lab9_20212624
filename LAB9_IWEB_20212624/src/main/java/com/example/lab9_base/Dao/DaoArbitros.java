package com.example.lab9_base.Dao;

import com.example.lab9_base.Bean.Arbitro;

import java.sql.*;
import java.util.ArrayList;

public class DaoArbitros extends DaoBase {
    public ArrayList<Arbitro> listarArbitros() {
        ArrayList<Arbitro> arbitros = new ArrayList<>();

        String sql = "select * from arbitro";

        try(Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {



            while (rs.next()) {
                Arbitro arbitro = new Arbitro();
                arbitro.setIdArbitro(rs.getInt("idArbitro"));
                arbitro.setNombre(rs.getString("nombre"));
                arbitro.setPais(rs.getString("pais"));
                arbitros.add(arbitro);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return arbitros;
    }

    public void crearArbitro(Arbitro arbitro) {

        String sql = "insert into arbitro (nombre, pais) values (?,?)";

        try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, arbitro.getNombre());
            stmt.setString(2, arbitro.getPais());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Arbitro> busquedaPais(String pais) {

        ArrayList<Arbitro> arbitros = new ArrayList<>();
        /*
        Inserte su código aquí
        */
        String sql = "select * from arbitro where pais like ?";
        try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + pais + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Arbitro arbitro = new Arbitro();
                arbitro.setIdArbitro(rs.getInt("idArbitro"));
                arbitro.setNombre(rs.getString("nombre"));
                arbitro.setPais(rs.getString("pais"));
                arbitros.add(arbitro);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return arbitros;
    }

    public ArrayList<Arbitro> busquedaNombre(String nombre)  {

        ArrayList<Arbitro> arbitros = new ArrayList<>();
        /*
        Inserte su código aquí
        */

        String sql = "select * from arbitro where nombre like ?";

        try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nombre + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Arbitro arbitro = new Arbitro();
                arbitro.setIdArbitro(rs.getInt("idArbitro"));
                arbitro.setNombre(rs.getString("nombre"));
                arbitro.setPais(rs.getString("pais"));
                arbitros.add(arbitro);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return arbitros;
    }

    public Arbitro buscarArbitro(int id) {
        Arbitro arbitro = null; // Inicialmente null
        String sql = "SELECT * FROM arbitro WHERE idArbitro = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) { // Si encuentra un resultado
                arbitro = new Arbitro();
                arbitro.setIdArbitro(rs.getInt("idArbitro"));
                arbitro.setNombre(rs.getString("nombre"));
                arbitro.setPais(rs.getString("pais"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return arbitro; // Devuelve null si no se encontró el árbitro
    }

    public void borrarArbitro(int id) {
        /*
        Inserte su código aquí
        */
        String sql1 = "delete from partido where arbitro = ?";
        String sql2 = "delete from arbitro where idArbitro = ?";

        try (Connection conn = getConnection()){
            PreparedStatement stmtPartidos = conn.prepareStatement(sql1);
            PreparedStatement stmtArbitros = conn.prepareStatement(sql2);

            stmtPartidos.setInt(1, id);
            stmtPartidos.executeUpdate();
            stmtArbitros.setInt(1, id);
            stmtArbitros.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existeArbitroNombre(String nombre) {
        boolean existe = false;

        String sql = "select COUNT(*) from arbitro where nombre = ?";

        try(Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);

            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                existe = true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return existe;
    }
}
