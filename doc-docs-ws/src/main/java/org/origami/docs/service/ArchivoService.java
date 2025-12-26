package org.origami.docs.service;

import com.google.gson.Gson;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.xmlbeans.XmlOptions;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.origami.docs.config.AppProps;
import org.origami.docs.entity.Archivo;
import org.origami.docs.entity.ArchivoIndexacion;
import org.origami.docs.entity.Imagen;
import org.origami.docs.entity.Usuario;
import org.origami.docs.mappers.ArchivoMapper;
import org.origami.docs.mappers.NotaMapper;
import org.origami.docs.model.*;
import org.origami.docs.repository.ArchivoRepository;
import org.origami.docs.util.Constantes;
import org.origami.docs.util.Transform;
import org.origami.docs.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@Service
public class ArchivoService {

    @Autowired
    private AppProps appProps;
    @Autowired
    private ArchivoRepository archivoRepository;
    @Autowired
    private ArchivoMapper archivoMapper;
    @Autowired
    private NotaMapper notaMapper;
    @Autowired
    private DocumentoService documentoService;
    @Autowired
    private DecryptEncrypt decryptEncrypt;

    public Map<String, List> find(ArchivoDto data, Pageable pageable) {
        //System.out.println(data.toString());
        Archivo archivo = archivoMapper.toEntity(data);
        Map map = new HashMap<>();

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("numTramite", contains().ignoreCase())
                .withMatcher("tramite", contains().ignoreCase())
                .withMatcher("detalleDocumento", contains().ignoreCase())
                .withMatcher("nombre", contains().ignoreCase());

        Pageable paging = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "_id");
        Page<Archivo> result = archivoRepository.findAll(Example.of(archivo, matcher), paging);
        List<ArchivoDto> archivosDto = archivoMapper.toDto(result.getContent());
        List<String> pages = new ArrayList<>();
        pages.add(String.valueOf(result.getTotalPages()));
        pages.add(String.valueOf(archivoRepository.count(Example.of(archivo, matcher))));
        map.put("result", archivosDto);
        map.put("pages", pages);
        return map;
    }

    public Map<String, List> find2(ArchivoDto data, Pageable pageable) {
        //System.out.println(data.toString());
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();
        Archivo archivo = archivoMapper.toEntity(data);
        Map map = new HashMap<>();
        Page<Archivo> result = archivoRepository.findAll(Example.of(archivo, exampleMatcher), pageable);
        List<ArchivoDto> archivosDto = archivoMapper.toDto(result.getContent());
        List<String> pages = new ArrayList<>();
        pages.add(String.valueOf(result.getTotalPages()));
        pages.add(String.valueOf(archivoRepository.count(Example.of(archivo))));
        map.put("result", archivosDto);
        map.put("pages", pages);
        return map;
    }

    public ArchivoDto consultarArchivo(ArchivoDto dto) {
        Archivo archivo = archivoMapper.toEntity(dto);
        System.out.println("dto " + dto.getId());
        System.out.println("archivo " + archivo.get_id());
        Optional<Archivo> archivoBD = archivoRepository.findOne(Example.of(archivo));
        System.out.println("archivoBD " + archivoBD.isPresent());
        return archivoBD.map(value -> archivoDto(value)).orElse(null);
    }

    public ArchivoDto visualizarArchivo(ArchivoDto dto) {
        try {
            Archivo archivo = archivoMapper.toEntity(dto);
            Optional<Archivo> archivoBD = archivoRepository.findOne(Example.of(archivo));
            return archivoBD.map(value -> this.generarImagenes(value)).orElse(null);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    private ArchivoDto generarImagenes(Archivo archivo) {
        try {
            BufferedImage bim;
            ArchivoDto dto = archivoMapper.toDto(archivo);
            File file = new File(dto.getRuta());
            File pagina;
            if (file.exists()) {
                decryptEncrypt.decrypt(dto.getRuta());
                PDDocument document = PDDocument.load(file);
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                for (ImagenDto i : dto.getImagenes()) {
                    i.setApiUrl(appProps.getApiContext() + appProps.getApiImagenes() + i.getNombreImagen());
                    pagina = new File(i.getRuta());
                    if (!pagina.exists()) {
                        bim = pdfRenderer.renderImageWithDPI(i.getIndice(), 200, ImageType.RGB);
                        ImageIO.write(bim, "PNG", pagina);
                    }
                }
                decryptEncrypt.encrypt(dto.getRuta());
                return dto;
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

    public ArchivoDto eliminarArchivo(ArchivoDto dto) {
        try {
            /*Archivo archivo = archivoMapper.toEntity(dto);
            Optional<Archivo> archivoBD = archivoRepository.findOne(Example.of(archivo));
            archivoBD.ifPresent(value -> archivoRepository.delete(value));*/
            archivoRepository.deleteById(dto.getId());
            File temp = new File(dto.getRuta());
            if (temp.delete()) {
                System.out.println("borrado: " + dto.getRuta());
            }
            return dto;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    /**
     * @param dto recibe un dto lleno pro solo se usan los campos de la
     * descripcion del documento y los campos de la indeacion porque para eso es
     * este metodo
     * @return el mismo dto xd
     */
    public ArchivoDto actualizarDatosIndex(ArchivoDto dto) {

        Optional<Archivo> archivoBD = archivoRepository.findById(dto.getId());
        if (archivoBD.isPresent()) {
            Archivo arc = archivoBD.get();
            arc.setDetalleDocumento(dto.getDetalleDocumento());
            arc.setDetalles(dto.getDetalles());
            archivoRepository.save(arc);
        }
        return dto;
    }

    public Data agregarNota(ImagenNotaDto dto) {
        try {
            Optional<Archivo> archivo = archivoRepository.findById(dto.getArchivoId());
            if (archivo.isPresent()) {
                Archivo arc = archivo.get();
                Imagen imagen = arc.getImagenes().get(dto.getIndiceImagen());
                if (dto.getTipo().equals(Constantes.notaAgregar)) {
                    imagen.getNotas().add(notaMapper.toEntity(dto.getNota()));
                } else {
                    imagen.getNotas().remove(dto.getIndiceNota().intValue());
                }
                arc.getImagenes().set(dto.getIndiceImagen(), imagen);
                arc = archivoRepository.save(arc);
                return new Data(0L, new Gson().toJson(archivoDto(arc)), "Datos grabados correctamente");
            } else {
                return new Data(1L, "", "No se encontraron datos del archivo");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new Data(1L, "", "No se encontraron datos del archivo y de la página");
        }

    }

    public byte[] consultarImagenes(String imagen) throws IOException {
        File initialFile = new File(appProps.getRutaImagen() + imagen);
        InputStream targetStream = new FileInputStream(initialFile);
        return IOUtils.toByteArray(targetStream);
    }

    public ArchivoDto cargarDocumento(String formato, String usuario, String indexacion, MultipartFile document) {
        try {
            Gson gson = new Gson();
            String archivoFecha = generarNombreArchivo(document.getOriginalFilename());
            String archivo = appProps.getRutaArchivo() + archivoFecha;
            System.out.println("Ruta de Guardado de Doc -> " + archivo);

            ArchivoIndexDto archivoIndex = gson.fromJson(indexacion, ArchivoIndexDto.class);
            List<ArchivoIndexacion> detalle = new ArrayList<>();
            if (Utils.isNotEmpty(archivoIndex.getDetalles())) {
                for (ArchivoIndexCampoDto campo : archivoIndex.getDetalles()) {
                    detalle.add(new ArchivoIndexacion(campo.getTipoDato(), campo.getDescripcion(),
                            campo.getCategorias(), campo.getDetalle(), campo.getObligatorio()));
                }
            }

            File f = new File(archivo);
            FileUtils.writeByteArrayToFile(f, document.getBytes());

            String tipoFormato = Utils.verificarArchivo(f, archivoIndex.getFormatoUpload());

            Archivo arc = new Archivo();
            arc.setNombre(archivoFecha);
            arc.setPeso(FileUtils.byteCountToDisplaySize(document.getSize()));
            arc.setUsuario(gson.fromJson(usuario, Usuario.class));
            arc.setTipo(Constantes.tipoArchivo);
            arc.setFecha(Utils.getDate(new Date()));
            arc.setRuta(archivo);
            arc.setTramite(archivoIndex.getTramite());
            arc.setNumTramite(archivoIndex.getNumTramite());
            arc.setFormato(tipoFormato);
            arc.setTipoContenido(document.getContentType());
            arc.setFechaCreacion(Utils.getFechaMongo());

            arc.setTipoIndexacion(archivoIndex.getTipoIndexacion());
            arc.setDetalleDocumento(archivoIndex.getDetalleDocumento());
            arc.setEstado(archivoIndex.getEstado());
            arc.setDetalles(detalle);
            //if (Boolean.TRUE.equals(generarImagenes)) {
            //  convertirPdfImagen(arc, archivo, tipoFormato);
            //}

            arc.setNumeroPaginas(numeroPaginas(archivo, tipoFormato));
            ///  arc.setImagenes(imagens);
            arc = archivoRepository.save(arc);
            decryptEncrypt.encrypt(archivo);
            arc.setImagenes(new ArrayList<>());
            List<Imagen> listImg = new ArrayList<>();
            listImg.add(new Imagen(0, "Pagina # 1", Constantes.sinResultados, appProps.getRutaImagen() + Constantes.sinResultados));
            arc.setImagenes(listImg);
            return archivoDto(arc);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArchivoDto imprimirArchivo(ArchivoDto dto) {

        return documentoService.imprimirArchivo(dto);
    }

    public Integer numeroPaginas(String archivo, String tipoFormato) {
        switch (tipoFormato) {
            case "PDF":
                try {
                    PDDocument document = PDDocument.load(new File(archivo));
                    return document.getNumberOfPages();
                } catch (IOException e) {
                    e.printStackTrace();
                    return 1;
                }
            case "IMG":
                return 1;
            default:
                return 1;
        }
    }

    public void convertirPdfImagen(Archivo arc, String archivo, String tipoFormato) {

        List<Imagen> listImg = new ArrayList<>();
        String archivoImagen, archivoImagenTemp;
        File file;
        String nameFile;
        switch (tipoFormato) {
            case "PDF":
                BufferedImage bim;
                file = new File(archivo);
                nameFile = file.getName().replace(".pdf", "_");
                archivoImagen = appProps.getRutaImagen();

                Date fa = new Date();
                if (archivoImagen.endsWith("/")) {
                    archivoImagen = archivoImagen + Utils.getAnio(fa) + "/" + Utils.getMes(fa) + "/" + Utils.getDia(fa) + "/";
                } else {
                    archivoImagen = archivoImagen + "/" + Utils.getAnio(fa) + "/" + Utils.getMes(fa) + "/" + Utils.getDia(fa) + "/";
                }
                Utils.crearDirectorio(archivoImagen);
                archivoImagen = archivoImagen + nameFile;
                try (final PDDocument document = PDDocument.load(file)) {
                    PDFRenderer pdfRenderer = new PDFRenderer(document);
                    for (int pagina = 0; pagina < document.getNumberOfPages(); ++pagina) {
                        System.out.println(Constantes.pagNombreImagen + ": " + pagina);
                        bim = pdfRenderer.renderImageWithDPI(pagina, 200, ImageType.RGB);
                        int pag = pagina + 1;
                        archivoImagenTemp = archivoImagen + pag + Constantes.extImagen;
                        listImg.add(new Imagen(pagina, Constantes.pagNombreImagen + pag, nameFile + pag + Constantes.extImagen, archivoImagenTemp));
                        ImageIO.write(bim, "PNG", new File(archivoImagenTemp));
                    }
                } catch (IOException e) {
                    listImg.add(new Imagen(0, "Pagina # 1", Constantes.sinResultados, appProps.getRutaImagen() + Constantes.sinResultados));
                    e.printStackTrace();
                }
                break;
            case "IMG":
                try {
                    file = new File(archivo);
                    String fileNameWithOutExt = FilenameUtils.removeExtension(file.getName());
                    archivoImagen = appProps.getRutaImagen() + fileNameWithOutExt + Constantes.extImagen;
                    File newfile = new File(archivoImagen);
                    FileUtils.copyFile(file, newfile);
                    listImg.add(new Imagen(0, Constantes.pagNombreImagen + 1, fileNameWithOutExt + Constantes.extImagen, archivoImagen));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

        }
        arc.setImagenes(listImg);

    }

    public ArchivoDto archivoDtoV1(Archivo archivo) {
        ArchivoDto dto = archivoMapper.toDto(archivo);
        if (Utils.isNotEmpty(dto.getImagenes())) {
            for (ImagenDto i : dto.getImagenes()) {
                i.setApiUrl(appProps.getApiContext() + appProps.getApiImagenes() + i.getNombreImagen());
            }
        }
        return dto;
    }

    private ArchivoDto archivoDto(Archivo archivo) {
        try {
            BufferedImage bim;
            ArchivoDto dto = archivoMapper.toDto(archivo);
            System.out.println("dto.getRuta() " + dto.getRuta());
            File file = new File(dto.getRuta());

            System.out.println("file.exists() " + file.exists());
            File pagina;
            if (file.exists()) {
                decryptEncrypt.decrypt(dto.getRuta());
                /*  PDDocument document = PDDocument.load(file);
                PDFRenderer pdfRenderer = new PDFRenderer(document);
               for (ImagenDto i : dto.getImagenes()) {
                    i.setApiUrl(appProps.getApiContext() + appProps.getApiImagenes() + i.getNombreImagen());
                    pagina = new File(i.getRuta());
                    if (!pagina.exists()) {
                        bim = pdfRenderer.renderImageWithDPI(i.getIndice(), 200, ImageType.RGB);
                        ImageIO.write(bim, "PNG", pagina);
                    }
                }*/
                decryptEncrypt.encrypt(dto.getRuta());
                return dto;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String generarNombreArchivo(String nombreArchivo) {
        Date hoy = Calendar.getInstance().getTime();
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String fecha = format.format(hoy);
        nombreArchivo = (fecha + "_" + nombreArchivo).replace(" ", "_").replace(":", "_");
        return nombreArchivo;
    }

    public List<Data> obtenerSellos() {
        List<Data> list = new ArrayList<>();
        List<Archivo> sellos = archivoRepository.findAllByTipoIndexacion("Sellos");
        for (Archivo a : sellos) {
            ArchivoDto d = archivoDto(a);
            list.add(new Data(d.getImagenes().get(0).getApiUrl(), d.getDetalleDocumento()));
        }

        return list;
    }

    public List<ArchivoDto> busquedaAvanzada(ArchivoIndexDto index) {
        List<Archivo> list = new ArrayList<>();
        List<ArchivoDto> dtolist = new ArrayList<>();
        for (ArchivoIndexCampoDto field : index.getDetalles()) {
            String det = field.getDetalle();
            List<Archivo> temp = new ArrayList<>();

            String numTramite = index.getNumTramite() == null ? null : index.getNumTramite().isEmpty() ? null : index.getNumTramite();
            String tipoIndexacion = index.getTipoIndexacion() == null ? null : index.getTipoIndexacion().isEmpty() ? null : index.getTipoIndexacion();
            String detDocumento = index.getDetalleDocumento() == null ? null : index.getDetalleDocumento().isEmpty() ? null : index.getDetalleDocumento();
            String detalleIndex = field.getDetalle() == null ? null : field.getDetalle().isEmpty() ? null : field.getDetalle();

            if (numTramite != null && tipoIndexacion != null && detDocumento != null && detalleIndex != null) {
                System.out.println("1er.- numTramite: " + numTramite + "; tipoIndexacion: " + tipoIndexacion + "; detDocumento: " + detDocumento + "; detalleIndex: " + detalleIndex);
                temp = archivoRepository.busquedaByIndexacion(numTramite, tipoIndexacion, detDocumento, detalleIndex);
            } else if (numTramite != null && tipoIndexacion != null && detDocumento != null && detalleIndex == null) {
                System.out.println("2do.- numTramite: " + numTramite + "; tipoIndexacion: " + tipoIndexacion + "; detDocumento: " + detDocumento + "; detalleIndex: " + detalleIndex);
                temp = archivoRepository.busquedaByIndexacion(numTramite, tipoIndexacion, detDocumento);
            } else if (numTramite != null && tipoIndexacion != null && detDocumento == null && detalleIndex == null) {
                System.out.println("3er.- numTramite: " + numTramite + "; tipoIndexacion: " + tipoIndexacion + "; detDocumento: " + detDocumento + "; detalleIndex: " + detalleIndex);
                temp = archivoRepository.busquedaByIndexacion(numTramite, tipoIndexacion);
            } else if (numTramite != null && tipoIndexacion == null && detDocumento == null && detalleIndex == null) {
                System.out.println("4to.- numTramite: " + numTramite + "; tipoIndexacion: " + tipoIndexacion + "; detDocumento: " + detDocumento + "; detalleIndex: " + detalleIndex);
                temp = archivoRepository.busquedaByIndexacion(numTramite);
            } else if (numTramite == null && tipoIndexacion != null && detDocumento == null && detalleIndex == null) {
                System.out.println("5to.- numTramite: " + numTramite + "; tipoIndexacion: " + tipoIndexacion + "; detDocumento: " + detDocumento + "; detalleIndex: " + detalleIndex);
                temp = archivoRepository.busquedaByTipoIndexacion(tipoIndexacion);
            } else if (numTramite == null && tipoIndexacion != null && detDocumento != null && detalleIndex == null) {
                System.out.println("6to.- numTramite: " + numTramite + "; tipoIndexacion: " + tipoIndexacion + "; detDocumento: " + detDocumento + "; detalleIndex: " + detalleIndex);
                temp = archivoRepository.busquedaByTipoIndexacion(tipoIndexacion, detDocumento);
            } else if (numTramite == null && tipoIndexacion != null && detDocumento != null && detalleIndex != null) {
                System.out.println("7mo.- numTramite: " + numTramite + "; tipoIndexacion: " + tipoIndexacion + "; detDocumento: " + detDocumento + "; detalleIndex: " + detalleIndex);
                temp = archivoRepository.busquedaByTipoIndexacion(tipoIndexacion, detDocumento, detalleIndex);
            } else if (numTramite == null && tipoIndexacion != null && detDocumento == null && detalleIndex != null) {
                System.out.println("8vo.- numTramite: " + numTramite + "; tipoIndexacion: " + tipoIndexacion + "; detDocumento: " + detDocumento + "; detalleIndex: " + detalleIndex);
                temp = archivoRepository.busquedaByTipoIndexacionDets(tipoIndexacion, detalleIndex);
            } else if (numTramite != null && tipoIndexacion != null && detDocumento == null && detalleIndex != null) {
                System.out.println("9no.- numTramite: " + numTramite + "; tipoIndexacion: " + tipoIndexacion + "; detDocumento: " + detDocumento + "; detalleIndex: " + detalleIndex);
                temp = archivoRepository.busquedaByTramiteTipoIndexacionDets(numTramite, tipoIndexacion, detalleIndex);
            } else {
                System.out.println("10mo.- numTramite: " + numTramite + "; tipoIndexacion: " + tipoIndexacion + "; detDocumento: " + detDocumento + "; detalleIndex: " + detalleIndex);
                temp = archivoRepository.busquedaByIndexacion(numTramite, tipoIndexacion, detDocumento, detalleIndex);
            }
            for (Archivo a : temp) {
                if (!list.contains(a)) {
                    list.add(a);
                }
            }
        }
        if (Utils.isNotEmpty(list)) {
            for (Archivo a : list) {
                dtolist.add(archivoDto(a));
            }
        }
        return dtolist;
    }

    public ArchivoDto convertirArchivo(ArchivoDto dto) {
        String conversion = dto.getConvertir();
        dto = consultarArchivo(dto);
        try {
            decryptEncrypt.decrypt(dto.getRuta());
            String archivo = dto.getRuta();
            String archivoTemp = "";
            String archivoRes = "";

            switch (conversion) {
                case "PDF_WORD":

                    archivoTemp = FilenameUtils.removeExtension(dto.getRuta()) + "_doc.pdf";
                    archivoRes = FilenameUtils.removeExtension(dto.getRuta()) + "_doc.docx";
                    File f = new File(archivoTemp);
                    FileUtils.copyFile(new File(archivo), f);
                    String parent = f.getParent() + "\\";
                    String command = "\"C:\\Program Files\\LibreOffice\\program\\soffice\"  --infilter=\"writer_pdf_import\" --convert-to docx --outdir " + parent + "   " + archivoTemp.replace("/", "\\");
                    System.out.println("command: " + command);
                    Process p = Runtime.getRuntime().exec(command);
                    p.waitFor();

                    /*
                    // Loading PDF
                    File pdffile = new File(archivo);
                    PDDocument pdDocument = PDDocument.load(pdffile);

                    // Splitter Class
                    Splitter splitting = new Splitter();

                    // Splitting the pages into multiple PDFs
                    List<PDDocument> Page = splitting.split(pdDocument);

                    // Using a iterator to Traverse all pages
                    Iterator<PDDocument> iteration
                            = Page.listIterator();

                    // Saving each page as an individual document
                    int j = 1;
                    List<String> archivosPDF = new ArrayList<>();
                    List<String> archivosDocx = new ArrayList<>();
                    while (iteration.hasNext()) {
                        String newPDF = FilenameUtils.removeExtension(dto.getRuta()) + (j++) + ".pdf";
                        archivosPDF.add(newPDF);
                        PDDocument pd = iteration.next();
                        pd.save(newPDF);
                    }
                    j = 1;

                    for (String s : archivosPDF) {
                        String htmlTemp = FilenameUtils.removeExtension(s) + "_temp.html";
                        archivoTemp = FilenameUtils.removeExtension(s) + "_temp.html";
                        PDDocument pdf = PDDocument.load(new File(s));
                        Writer output = new PrintWriter(htmlTemp, "utf-8");
                        new PDFDomTree().writeText(pdf, output);

                        output.close();


                         StringBuilder contentBuilder = new StringBuilder();
                        try {
                            BufferedReader in = new BufferedReader(new FileReader(htmlTemp));
                            String str;
                            while ((str = in.readLine()) != null) {
                                contentBuilder.append(str);
                            }
                            in.close();
                        } catch (IOException e) {
                        }
                        String content = contentBuilder.toString();
                        // To docx, with content controls
                        WordprocessingMLPackage wordProcess = WordprocessingMLPackage.createPackage();

                        XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(wordProcess);

                        wordProcess.getMainDocumentPart().getContent().addAll(
                                XHTMLImporter.convert(content, null));

                        wordProcess.save(new File(archivoTemp));

                    }


                    System.out.println("Splitted Pdf Successfully.");
                    pdDocument.close();


                     */
 /*     List<File> files = new ArrayList<>();
                    for (String s : archivosDocx) {
                        files.add(new File(s));
                    }
                    FileOutputStream faos = new FileOutputStream(archivoRes);
                    new MergeDocx().mergeDocx(files, faos);
                    merge(files, archivoRes);*/

 /*    com.aspose.pdf.Document word = new com.aspose.pdf.Document(archivo);
                    archivoTemp = FilenameUtils.removeExtension(dto.getRuta()) + "_temp.docx";
                    archivoRes = FilenameUtils.removeExtension(dto.getRuta()) + ".docx";

                    word.save(archivoTemp, SaveFormat.DocX);

                    XWPFDocument doc = new XWPFDocument(OPCPackage.open(archivoTemp));
                    for (XWPFParagraph p : doc.getParagraphs()) {
                        if (p.getText().contains(Constantes.reemplazarEvaluacion)) {
                            List<XWPFRun> runs = p.getRuns();
                            if (runs != null) {
                                for (XWPFRun r : runs) {
                                    String text = r.getText(0);
                                    if (Utils.isNotEmptyString(text)) {
                                        switch (text) {
                                            case Constantes.reempEvaluation:
                                                text = text.replace(Constantes.reempEvaluation, "");
                                                break;
                                            case Constantes.reempOnly:
                                                text = text.replace(Constantes.reempOnly, "");
                                                break;
                                            case Constantes.reempCreated:
                                                text = text.replace(Constantes.reempCreated, "");
                                                break;
                                            case Constantes.reempWith:
                                                text = text.replace(Constantes.reempWith, "");
                                                break;
                                            case Constantes.reempAsposePDF:
                                                text = text.replace(Constantes.reempAsposePDF, "");
                                                break;
                                            case Constantes.reempCopyright:
                                                text = text.replace(Constantes.reempCopyright, "");
                                                break;
                                            case Constantes.reemp20022019:
                                                text = text.replace(Constantes.reemp20022019, "");
                                                break;
                                            case Constantes.reempAspose:
                                                text = text.replace(Constantes.reempAspose, "");
                                                break;
                                            case Constantes.reempPty:
                                                text = text.replace(Constantes.reempPty, "");
                                                break;
                                            case Constantes.reempPtyLtd:
                                                text = text.replace(Constantes.reempPtyLtd, "");
                                                break;
                                            default:
                                                break;
                                        }
                                        r.setText(text, 0);
                                    }
                                }
                            }
                        }
                    }
                    doc.write(new FileOutputStream(archivoRes));*/
                    break;
                case "PDF_EXCEL":

                    archivoTemp = FilenameUtils.removeExtension(dto.getRuta()) + "_xls.txt";
                    archivoRes = FilenameUtils.removeExtension(dto.getRuta()) + "_xls.xls";
                    //  File fi = new File(archivoTemp);
                    //FileUtils.copyFile(new File(archivo), fi);
                    /*File fcsv = new File(archivoTemp);
                    FileUtils.copyFile(new File(archivo), fcsv);
                    String parentCSV = fcsv.getParent() + "\\";
                    String commandCSV = "\"C:\\Program Files (x86)\\PDFConvert\\pdfconvert.exe\" /cs 9000 /i \"" + archivoTemp.replace("/", "\\") + "\" /o  \"" + parentCSV + "\" /pto 1";
                    System.out.println("command: " + commandCSV);
                    Process pCSV = Runtime.getRuntime().exec(commandCSV);
                    pCSV.waitFor();*/

 /*   InputStream pdfInputStream = new FileInputStream(archivoTemp);
                    ByteArrayOutputStream excelOutputStream = Pdf2Excel.convert(pdfInputStream);

                    try (OutputStream outputStream = new FileOutputStream(archivoRes)) {
                        excelOutputStream.writeTo(outputStream);
                    }
                    excelOutputStream.close();*/
                    try {
                        //Loading the pdf file into PDDocument
                        File MyFile = new File(dto.getRuta());
                        PDDocument MyPDF = PDDocument.load(MyFile);
                        PDFTextStripper TextStripper = new PDFTextStripper();
                        //Fetching the text from the pdf
                        String text = TextStripper.getText(MyPDF);
                        //use FileWriter to open the text file and write the text to it
                        FileWriter textfile = new FileWriter(archivoTemp);
                        //Writing text to the text file
                        textfile.write(text);
                        textfile.close();
                        MyPDF.close();
                        readFileByLine(archivoTemp, archivoRes);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    /*           com.aspose.pdf.Document xlx = new com.aspose.pdf.Document(archivo);
                    archivoTemp = FilenameUtils.removeExtension(dto.getRuta()) + "_temp.xlsx";
                    archivoRes = FilenameUtils.removeExtension(dto.getRuta()) + ".xlsx";
                    ExcelSaveOptions options = new ExcelSaveOptions();
                    options.setFormat(ExcelSaveOptions.ExcelFormat.XLSX);
                    xlx.save(archivoRes, options);

                    try {
                        FileInputStream inputStream = new FileInputStream(archivoTemp);
                        Workbook workbook = WorkbookFactory.create(inputStream);
                        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                            Sheet sheet = workbook.getSheetAt(i);
                            Cell cell2Update = sheet.getRow(0).getCell(1);
                            cell2Update.setCellValue("");
                        }
                        inputStream.close();

                        FileOutputStream outputStream = new FileOutputStream(archivoRes);
                        workbook.write(outputStream);
                        workbook.close();
                        outputStream.close();

                    } catch (IOException | EncryptedDocumentException ex) {
                        ex.printStackTrace();
                    }*/
                    break;
                case "PDF_TXT": ///OK
                    archivoTemp = FilenameUtils.removeExtension(dto.getRuta()) + "_temp.txt";
                    archivoRes = FilenameUtils.removeExtension(dto.getRuta()) + ".txt";
                    /* try {
                        com.lowagie.text.Document docPDF = new com.lowagie.text.Document();
                        docPDF.open();
                        PdfReader reader = new PdfReader(Files.readAllBytes(new File(dto.getRuta()).toPath()));
                        PdfDictionary dictionary = reader.getPageN(1);
                        PRIndirectReference reference = (PRIndirectReference)
                                dictionary.get(PdfName.CONTENTS);
                        PRStream stream = (PRStream) PdfReader.getPdfObject(reference);
                        byte[] bytes = PdfReader.getStreamBytes(stream);
                        PRTokeniser tokenizer = new PRTokeniser(new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(bytes)));
                        FileOutputStream fos = new FileOutputStream(archivoRes);
                        StringBuffer buffer = new StringBuffer();
                        while (tokenizer.nextToken()) {
                            if (tokenizer.getTokenType() == PRTokeniser.TokenType.STRING) {
                                buffer.append(tokenizer.getStringValue());
                            }
                        }
                        String test = buffer.toString();
                        StringReader stReader = new StringReader(test);
                        int t;
                        while ((t = stReader.read()) > 0)
                            fos.write(t);
                        docPDF.add(new Paragraph(".."));
                        docPDF.close();
                        reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                    try {
                        //Loading the pdf file into PDDocument
                        File MyFile = new File(dto.getRuta());
                        PDDocument MyPDF = PDDocument.load(MyFile);
                        PDFTextStripper TextStripper = new PDFTextStripper();
                        //Fetching the text from the pdf
                        String text = TextStripper.getText(MyPDF);
                        //use FileWriter to open the text file and write the text to it
                        FileWriter textfile = new FileWriter(archivoRes);
                        //Writing text to the text file
                        textfile.write(text);
                        textfile.close();
                        MyPDF.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "WORD_PDF": //OK
                    /*     archivoRes = FilenameUtils.removeExtension(dto.getRuta()) + ".pdf";
                    InputStream templateInputStream = new FileInputStream(dto.getRuta());
                    WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(templateInputStream);
                    MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();
                    FileOutputStream os = new FileOutputStream(archivoRes);
                    Docx4J.toPDF(wordMLPackage, os);
                    os.flush();
                    os.close();
                     */

                    archivoTemp = FilenameUtils.removeExtension(dto.getRuta()) + "_doc.docx";
                    archivoRes = FilenameUtils.removeExtension(dto.getRuta()) + ".pdf";
                    File fa = new File(archivoTemp);
                    FileUtils.copyFile(new File(archivo), fa);
                    String parentw = fa.getParent() + "\\";
                    String commandw = "\"C:\\Program Files\\LibreOffice\\program\\soffice\"  --infilter=\"pdf:writer_pdf_Export\" --convert-to pdf --outdir " + parentw + "   " + archivo.replace("/", "\\");
                    System.out.println("command: " + commandw);
                    Process pw = Runtime.getRuntime().exec(commandw);
                    pw.waitFor();

                    break;
                case "EXCEL_PDF": //OK
                    archivoRes = FilenameUtils.removeExtension(dto.getRuta()) + ".pdf";

                    InputStream input = new FileInputStream(archivo);
                    HSSFWorkbook excelBook = new HSSFWorkbook();
                    if (archivo.endsWith("xls")) {//Excel 2003
                        excelBook = new HSSFWorkbook(input);
                    } else if (archivo.endsWith("xlsx")) { //Excel 2007/2010
                        Transform xls = new Transform();
                        XSSFWorkbook workbookOld = new XSSFWorkbook(archivo);
                        xls.transformXSSF(workbookOld, excelBook);
                    }
                    ExcelToHtmlConverter excelToHtmlConverter = new ExcelToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
                    excelToHtmlConverter.setOutputColumnHeaders(false);
                    excelToHtmlConverter.setOutputRowNumbers(false);
                    excelToHtmlConverter.processWorkbook(excelBook);
                    org.w3c.dom.Document w3Document = excelToHtmlConverter.getDocument();

                    ByteArrayOutputStream htmlOutputStream = new ByteArrayOutputStream();
                    Source xmlSource = new DOMSource(w3Document);
                    Result outputTarget = new StreamResult(htmlOutputStream);
                    TransformerFactory.newInstance().newTransformer().transform(xmlSource, outputTarget);
                    InputStream htmlInputStream = new ByteArrayInputStream(htmlOutputStream.toByteArray());

                    ConverterProperties converterProperties = new ConverterProperties();

                    com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(new com.itextpdf.kernel.pdf.PdfWriter(archivoRes));
                    pdfDoc.setDefaultPageSize(PageSize.A4.rotate());

                    HtmlConverter.convertToPdf(htmlInputStream, pdfDoc, converterProperties);

                    break;
                case "TXT_PDF"://OK
                    System.out.println(archivo);
                    archivoRes = FilenameUtils.removeExtension(dto.getRuta()) + ".pdf";

                    try {
                        BufferedReader inputStream = new BufferedReader(new FileReader(archivo));
                        String inLine = null;
                        StringBuffer sb = new StringBuffer("");
                        while ((inLine = inputStream.readLine()) != null) {
                            sb.append(inLine);
                        }
                        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
                        PdfWriter.getInstance(document, new FileOutputStream(archivoRes));
                        document.open();
                        document.add(new com.itextpdf.text.Paragraph(sb.toString()));
                        sb.append(inLine);
                        document.close();
                        inputStream.close();
                        System.out.println("Text is inserted into pdf file");
                    } catch (Exception e) {
                        System.out.println("IOException:");
                        e.printStackTrace();
                    }
                    break;
                case "IMG_TIFF": //OK
                    archivoRes = FilenameUtils.removeExtension(dto.getRuta()) + ".tiff";
                    FileUtils.copyFile(new File(archivo), new File(archivoRes));
                    break;
                case "IMG_BMP"://OK
                    archivoRes = FilenameUtils.removeExtension(dto.getRuta()) + ".bmp";
                    FileUtils.copyFile(new File(archivo), new File(archivoRes));
                    break;
                case "IMG_WMF"://OK
                    archivoRes = FilenameUtils.removeExtension(dto.getRuta()) + ".wmf";
                    FileUtils.copyFile(new File(archivo), new File(archivoRes));
                    break;
                case "IMG_DIB"://OK
                    archivoRes = FilenameUtils.removeExtension(dto.getRuta()) + ".dib";
                    FileUtils.copyFile(new File(archivo), new File(archivoRes));
                    break;
                case "IMG_GIF"://OK
                    archivoRes = FilenameUtils.removeExtension(dto.getRuta()) + ".gif";
                    FileUtils.copyFile(new File(archivo), new File(archivoRes));
                    break;
                case "IMG_JPG"://OK
                    archivoRes = FilenameUtils.removeExtension(dto.getRuta()) + ".jpg";
                    FileUtils.copyFile(new File(archivo), new File(archivoRes));
                    break;
                case "IMG_TIF2"://OK
                    archivoRes = FilenameUtils.removeExtension(dto.getRuta()) + ".tif2";
                    FileUtils.copyFile(new File(archivo), new File(archivoRes));
                    break;
                case "IMG_PCX":
                    archivoRes = FilenameUtils.removeExtension(dto.getRuta()) + ".pcx";
                    FileUtils.copyFile(new File(archivo), new File(archivoRes));
                    break;
                default:
                    dto.setRutaConvertido(archivo);
                    break;
            }
            File f = new File(archivoTemp);
            if (f.exists()) {
                f.delete();
            }
            dto.setRutaConvertido(archivoRes);
            decryptEncrypt.encrypt(archivoRes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            decryptEncrypt.encrypt(dto.getRuta());
        }
        return dto;
    }

    public void merge(final List<File> files, String dest) {

        File outFile = new File(dest);
        try (OutputStream os = new FileOutputStream(outFile); InputStream is = new FileInputStream(files.get(0)); XWPFDocument doc = new XWPFDocument(is);) {

            CTBody ctBody = doc.getDocument().getBody();
            for (int i = 1; i < files.size(); i++) {
                try (InputStream isI = new FileInputStream(files.get(i)); XWPFDocument docI = new XWPFDocument(isI);) {

                    CTBody ctBodyI = docI.getDocument().getBody();
                    appendBody(ctBody, ctBodyI);
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            doc.write(os);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //  return outFile;
    }

    private static void appendBody(CTBody src, CTBody append) {
        XmlOptions optionsOuter = new XmlOptions();
        optionsOuter.setSaveOuter();
        String appendString = append.xmlText(optionsOuter);
        String srcString = src.xmlText();
        String prefix = srcString.substring(0, srcString.indexOf(">") + 1);
        String mainPart = srcString.substring(srcString.indexOf(">") + 1, srcString.lastIndexOf("<"));
        String suffix = srcString.substring(srcString.lastIndexOf("<"));
        String addPart = appendString.substring(appendString.indexOf(">") + 1, appendString.lastIndexOf("<"));
        CTBody makeBody;
        try {
            makeBody = (CTBody) CTBody.Factory.parse(prefix + mainPart + addPart + suffix);
            src.set(makeBody);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void readFileByLine(String filePath, String dest) {
        File file = new File(filePath);
        BufferedReader bd = null;
        Map<String, String> str = new HashMap<String, String>();
        String s1 = "";
        String s2 = "";

        try {
            bd = new BufferedReader(new InputStreamReader(new FileInputStream(file)));//Encoding conversion (key place)

            String temp = "";
            int line = 1;
            while ((temp = bd.readLine()) != null) {
                if (temp.length() > 0) {
                    s1 = temp.substring(0, 1);
                    s1 = s1.trim();
                    s2 = temp;
                    s2 = s2.trim();
                    str.put(s1, s2);
                }
                ++line;
            }
            createExcel(str, dest);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bd != null) {
                    bd.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static void createExcel(Map<String, String> map, String dest) {
        try {
            FileOutputStream fOut = new FileOutputStream(dest);
            File file = new File(dest);
            if (file.exists()) {
                file.delete();
            }
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("Hoja 1");
            HSSFRow row = null;
            HSSFCell cell1 = null;
            HSSFCell cell2 = null;

            Iterator iter = map.entrySet().iterator();
            int i = 0;

            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                row = sheet.createRow((short) i++);
                cell1 = row.createCell((short) 0);
                cell2 = row.createCell((short) 1);
                //Define the cell as a string type
                cell1.setCellType(CellType.STRING);
                cell2.setCellType(CellType.STRING);

                //Enter some content in the cell
                cell1.setCellValue(key.toString());
                cell2.setCellValue(val.toString());

                if (i > 255) {
                    break;
                }
            }

            //Save the corresponding Excel workbook
            workbook.write(fOut);
            fOut.flush();
            //The operation is over, close the file
            fOut.close();
            System.out.println("File generation...");

        } catch (Exception e) {
            System.out.println("An exception occurred: " + e);
        }
    }

    public String convertFileToString(File archivo) {
        String contentType = "";
        Path path = archivo.toPath();
        contentType = path.toString();
        return contentType;
    }

    public byte[] convertFileToBytes(File file) {
        byte[] bytesArray = null;
        try {
            bytesArray = Files.readAllBytes(file.toPath()); // Lee el contenido del archivo en el arreglo de bytes
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytesArray;
    }

    public ArchivoDto consultarArchivoTramite(String tramite) {
        try {
            Archivo archivoBD = archivoRepository.findTopByNumTramiteOrderByFechaCreacionDesc(tramite);
            return archivoMapper.toDto(archivoBD);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

}
