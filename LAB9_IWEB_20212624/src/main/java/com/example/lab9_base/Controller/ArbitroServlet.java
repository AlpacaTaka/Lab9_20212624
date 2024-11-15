package com.example.lab9_base.Controller;

import com.example.lab9_base.Bean.Arbitro;
import com.example.lab9_base.Dao.DaoArbitros;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "ArbitroServlet", urlPatterns = {"/ArbitroServlet"})
public class ArbitroServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        RequestDispatcher view;
        ArrayList<String> opciones = new ArrayList<>();
        opciones.add("nombre");
        opciones.add("pais");
        DaoArbitros daoArbitros = new DaoArbitros();

        switch (action) {

            case "buscar":

                String tipo = request.getParameter("tipo");
                String buscar = request.getParameter("buscar");

                ArrayList<Arbitro> resultArbitro = new ArrayList<>();

                if(tipo!=null && buscar!=null && !buscar.trim().isEmpty()) {
                    if(tipo.equals("nombre")) {
                        resultArbitro = daoArbitros.busquedaNombre(buscar);
                    }
                    else if(tipo.equals("pais")) {
                        resultArbitro = daoArbitros.busquedaPais(buscar);
                    }
                    request.setAttribute("listaArbitros", resultArbitro);
                    view = request.getRequestDispatcher("/arbitros/list.jsp");
                    view.forward(request, response);

                }
                else{
                    response.sendRedirect(request.getContextPath() + "/ArbitroServlet");

                }




                break;

            case "guardar":

                String nombre = request.getParameter("nombre");
                String pais = request.getParameter("pais");

                if ((nombre == null || nombre.trim().isEmpty()) && (pais == null || pais.trim().isEmpty())) {
                    response.sendRedirect("/ArbitroServlet?action=crear");
                    return;
                } else if (daoArbitros.existeArbitroNombre(nombre)) {
                    response.sendRedirect(request.getContextPath() + "/ArbitroServlet?action=crear");
                }
                else{

                    Arbitro nuevoArbitro = new Arbitro();
                    nuevoArbitro.setNombre(nombre);
                    nuevoArbitro.setPais(pais);
                    daoArbitros.crearArbitro(nuevoArbitro);
                    response.sendRedirect(request.getContextPath() + "/ArbitroServlet");
                }



                break;

        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        RequestDispatcher view;
        ArrayList<String> paises = new ArrayList<>();
        paises.add("Peru");
        paises.add("Chile");
        paises.add("Argentina");
        paises.add("Paraguay");
        paises.add("Uruguay");
        paises.add("Colombia");
        ArrayList<String> opciones = new ArrayList<>();
        opciones.add("nombre");
        opciones.add("pais");

        DaoArbitros arbitroDao = new DaoArbitros();

        switch (action) {
            case "lista":


                ArrayList<Arbitro> listaArbitros = arbitroDao.listarArbitros();
                request.setAttribute("listaArbitros", listaArbitros);



                view = request.getRequestDispatcher("/arbitros/list.jsp");
                view.forward(request, response);
                break;
            case "crear":

                request.setAttribute("paises", paises);
                view = request.getRequestDispatcher("/arbitros/form.jsp");
                view.forward(request, response);

                break;
            case "borrar":

                Integer id =Integer.parseInt(request.getParameter("id"));

                arbitroDao.borrarArbitro(id);
                response.sendRedirect(request.getContextPath() + "/ArbitroServlet");

                break;
        }
    }
}
