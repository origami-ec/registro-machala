package org.origami.ws.service;

import org.origami.ws.entities.origami.Feriados;
import org.origami.ws.repository.origami.FeriadosRepository;
import org.origami.ws.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AppServices {

    @Autowired
    private FeriadosRepository feriadosRepository;

    public Date diasEntregaTramite(Date fecha, int dias, int horas, int minute) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar cal = Calendar.getInstance();
            Optional<Feriados> fe;
            cal.setTime(fecha);
            int hoy;
            if (dias > 0) {
                do {
                    cal.add(Calendar.DAY_OF_YEAR, 1);
                    hoy = cal.get(Calendar.DAY_OF_WEEK);
                    if (hoy != Calendar.SATURDAY && hoy != Calendar.SUNDAY) {
                        fe = feriadosRepository.findOne(Example.of(new Feriados(sdf.parse(sdf.format(cal.getTime())))));
                        if (!fe.isPresent()) {
                            dias--;
                        }
                    }
                } while (dias > 0);
            }

            if (horas > 0) {
                do {
                    cal.add(Calendar.HOUR, 1);
                    hoy = cal.get(Calendar.DAY_OF_WEEK);
                    if (hoy != Calendar.SATURDAY && hoy != Calendar.SUNDAY) {
                        fe = feriadosRepository.findOne(Example.of(new Feriados(sdf.parse(sdf.format(cal.getTime())))));
                        if (!fe.isPresent()) {
                            horas--;
                        }
                    }
                } while (horas > 0);
            }
            if (minute > 0) {
                do {
                    cal.add(Calendar.MINUTE, 1);
                    hoy = cal.get(Calendar.DAY_OF_WEEK);
                    if (hoy != Calendar.SATURDAY && hoy != Calendar.SUNDAY) {
                        fe = feriadosRepository.findOne(Example.of(new Feriados(sdf.parse(sdf.format(cal.getTime())))));
                        if (!fe.isPresent()) {
                            minute--;
                        }
                    }
                } while (minute > 0);
            }
            return cal.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return Utility.calcularEntregasFechas(fecha, dias, horas, minute);
        }
    }

}
