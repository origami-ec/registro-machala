/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans.indexacion;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import com.origami.documental.services.ArchivosService;

/**
 *
 * @author ANGEL NAVARRO
 */
@Named
@ViewScoped
public class Test implements Serializable {

    private String text;
    @Inject
    private ArchivosService arch;

    @PostConstruct
    public void initView() {
        try {
            text = arch.rtfToHtml("{\\rtf1\\ansi\\ansicpg1252\\deff0\\deflang3082{\\fonttbl{\\f0\\froman\\fprq2\\fcharset0 Times New Roman;}{\\f1\\fnil\\fcharset0 Times New Roman;}}"
                    + "\\viewkind4\\uc1\\pard\\nowidctlpar\\hyphpar0\\ri-306\\sl360\\slmult0\\qj\\tx0\\tx426\\tx1440\\tx1872\\tx2160\\lang1034\\expndtw-3\\f0\\fs24 Que, por escritura p\\'fablica celebrada en esta ciudad, el 21 de marzo de 1980, ante el Notario Primero del cant\\'f3n Loja, doctor Eduardo Beltr\\'e1n Beltr\\'e1n, los esposos Carlos Coronel Pinta y Luz Mar\\'eda Benitez de Coronel, venden a favor de los esposos \\b JUAN GORDILLO SAMANIEGO Y MARIA HORTENSIA PINTA DE GORDILLO\\b0 , el predio denominado El Salado, perteneciente a la parroquia Sucre del cant\\'f3n y provincia de Loja, por el precio de VEINTE MIL SUCRES 00/100 (s/20.000,00) de contado.-Inscripci\\'f3n No. 562 del Registro de Propiedad del 15-IV-80."
                    + "\\par La propiedad en referencia, a la fecha, no se encuentra embargada, hipotecada, ni en poder de tercer tenedor o poseedor con t\\'edtulo inscrito."
                    + "\\par \\pard\\nowidctlpar\\hyphpar0\\ri-306\\sl360\\slmult0\\qj\\tx426\\tqc\\tx5246\\tab                                            Loja, 19 de junio de 1996."
                    + "\\par \\pard\\nowidctlpar\\hyphpar0\\ri-306\\sl360\\slmult0\\qj\\tx0\\tx426\\tx1440\\tx1872\\tx2160 -llch-"
                    + "\\par -je4367-\\lang3082\\expndtw0\\f1 "
                    + "\\par }");
        } catch (IOException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
