package org.origami.ws.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Optional;

import org.origami.ws.entities.origami.HistoricoTramites;
import org.origami.ws.entities.origami.Observaciones;
import org.origami.ws.entities.origami.SecuenciaTramites;
import org.origami.ws.entities.origami.ServiciosDepartamento;
import org.origami.ws.repository.origami.HistoricoTramiteRepository;
import org.origami.ws.repository.origami.ObservacionRepository;
import org.origami.ws.repository.origami.SecuenciaTramitesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class HistoricoTramiteService {

    @Autowired
    private HistoricoTramiteRepository historicoTramiteRepository;
    @Autowired
    private SecuenciaGeneralService secuenciaGeneralService;
    @Autowired
    private ObservacionRepository observacionRepository;
    @Autowired
    private SecuenciaTramitesRepository secuenciaTramitesRepository;


    public HistoricoTramites find(HistoricoTramites data) {
        Optional<HistoricoTramites> tramites = historicoTramiteRepository.findOne(Example.of(data));
        return tramites.orElse(null);
    }

    public String findProcessInstace(String codigo) {
        HistoricoTramites tramite = historicoTramiteRepository.findIdProceso(codigo);
        return tramite != null ? tramite.getIdProceso() : "";
    }

    public HistoricoTramites findProcessId(String id) {
        HistoricoTramites tramite = historicoTramiteRepository.findByIdProceso(id);
        return tramite;
    }

    public HistoricoTramites findProcessTemp(String id) {
        HistoricoTramites tramite = historicoTramiteRepository.findByIdProcesoTemp(id);
        return tramite;
    }

    public HistoricoTramites registrar(HistoricoTramites historicoTramites, ServiciosDepartamento tipoServicio, Boolean interno) {
        try {
            Calendar c = Calendar.getInstance();
            int anio = c.get(Calendar.YEAR);
            if (historicoTramites.getCodigo() == null || historicoTramites.getCodigo().isEmpty()) {
                SecuenciaTramites num = new SecuenciaTramites();
                num.setFecha(new Date());
                num.setNumeroTramite(null);
                num = secuenciaTramitesRepository.save(num);
                Integer secuencia = secuenciaGeneralService.getSecuenciaGeneralByAnio("NUMERO_TRAMITE");
                historicoTramites.setNumTramite(num.getId());
                historicoTramites.setSecuencial(secuencia.longValue());
                String codigoExterno;
                String abr;
                abr = tipoServicio == null ? historicoTramites.getTipoTramite().getAbreviatura() : tipoServicio.getAbreviatura();
                codigoExterno = interno ? "INT-" : "EXT-";
                Formatter fmt = new Formatter();
                String codigo = codigoExterno + abr + "-" + fmt.format("%05d", historicoTramites.getSecuencial()) + "-" + anio;
                historicoTramites.setCodigo(codigo);
            }
            //secuenciaGeneralRepository.save(secuencia);
            historicoTramites.setFecha(new Date());

            historicoTramites.setFechaIngreso(new Date());
            return historicoTramiteRepository.save(historicoTramites);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public HistoricoTramites actualizar(HistoricoTramites historicoTramites) {
        return historicoTramiteRepository.save(historicoTramites);
    }

    public HistoricoTramites actualizarProcessInstance(HistoricoTramites historicoTramites) {
        HistoricoTramites ht = historicoTramiteRepository.findById(historicoTramites.getId()).get();
        ht.setIdProceso(historicoTramites.getIdProceso());
        return historicoTramiteRepository.save(ht);
    }

    public void guardarObservacionInicial(HistoricoTramites historicoTramites, String usuario) {
        Observaciones observaciones = new Observaciones();
        observaciones.setFecCre(new Date());
        observaciones.setEstado(Boolean.TRUE);
        observaciones.setIdTramite(historicoTramites);
        observaciones.setUserCre(usuario);
        observaciones.setTarea("Inicio de trámite");
        observaciones.setObservacion("Inicio de trámite");
        observacionRepository.save(observaciones);
    }

}
