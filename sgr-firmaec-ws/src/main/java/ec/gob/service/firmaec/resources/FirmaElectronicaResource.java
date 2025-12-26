package ec.gob.service.firmaec.resources;

import ec.gob.service.firmaec.model.FirmaElectronica;
import ec.gob.service.firmaec.model.FirmaElectronicaDocumento;
import ec.gob.service.firmaec.rubrica.sign.FirmaElectronicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping("/api/")
public class FirmaElectronicaResource {

    @Autowired
    private FirmaElectronicaService service;

    @RequestMapping(value = "firmaElectronica/generar", method = RequestMethod.POST)
    public ResponseEntity<?> generar(@RequestBody FirmaElectronica firmaElectronica) {
        try {
            System.out.println("firmaElectronica: " + firmaElectronica.toString());
            firmaElectronica = service.firmarDocumento(firmaElectronica);

            return new ResponseEntity<>(firmaElectronica, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @RequestMapping(value = "firmaDigital/generar", method = RequestMethod.POST)
    public ResponseEntity<?> generarDigital(@RequestBody FirmaElectronica firmaElectronica) {
        try {
            //System.out.println(firmaElectronica.toString());
            return new ResponseEntity<>(service.firmarDocumentoDigital(firmaElectronica), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @RequestMapping(value = "firmaElectronica/validar", method = RequestMethod.POST)
    public ResponseEntity<?> validar(@RequestBody FirmaElectronica firmaElectronica) {
        try {
            return new ResponseEntity<>(service.validarCertificado(firmaElectronica), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            firmaElectronica.setUid(null);
            firmaElectronica.setEstadoFirma(e.getMessage());
            return new ResponseEntity<>(firmaElectronica, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "firmaElectronica/verificarDocumento", method = RequestMethod.POST)
    public ResponseEntity<?> verificarDocumento(@RequestBody FirmaElectronica firmaElectronica) {
        try {

            return new ResponseEntity<>(service.verificarDocumento(firmaElectronica), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(firmaElectronica, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "firmaElectronica/generarDocumentoAnterior", method = RequestMethod.POST)
    public ResponseEntity<?> generar(@RequestBody FirmaElectronicaDocumento firmaElectronica) {
        try {
            //System.out.println(firmaElectronica.toString());
            Date horaActual = new Date();
            if (cambiarHora(firmaElectronica.getFechaFirmar()) == 0) {
                FirmaElectronica fe = service.firmarDocumento(firmaElectronica.getFirmaElectronica());
                cambiarHora(horaActual);
                return new ResponseEntity<>(fe, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.OK);
            //cambiarHora(horaActual);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    private int cambiarHora(Date fecha) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        String newDate = calendar.get(Calendar.YEAR) + "-0" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":00";

        String cmd = "date \"+%d-%m-%C%y %H:%M:%S\" --set \"" + newDate.trim() + "\"";
        System.out.println("cmd: " + cmd);
        String s;
        Process p;
        try {
            ///p =Runtime.getRuntime().exec(cmd);
            p = Runtime.getRuntime().exec(new String[]{"date", "--set", newDate.trim()});
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null) {
                System.out.println("line: " + s);
            }
            p.waitFor();
            System.out.println("exit: " + p.exitValue());
            p.destroy();
            return p.exitValue();
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
        }
        return 1;
    }

}
