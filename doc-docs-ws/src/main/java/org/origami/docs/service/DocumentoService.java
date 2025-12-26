package org.origami.docs.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.origami.docs.config.AppProps;
import org.origami.docs.entity.DocumentoFirmado;
import org.origami.docs.entity.Indexacion;
import org.origami.docs.mappers.DocumentoFirmadoMapper;
import org.origami.docs.model.*;
import org.origami.docs.repository.DocumentoFirmadoRepository;
import org.origami.docs.util.Constantes;
import org.origami.docs.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;

@Service
public class DocumentoService {

    @Autowired
    private DocumentoFirmadoRepository repository;
    @Autowired
    private DocumentoFirmadoMapper mapper;
    @Autowired
    private IndexacionService indexacionService;
    @Autowired
    private AppProps appProps;
    @Autowired
    private DecryptEncrypt decryptEncrypt;

    public List<DocumentoFirmadoDTO> findAllByUser(String usuario) {
        List<DocumentoFirmado> documentos = repository.findAllByUsuario_Usuario(usuario);
        if (Utils.isEmpty(documentos)) {
            return new ArrayList<>();
        }
        return mapper.toDto(documentos);
    }

    public DocumentoFirmado guardar(DocumentoFirmado documentoFirmado) {
        Indexacion indexacion = indexacionService.consultar(Constantes.indexacionFirma);
        documentoFirmado.setDetalleDocumento(documentoFirmado.getMotivo() != null ? documentoFirmado.getMotivo() : "");
        documentoFirmado.setTipoIndexacion(indexacion.getDescripcion());
        try {
            String archivo = appProps.getRutaArchivosFD() + documentoFirmado.getArchivoFirmado();

            File f = new File(archivo);
            if (f.exists()) {
                f.delete();
            }
            Path path = Paths.get(archivo);
            Files.write(path, documentoFirmado.getArchivoDesk());
            documentoFirmado.setArchivoFirmado(archivo);
            decryptEncrypt.encrypt(archivo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return repository.save(documentoFirmado);
    }

    public Map<String, List> find(DocumentoFirmadoDTO data, Pageable pageable) {
        DocumentoFirmado doc = mapper.toEntity(data);
        Map map = new HashMap<>();
        Page<DocumentoFirmado> result = repository.findAll(Example.of(doc), pageable);
        List<DocumentoFirmadoDTO> list = mapper.toDto(result.getContent());
        List<String> pages = new ArrayList<>();
        pages.add(String.valueOf(result.getTotalPages()));
        pages.add(String.valueOf(repository.count(Example.of(doc))));
        map.put("result", list);
        map.put("pages", pages);
        return map;
    }

    public ArchivoDto imprimirArchivo(ArchivoDto dto) {
        File origen = null;
        try {
            decryptEncrypt.decrypt(dto.getRuta());
            if (dto.getFormato().startsWith("PDF")) {
                origen = new File(dto.getRuta());
            } else {

                Document document = new Document();
                String input = dto.getRuta();
                String output = FilenameUtils.removeExtension(dto.getRuta()) + ".pdf";
                FileOutputStream fos = new FileOutputStream(output);

                PdfWriter writer = PdfWriter.getInstance(document, fos);
                writer.open();
                document.open();
                document.add(Image.getInstance(Files.readAllBytes(new File(input).toPath())));
                document.close();
                writer.close();

                origen = new File(output);
            }


            String archivoDestino = appProps.getRutaArchivosNT() + dto.getNombre();

            File f = new File(archivoDestino);
            if (f.exists()) f.delete();
            PdfReader pdfReader =
                    new PdfReader(Files.readAllBytes(origen.toPath()));

            PdfStamper pdfStamper = new PdfStamper(pdfReader,
                    new FileOutputStream(archivoDestino));
            Boolean tieneNotas = Boolean.FALSE;
            for (ImagenDto pg : dto.getImagenes()) {
                if (Utils.isNotEmpty(pg.getNotas())) {
                    tieneNotas = Boolean.TRUE;
                    int countNotas = 0;
                    Rectangle pageSize = pdfReader.getPageSize(pg.getIndice() + 1);
                    System.out.println(pageSize.toString());
                    PdfContentByte pageContentByte =
                            pdfStamper.getOverContent(pg.getIndice() + 1); //se suma uno porque las paginas empiezan en 0
                    for (NotaDto nota : pg.getNotas()) {
                        countNotas = countNotas + 1;
                        Chunk textAsChunk = new Chunk("Nota # " + countNotas + ": " + nota.getTitulo());
                        textAsChunk.setBackground(colorTobase(nota.getColor()));

                        float x = Math.abs((pageSize.getWidth() - nota.getPosicionX2()));
                        float y = Math.abs((pageSize.getHeight() - nota.getPosicionY2())) + nota.getAlto();
                        ColumnText.showTextAligned(pageContentByte, Element.ALIGN_LEFT, new Phrase(textAsChunk), x, y, 0);
                    }
                }
            }
            //pdfStamper.close();

            if (tieneNotas) {
                String archivoUnido = appProps.getRutaArchivosNT() + FilenameUtils.removeExtension(dto.getNombre()) + "_notas.pdf";
                List<PDFNotas> list = new ArrayList<>();
                List<PDFNotasDetalle> notas = new ArrayList<>();
                for (ImagenDto pg : dto.getImagenes()) {
                    if (Utils.isNotEmpty(pg.getNotas())) {
                        int countNotas = 0;
                        for (NotaDto nota : pg.getNotas()) {
                            countNotas = countNotas + 1;
                            nota.setTitulo("Nota #" + countNotas + " " + nota.getTitulo());
                        }
                        notas.add(new PDFNotasDetalle(pg.getDescripcion(), pg.getNotas()));
                    }
                }
                list.add(new PDFNotas(notas));
                Map par = new HashMap();
                par.putAll(getUrlsImagenes());
                String rutaNotas = appProps.getRutaArchivosNT() + "notas_" + new Date().getTime() + ".pdf";
                this.generarPDF(armarRutaJasper("notasDocumento"), new PDFNotas(notas), par, rutaNotas);
                pdfStamper.close();

                System.out.println("PDF modified successfully.");
                File archivo = new File(archivoDestino);
                File archivonotas = new File(rutaNotas);
                List<File> files = new ArrayList<>();
                files.add(archivo);
                files.add(archivonotas);
                mergePDFFiles(files, archivoUnido);
                dto.setRutaNotas(archivoUnido);
            } else {
                dto.setRutaNotas(origen.getAbsolutePath());
            }
            pdfReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            decryptEncrypt.encrypt(origen.getAbsolutePath());
        }
        return dto;
    }

    public void generarPDF(String resourceLocation, Object dataSource, Map parameters, String notasFile) {
        try {
            JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(Arrays.asList(dataSource));
            String os = System.getProperty("os.name");
            JasperPrint jasperPrint;
            File file = ResourceUtils.getFile(resourceLocation);
            jasperPrint = JasperFillManager.fillReport(file.getAbsolutePath(), parameters, jrBeanCollectionDataSource);
            JasperExportManager.exportReportToPdfFile(jasperPrint, notasFile);
        } catch (Exception e) {
            e.printStackTrace();
            ;
        }
    }


    public String armarRutaJasper(String nombre) {
        if (!appProps.getActiveProfile().contains("prod")) {
            return "classpath:reportes/" + nombre + ".jasper";
        } else {
            return appProps.getRutaReportes() + nombre + ".jasper";
        }

    }


    public Map getUrlsImagenes() {
        Map map = new HashMap();
        if (!appProps.getActiveProfile().contains("prod")) {
            map.put("HEADER_URL", "classpath:imagenes/cabecera.png");
            map.put("WATERMARK_URL", "classpath:imagenes/marca_agua.png");
            map.put("FOOTER_URL", "classpath:imagenes/pie_pagina.png");
        } else {
            map.put("HEADER_URL", appProps.getRutaImagenes() + "cabecera.png");
            map.put("WATERMARK_URL", appProps.getRutaImagenes() + "marca_agua.png");
            map.put("FOOTER_URL", appProps.getRutaImagenes() + "pie_pagina.png");
        }
        return map;
    }

    public void mergePDFFiles(List<File> files, String mergedFileName) {
        try {
            PDFMergerUtility pdfmerger = new PDFMergerUtility();
            for (File file : files) {
                PDDocument document = PDDocument.load(file, MemoryUsageSetting.setupTempFileOnly());
                pdfmerger.setDestinationFileName(mergedFileName);
                pdfmerger.addSource(file);
                pdfmerger.mergeDocuments(MemoryUsageSetting.setupTempFileOnly());
                document.close();
            }

          /*  PDFMergerUtility mergerUtility = new PDFMergerUtility();
            PDDocument srcDoc = PDDocument.load(Files.readAllBytes(file1.toPath()));
            PDDocument dstDoc = PDDocument.load(file2);
            mergerUtility.appendDocument( srcDoc, dstDoc);
            dstDoc.save(mergedFileName);
            srcDoc.close();
            dstDoc.close();*/
            decryptEncrypt.encrypt(mergedFileName);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private BaseColor colorTobase(String color) {
        if (color != null)
            switch (color) {
                case "#FFFFFF": //"Blanco":
                    return BaseColor.WHITE;
                case "#A9D0F5": // "Azul":
                    return BaseColor.BLUE;
                case "#F5A9F2": // "Lila":
                    return BaseColor.MAGENTA;
                case "#F7D358":// "Naranja":
                    return BaseColor.ORANGE;
                case "#F78181": //"Rojo":
                    return BaseColor.RED;
                case "#F3F781": //"Amarillo":
                    return BaseColor.YELLOW;
                case "#BDBDBD": //"Gris":
                    return BaseColor.GRAY;
                case "#008000": //"Verde":
                    return BaseColor.GREEN;
            }
        return BaseColor.WHITE;
    }

}
