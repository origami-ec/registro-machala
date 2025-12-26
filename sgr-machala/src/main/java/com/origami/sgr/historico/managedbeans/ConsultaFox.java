/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.historico.managedbeans;

import com.origami.config.SisVars;
import com.origami.sgr.historico.model.Partida;
import com.origami.sgr.util.JsfUti;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class ConsultaFox implements Serializable {

    private Connection co;
    private Integer flag1 = 0;
    private Integer flag2 = 0;
    private String nombres;
    private Partida partida;
    private List<Partida> consulta;

    @PostConstruct
    public void init() {
        try {

        } catch (Exception e) {
            Logger.getLogger(ConsultaFox.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @PreDestroy
    public void preDestroy() {
        if (co != null) {
            try {
                co.close();
            } catch (SQLException ex) {
                Logger.getLogger(ConsultaFox.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void consultarNombres() {
        String query;
        String tabla = "";
        String columna = "";
        try {
            if (flag1 == 0) {
                JsfUti.messageWarning(null, "Seleccione el libro de consulta.", "");
                return;
            }
            if (flag2 == 0) {
                JsfUti.messageWarning(null, "Seleccione el tipo de consulta.", "");
                return;
            }
            if (nombres == null || nombres.isEmpty()) {
                JsfUti.messageWarning(null, "Debe ingresar los nombres.", "");
                return;
            }
            switch (flag1) {
                case 0:
                    JsfUti.messageWarning(null, "Debe seleccionar el tipo de busqueda.", "");
                    break;
                case 1:
                    tabla = "historico.demandas";
                    if (flag2 == 1) {
                        columna = "deudor";
                    } else {
                        columna = "acreedor";
                    }
                    query = "select * from " + tabla + " where " + columna + " ilike '%" + nombres + "%' order by anio, partida";
                    this.buscarHistorico(query);
                    break;
                case 2:
                    tabla = "historico.mercantil";
                    if (flag2 == 1) {
                        columna = "deudor";
                    } else {
                        columna = "acreedor";
                    }
                    query = "select * from " + tabla + " where " + columna + " ilike '%" + nombres + "%' order by anio, partida";
                    this.buscarHistorico(query);
                    break;
                case 3:
                    tabla = "historico.minero";
                    if (flag2 == 1) {
                        columna = "deudor";
                    } else {
                        columna = "acreedor";
                    }
                    query = "select * from " + tabla + " where " + columna + " ilike '%" + nombres + "%' order by anio, partida";
                    this.buscarHistorico(query);
                    break;
                case 4:
                    tabla = "historico.prendas";
                    if (flag2 == 1) {
                        columna = "deudor";
                    } else {
                        columna = "acreedor";
                    }
                    query = "select * from " + tabla + " where " + columna + " ilike '%" + nombres + "%' order by anio, partida";
                    this.buscarHistorico(query);
                    break;
                case 5:
                    tabla = "historico.prendas_bnf";
                    if (flag2 == 1) {
                        columna = "deudor";
                    } else {
                        columna = "acreedor";
                    }
                    query = "select * from " + tabla + " where " + columna + " ilike '%" + nombres + "%' order by anio, partida";
                    this.buscarHistorico(query);
                    break;
                case 6:
                    tabla = "historico.pro_1940";
                    if (flag2 == 1) {
                        columna = "deudor";
                    } else {
                        columna = "acreedor";
                    }
                    query = "select * from " + tabla + " where " + columna + " ilike '%" + nombres + "%' order by anio, partida";
                    this.buscarHistorico(query);
                    break;
                case 7:
                    tabla = "historico.pro_1970";
                    if (flag2 == 1) {
                        columna = "deudor";
                    } else {
                        columna = "acreedor";
                    }
                    query = "select * from " + tabla + " where " + columna + " ilike '%" + nombres + "%' order by anio, partida";
                    this.buscarHistorico(query);
                    break;
                case 8:
                    tabla = "historico.pro_1993";
                    if (flag2 == 1) {
                        columna = "deudor";
                    } else {
                        columna = "acreedor";
                    }
                    query = "select * from " + tabla + " where " + columna + " ilike '%" + nombres + "%' order by anio, partida";
                    this.buscarHistorico(query);
                    break;
                case 9:
                    tabla = "historico.pro_1998";
                    if (flag2 == 1) {
                        columna = "deudor";
                    } else {
                        columna = "acreedor";
                    }
                    query = "select * from " + tabla + " where " + columna + " ilike '%" + nombres + "%' order by anio, partida";
                    this.buscarHistorico(query);
                    break;
                case 10:
                    tabla = "historico.pro_2002";
                    if (flag2 == 1) {
                        columna = "deudor";
                    } else {
                        columna = "acreedor";
                    }
                    query = "select * from " + tabla + " where " + columna + " ilike '%" + nombres + "%' order by anio, partida";
                    this.buscarHistorico(query);
                    break;
                case 11:
                    tabla = "historico.pro_2006";
                    if (flag2 == 1) {
                        columna = "deudor";
                    } else {
                        columna = "acreedor";
                    }
                    query = "select * from " + tabla + " where " + columna + " ilike '%" + nombres + "%' order by anio, partida";
                    this.buscarHistorico(query);
                    break;
                case 12:
                    tabla = "historico.prohibicion";
                    if (flag2 == 1) {
                        columna = "actor";
                    } else {
                        columna = "demandado";
                    }
                    query = "select * from " + tabla + " where " + columna + " ilike '%" + nombres + "%' ";
                    this.buscarHistorico2(query);
                    break;
                case 13:
                    tabla = "historico.sentencias";
                    if (flag2 == 1) {
                        columna = "deudor";
                    } else {
                        columna = "acreedor";
                    }
                    query = "select * from " + tabla + " where " + columna + " ilike '%" + nombres + "%' order by anio, partida";
                    this.buscarHistorico(query);
                    break;
                case 14:
                    tabla = "historico.propiedades";
                    if (flag2 == 1) {
                        columna = "comprador";
                    } else {
                        columna = "vendedor";
                    }
                    query = "select * from " + tabla + " where " + columna + " ilike '%" + nombres + "%' order by anio, partida";
                    this.buscarHistorico(query);
                    break;
            }
        } catch (SQLException e) {
            Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void buscarHistorico(String query) throws SQLException {
        co = this.getConnection();
        try {
            consulta = new ArrayList<>();
            PreparedStatement ps = co.prepareStatement(query);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    partida = new Partida();
                    partida.setDeudor(rs.getString(1));
                    partida.setAcreedor(rs.getString(2));
                    partida.setAnio(rs.getInt(3));
                    partida.setPartida(rs.getInt(4));
                    partida.setObservacion(rs.getString(5));
                    consulta.add(partida);
                }
            }
            if (consulta.isEmpty()) {
                JsfUti.messageWarning(null, "No se encontraron coincidencias.", "");
            }
        } catch (SQLException e) {
            co.close();
            System.out.println(e);
        }
        try {
            co.close();
        } catch (SQLException ex1) {
            Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }

    public void buscarHistorico2(String query) throws SQLException {
        co = this.getConnection();
        try {
            consulta = new ArrayList<>();
            PreparedStatement ps = co.prepareStatement(query);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    partida = new Partida();
                    partida.setDeudor(rs.getString(1));
                    partida.setAcreedor(rs.getString(2));
                    partida.setFecha(rs.getString(3));
                    partida.setObservacion(rs.getString(4));
                    consulta.add(partida);
                }
            }
            if (consulta.isEmpty()) {
                JsfUti.messageWarning(null, "No se encontraron coincidencias.", "");
            }
        } catch (SQLException e) {
            co.close();
            System.out.println(e);
        }
        try {
            co.close();
        } catch (SQLException ex1) {
            Logger.getLogger(ConsultaHistorico.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }

    public Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConsultaFox.class.getName()).log(Level.SEVERE, null, ex);
        }
        Properties props = new Properties();
        props.setProperty("user", SisVars.userName);
        props.setProperty("password", SisVars.password);
        co = null;
        try {
            co = DriverManager.getConnection(SisVars.url, props);
        } catch (SQLException ex) {
            Logger.getLogger(ConsultaFox.class.getName()).log(Level.SEVERE, null, ex);
        }
        return co;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public List<Partida> getConsulta() {
        return consulta;
    }

    public void setConsulta(List<Partida> consulta) {
        this.consulta = consulta;
    }

    public Integer getFlag1() {
        return flag1;
    }

    public void setFlag1(Integer flag1) {
        this.flag1 = flag1;
    }

    public Integer getFlag2() {
        return flag2;
    }

    public void setFlag2(Integer flag2) {
        this.flag2 = flag2;
    }

}
