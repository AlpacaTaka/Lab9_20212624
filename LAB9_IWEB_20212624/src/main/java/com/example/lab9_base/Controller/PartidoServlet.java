package com.example.lab9_base.Controller;

import com.example.lab9_base.Bean.Arbitro;
import com.example.lab9_base.Bean.Partido;
import com.example.lab9_base.Bean.Seleccion;
import com.example.lab9_base.Dao.DaoArbitros;
import com.example.lab9_base.Dao.DaoPartidos;
import com.example.lab9_base.Dao.DaoSelecciones;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "PartidoServlet", urlPatterns = {"/PartidoServlet", ""})
public class PartidoServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "guardar" : request.getParameter("action");
        RequestDispatcher view;

        switch (action) {

            case "guardar":
                /*
                Inserte su código aquí
                */
                String fecha = request.getParameter("fecha");
                String jornada = request.getParameter("jornada");
                String local = request.getParameter("local");
                String visitante = request.getParameter("visitante");
                String arbitro = request.getParameter("arbitro");

                if (fecha == null || fecha.isEmpty() ||
                        jornada == null || jornada.isEmpty() ||
                        local == null || local.isEmpty() ||
                        visitante == null || visitante.isEmpty() ||
                        arbitro == null || arbitro.isEmpty()) {

                    response.sendRedirect(request.getContextPath() + "/PartidoServlet?action=crear");
                    return;
                }

                // Validar que las selecciones sean diferentes
                if (local.equals(visitante)) {
                    response.sendRedirect(request.getContextPath() + "/PartidoServlet?action=crear");
                    return;
                }

                // Validar que el partido no se repita en la misma jornada
                DaoPartidos daoPartidos = new DaoPartidos();
                int idLocal = Integer.parseInt(local);
                int idVisitante = Integer.parseInt(visitante);
                int numjornada = Integer.parseInt(jornada);
                int idArbitro = Integer.parseInt(arbitro);

                if (daoPartidos.existePartido(idLocal, idVisitante, numjornada)) {
                    response.sendRedirect(request.getContextPath() + "/PartidoServlet?action=crear");
                    return;
                }
                // Crear el partido si las validaciones son exitosas
                Partido partido = new Partido();
                partido.setFecha(fecha);

                DaoSelecciones daoSelecciones = new DaoSelecciones();
                Seleccion seleccionLocal = daoSelecciones.obtenerSeleccion(idLocal);
                partido.setSeleccionLocal(seleccionLocal);
                Seleccion seleccionVisitante = daoSelecciones.obtenerSeleccion(idVisitante);
                partido.setSeleccionVisitante(seleccionVisitante);

                DaoArbitros daoArbitros = new DaoArbitros();
                Arbitro arbitroPartido = daoArbitros.buscarArbitro(idArbitro);
                partido.setArbitro(arbitroPartido);

                partido.setNumeroJornada(numjornada);

                daoPartidos.crearPartido(partido);

                // Redirigir a la lista de partidos
                response.sendRedirect(request.getContextPath() + "/PartidoServlet");




                break;

        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        RequestDispatcher view;
        DaoPartidos daoPartidos = new DaoPartidos();
        switch (action) {
            case "lista":
                /*
                Inserte su código aquí
                 */
                ;
                ArrayList<Partido> listaPartidos = daoPartidos.listaDePartidos();

                System.out.println("La lista contiene " + listaPartidos.size() + " partidos.");
                request.setAttribute("listaPartidos", listaPartidos);

                view = request.getRequestDispatcher("index.jsp");
                view.forward(request, response);
                break;
            case "crear":
                /*
                Inserte su código aquí
                 */
                ArrayList<Seleccion> selecciones = new ArrayList<>();
                DaoSelecciones daoSelecciones = new DaoSelecciones();
                selecciones=daoSelecciones.listarSelecciones();
                request.setAttribute("selecciones", selecciones);

                ArrayList<Arbitro> arbitros = new ArrayList<>();
                DaoArbitros daoArbitros = new DaoArbitros();
                arbitros=daoArbitros.listarArbitros();
                request.setAttribute("arbitros", arbitros);




                view = request.getRequestDispatcher("/partidos/form.jsp");
                view.forward(request, response);

                break;

        }

    }
}
