package org.bcbg.service;

import org.bcbg.model.Imagenes;
import org.bcbg.model.Impresora;
import org.springframework.stereotype.Service;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.util.ArrayList;
import java.util.List;

@Service
public class GestorDocService {

    public List<Impresora> impresorasConectadas() {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        System.out.println("Number of print services: " + printServices.length);
        List<Impresora> impresoras = new ArrayList<>();
        for (PrintService printer : printServices) {
            System.out.println("Printer: " + printer.getName());
            impresoras.add(new Impresora(printer.getName()));
        }
        return impresoras;
    }

}
