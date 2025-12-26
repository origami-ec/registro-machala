package org.origami.docs.model;

import java.util.List;

public class PDFNotas {
    private List<PDFNotasDetalle> notas;

    public PDFNotas(List<PDFNotasDetalle> notas) {
        this.notas = notas;
    }

    public PDFNotas() {
    }

    public List<PDFNotasDetalle> getNotas() {
        return notas;
    }

    public void setNotas(List<PDFNotasDetalle> notas) {
        this.notas = notas;
    }
}
