/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.restful.models;

/**
 *
 * @author Anyelo
 */
public enum CatalogoErrorExterno {

    E000("E000", "Ejecución satisfactoria", "Ejecución satisfactoria"),
    E001("E001", "Error de transacción", "No se ejecutó el método de consulta"),
    E002("E002", "Error al ejecutar el servicio", "No se pudo retornar datos"),
    E003("E003", "No se encontraron datos de la ficha", "Número de ficha incorrecto"),
    E004("E004", "El link de la solicitud ha caducado", "El link de la solicitud ha caducado"),
    E005("E005", "El documento %s no posee firma electronica", "El documento no posee firma electronica"),
    E006("E006", "Faltan firmas electronicas en el documento %s", "Faltan firmas electronicas en el documento"),
    E007("E007", "Datos actualizadas correctamente", "Datos actualizadas correctamente"),
    E008("E008", "Faltan datos para generar la orden de pago", "Faltan datos para generar la orden de pago"),
    E009("E009", "No se puede aplicar el pago. Pague primero las deudas pendientes o vencidas",
            "Primero pague las deudas pendientes o vencidas más antiguas"),
    E010("E010", "Datos de la solicitud guardados con exito", "Datos de la solicitud guardados con exito"),
    E011("E011", "Datos de la solicitud actualizados con exito", "Datos de la solicitud actualizados con exito"),
    E012("E012", "Aun existen un trámite pendiente para el predio solicitado",
            "Aun existen un trámite pendiente para el predio solicitado"),
    E013("E013", "No se encontraron resultados", "No se encontraron resultados"),
    E014("E014", "El número de identificación que proporcionaste está registrado en la base de datos del Registro de la Propiedad, "
            + "no puedes solicitar un certificado de no poseer bienes.", "SI se encontraron resultados"),
    E015("E000", "El número de identificación que proporcionaste no está registrado en la base de datos del Registro de la Propiedad, "
            + "si puedes solicitar un certificado de no poseer bienes.", "NO se encontraron resultados."),
    E016("E016", "Análisis", "Trámite en Proceso de Análisis de Datos"),
    E017("E017", "Devolutiva", "Trámite con Nota Devolutiva"),
    E018("E018", "Revisión", "Trámite en Proceso de Revisión de Documento"),
    E019("E019", "Firma", "Trámite en Proceso de Firma de Documento"),
    E020("E020", "Finalizado", "Trámite Finalizado"),
    E021("E021", "No se puede Iniciar el Proceso", "El proceso del trámite ya fue iniciado anteriormente."),
    E022("E022", "Nro de referencia repetido", "No se puede registrar el trámite porque el nro de referencia ya fue ingresado."),
    E023("E023", "Título", "Trámite en Proceso de Revisión de Títulos"),
    E024("E024", "Legal", "Trámite en Proceso de Revisión Legal"),
    E025("E025", "Devolutiva", "Trámite con Nota Devolutiva"),
    E026("E026", "Revisión", "Trámite en Proceso de Revisión de Datos Ingresados"),
    E027("E027", "Generación", "Trámite en Proceso de Generación de Documento"),
    E028("E028", "Foliación", "Trámite en Proceso de Foliación de Documento");

    //Existe una solicitud previa que aún no ha sido finalizada.
    private final String codigo;
    private final String descripcion;
    private final String sugerencia;

    CatalogoErrorExterno(String codigo, String descripcion, String sugerencia) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.sugerencia = sugerencia;
    }

    public String codigo() {
        return codigo;
    }

    public String descripcion() {
        return descripcion;
    }

    public String sugerencia() {
        return sugerencia;
    }

    public static CatalogoErrorExterno of(String codigo) {
        for (CatalogoErrorExterno mode : CatalogoErrorExterno.values()) {
            if (mode.codigo().equals(codigo)) {
                return mode;
            }
        }
        return null;
    }

}
