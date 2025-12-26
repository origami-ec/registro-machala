package org.origami.docs.service;

import org.origami.docs.entity.FirmaElectronica;
import org.origami.docs.repository.FirmaElectronicaRepository;
import org.origami.docs.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class FirmaElectronicaService {

    @Autowired
    private FirmaElectronicaRepository repository;

    public FirmaElectronica buscarFirma(FirmaElectronica data) {
        System.out.println("buscarFirma: " + data.toString());
        FirmaElectronica fd = repository.findOne(Example.of(data)).orElse(null);
        if (fd != null) {
            fd.setFirmaCaducada(Boolean.FALSE);
            fd.setEstadofirmaCaducada("");
            if (fd.getFechaExpiracion() != null) {
                Date hoy = new Date();
                Date expiracion = fd.getFechaExpiracion();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(expiracion);
                calendar.add(Calendar.MONTH, -1); // restar un mes a la fecha

                Date expiracionProx = calendar.getTime();
                if (hoy.after(expiracion)) { //LA FIRMA YA SE ENCUENTRA CADUCADA
                    fd.setFirmaCaducada(Boolean.TRUE);
                    fd.setEstadofirmaCaducada("Su firma electrónica esta vencida por favor renuevela: Fecha de expiración: "
                            + Utils.dateFormatPattern("yyyy-MM-dd", expiracion));
                } else if (hoy.after(expiracionProx)) {
                    fd.setEstadofirmaCaducada("Su firma electrónica esta próxima a caducar: Fecha de expiración: "
                            + Utils.dateFormatPattern("yyyy-MM-dd", expiracion) + " - Faltan: " + Utils.diasRestantes(expiracion) + " días.");
                }
            }
        }
        return fd;
    }

    public FirmaElectronica guardarFirma(FirmaElectronica data) {
        return repository.save(data);
    }

}
