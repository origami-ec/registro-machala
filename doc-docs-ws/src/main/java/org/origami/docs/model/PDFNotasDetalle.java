package org.origami.docs.model;

import java.util.List;

public class PDFNotasDetalle {

    private String pagina;
    private List<NotaDto> detalle;

    public PDFNotasDetalle(String pagina, List<NotaDto> detalle) {
        this.pagina = pagina;
        this.detalle = detalle;
    }

    public String getPagina() {
        return pagina;
    }

    public void setPagina(String pagina) {
        this.pagina = pagina;
    }

    public List<NotaDto> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<NotaDto> detalle) {
        this.detalle = detalle;
    }
}
