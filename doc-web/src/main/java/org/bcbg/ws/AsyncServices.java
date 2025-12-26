/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.bcbg.ws;

import javax.ejb.Local;

/**
 *
 * @author ORIGAMI
 */
@Local
public interface AsyncServices {

    /**
     *
     * @param descripcion Ejemplo: Nombre del Menu en el que el usuario dio
     * @param accion Accion de Consultar, Editar, Eliminar
     * @param response Informacion de lo que devuelve el WS
     * @param request URL a consultar
     * @param usuario nombre de usuario
     * @param usuarioApp nombre de usuario
     * @param ip ip del equipo cliente
     * @param mac mac del equipo cliente
     * @param osClient mac del equipo cliente
     */
    public void guardarLog(String descripcion, String accion, String response, String request, String usuario, String usuarioApp, String ip, String mac, String osClient);
}
