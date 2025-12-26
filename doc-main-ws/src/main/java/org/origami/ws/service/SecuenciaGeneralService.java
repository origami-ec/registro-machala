package org.origami.ws.service;

import org.origami.ws.entities.origami.SecuenciaGeneral;
import org.origami.ws.repository.origami.SecuenciaGeneralRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.math.BigInteger;
import java.util.Calendar;

@Service
public class SecuenciaGeneralService {

    @Autowired
    private SecuenciaGeneralRepository repository;

    @Transactional
    @Lock(LockModeType.WRITE)
    public Integer getSecuenciaGeneral(String codigo) {
        Calendar c = Calendar.getInstance();
        Integer anio = c.get(Calendar.YEAR);
        SecuenciaGeneral sg;
        try {
            sg = repository.findByCode(codigo);
            if (sg == null) {
                sg = new SecuenciaGeneral();
                sg.setAnio(anio);
                sg.setCode(codigo);
                sg.setSecuencia(BigInteger.ZERO);
            }
            sg.setSecuencia(sg.getSecuencia().add(BigInteger.ONE));
            sg = repository.save(sg);
            return sg.getSecuencia().intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    @Transactional
    @Lock(LockModeType.WRITE)
    public Integer getSecuenciaGeneralByAnio(String codigo) {
        Calendar c = Calendar.getInstance();
        Integer anio = c.get(Calendar.YEAR);
        SecuenciaGeneral sg;
        try {
            sg = repository.findByCodeAndAnio(codigo, anio);
            if (sg == null) {
                sg = new SecuenciaGeneral();
                sg.setAnio(anio);
                sg.setCode(codigo);
                sg.setSecuencia(BigInteger.ZERO);
            }
            sg.setSecuencia(sg.getSecuencia().add(BigInteger.ONE));
            sg = repository.save(sg);
            return sg.getSecuencia().intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Integer getSecuenciaGeneralByAnioYDepartamento(String codigo, Long departamento) {
        Calendar c = Calendar.getInstance();
        Integer anio = c.get(Calendar.YEAR);
        SecuenciaGeneral sg;
        try {
            sg = repository.findByCodeAndAnioAndDepartamento(codigo, anio, departamento);
            if (sg == null) {
                sg = new SecuenciaGeneral();
                sg.setAnio(anio);
                sg.setCode(codigo);
                sg.setDepartamento(departamento);
                sg.setSecuencia(BigInteger.ZERO);
            }
            sg.setSecuencia(sg.getSecuencia().add(BigInteger.ONE));
            sg = repository.save(sg);
            return sg.getSecuencia().intValue();
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        }
    }

}
