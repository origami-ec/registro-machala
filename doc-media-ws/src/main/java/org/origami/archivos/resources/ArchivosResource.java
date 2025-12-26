package org.origami.archivos.resources;

import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.origami.archivos.config.AppProps;
import org.origami.archivos.model.Imagenes;
import org.origami.archivos.service.DecryptEncrypt;
import org.origami.archivos.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.origami.archivos.model.InfoAnotaciones;

@RestController
@RequestMapping("/origami/api/")
public class ArchivosResource {

    @Autowired
    private AppProps appProp;
    @Autowired
    private DecryptEncrypt decryptEncrypt;
    @Autowired
    private Utils utils;

    @RequestMapping(value = "/resource/image/{ruta}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getImage(@PathVariable String ruta) throws IOException {
        String archivo = utils.replaceRutaArchivo(ruta);
        //System.out.println("archivo: " + archivo);
        decryptEncrypt.decrypt(archivo);
        File initialFile = new File(archivo);
        InputStream targetStream = new FileInputStream(initialFile);
        byte[] bytes = IOUtils.toByteArray(targetStream);
        targetStream.close();
        decryptEncrypt.encrypt(archivo);
        return bytes;
    }

    /**
     * Se medoifica a que solo devuelva una sola pagina por motivos de que
     * cuando es un documento demasiado grande genera errores
     *
     * @param archivo ruta del archivo a firmar
     * @param pagina numero de la pagina que se firma
     * @return lista d imagenes del documento en pdf
     */
    @RequestMapping(value = "/resource/pdfImagenes/{archivo}/pagina/{pagina}", method = RequestMethod.GET)
    public List<Imagenes> pdfToImagen(@PathVariable String archivo, @PathVariable Integer pagina) {

        String pathFile = utils.replaceRutaArchivo(archivo);
        List<Imagenes> files = new ArrayList<>();
        if (pathFile != null) {
            decryptEncrypt.decrypt(pathFile);
            String fileName, tempName;
            BufferedImage bim;

            File file = new File(pathFile);
            String nameFile = "imgtemp_" + file.getName().replace(".pdf", "_");
            fileName = appProp.getImagenes() + file.getName().replace(".pdf", "_");

            String url = appProp.getUrlResource() + "resource/image/" + nameFile;
            try (final PDDocument document = PDDocument.load(file)) {
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                //for (int page = 0; page < document.getNumberOfPages(); ++page) {
                Integer page = pagina - 1;//SE RESTA PORQUE CUANDO SE LEE EMPIEZA DESDE CERO
                System.out.println("page: " + pagina);
                PDPage page1 = document.getPage(page);

                bim = pdfRenderer.renderImageWithDPI(page, 50, ImageType.RGB);
                bim = utils.resize(bim, Float.valueOf(page1.getMediaBox().getWidth()).intValue(), Float.valueOf(page1.getMediaBox().getHeight()).intValue());
                tempName = fileName + page + ".png";
                files.add(new Imagenes("Pagina # " + page, url + page + ".png", tempName));
                ImageIOUtil.writeImage(bim, tempName, 50);
                decryptEncrypt.encrypt(tempName);
                //}
            } catch (IOException e) {
                files.add(new Imagenes("Pagina # 1", appProp.getUrlResource() + "resource/image/sin_resultados.png"));
                e.printStackTrace();
            }
            decryptEncrypt.encrypt(pathFile);
        } else {
            files.add(new Imagenes("Pagina # 1", appProp.getUrlResource() + "resource/image/sin_resultados.png"));
        }
        return files;
    }

    /**
     * Metodos de verificación para archivos
     *
     * @param archivo ruta = ar_Users-OrigamiEC-Documents-grafi.pdf
     * @return Byte[] pdf
     */
    @RequestMapping(value = "resource/pdf/{archivo}/descarga/{descarga}", method = RequestMethod.GET, produces = "application/pdf")
    public ResponseEntity<byte[]> downloadPDFFile(@PathVariable String archivo, @PathVariable String descarga) {
        String filename = utils.replaceRutaArchivo(archivo);
        if (!archivo.contains("MANUAL_DE_USUARIO_VENTANILLA_INTELIGENTE")) {
            decryptEncrypt.decrypt(filename);
        }
        File file = new File(filename);

        byte[] pdfContents = null;
        try {
            String[] nombreArchivo = archivo.split("_");
            StringBuilder archivoDescarga = new StringBuilder();
            for (int i = 2; i < nombreArchivo.length; i++) {
                archivoDescarga.append(" ").append(nombreArchivo[i]);
            }
            System.out.println("path " + file.getPath());
            pdfContents = Files.readAllBytes(file.toPath());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", (descarga.equals("DOWNLOAD") ? "attachment" : "inline") + "; filename=" + archivoDescarga);
            if (!archivo.contains("MANUAL_DE_USUARIO_VENTANILLA_INTELIGENTE")) {
                decryptEncrypt.encrypt(filename);
            }
            return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "resource/docs/{archivo}/descarga/{descarga}", method = RequestMethod.GET, produces = "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
    public ResponseEntity<byte[]> downloadOfficeFile(@PathVariable String archivo, @PathVariable String descarga) {
        String filename = utils.replaceRutaArchivo(archivo);
        decryptEncrypt.decrypt(filename);
        File file = new File(filename);
        byte[] pdfContents = null;
        try {
            String[] nombreArchivo = archivo.split("_");
            StringBuilder archivoDescarga = new StringBuilder();
            for (int i = 2; i < nombreArchivo.length; i++) {
                archivoDescarga.append(" ").append(nombreArchivo[i]);
            }
            System.out.println("path " + file.getPath());
            pdfContents = Files.readAllBytes(file.toPath());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", (descarga.equals("DOWNLOAD") ? "attachment" : "inline") + "; filename=" + archivoDescarga);
            decryptEncrypt.encrypt(filename);
            return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "resource/xlxs/{archivo}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadXlxFile(@PathVariable String archivo) {
        String filename = utils.replaceRutaArchivo(archivo);
        decryptEncrypt.decrypt(filename);
        File file = new File(filename);
        byte[] pdfContents = null;
        try {
            System.out.println("path " + file.getPath());
            pdfContents = Files.readAllBytes(file.toPath());
            HttpHeaders headers = new HttpHeaders();
            System.out.println(file.getName());
            headers.add("Content-Disposition", "attachment" + "; filename=" + file.getName());
            decryptEncrypt.encrypt(filename);
            return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "resource/pdf/{archivo}", method = RequestMethod.GET, produces = "application/pdf")
    public ResponseEntity<byte[]> downloadPDFFile(@PathVariable String archivo) {
        String filename = utils.replaceRutaArchivo(archivo);
        decryptEncrypt.decrypt(filename);
        File file = new File(filename);
        byte[] pdfContents = null;
        try {
            String[] nombreArchivo = archivo.split("_");
            StringBuilder archivoDescarga = new StringBuilder();
            for (int i = 2; i < nombreArchivo.length; i++) {
                archivoDescarga.append(" ").append(nombreArchivo[i]);
            }
            System.out.println("path " + file.getPath());
            pdfContents = Files.readAllBytes(file.toPath());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline" + "; filename=" + archivoDescarga);
            decryptEncrypt.encrypt(filename);
            return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/resource/descargarImage/{ruta}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> descargarImage(@PathVariable String ruta) throws IOException {
        String archivo = utils.replaceRutaArchivo(ruta);
        decryptEncrypt.decrypt(archivo);
        File initialFile = new File(archivo);
        InputStream targetStream = new FileInputStream(initialFile);
        byte[] bytes = IOUtils.toByteArray(targetStream);
        targetStream.close();
        decryptEncrypt.encrypt(archivo);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment" + "; filename=" + initialFile.getName());

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "resource/download/pdf/{nombre}", method = RequestMethod.GET, produces = "application/pdf")
    public ResponseEntity<byte[]> downloadPDFFileV2(@PathVariable String nombre) {
        String filename = utils.addRutaArchivo(nombre);
        decryptEncrypt.decrypt(filename);
        File file = new File(filename);
        byte[] pdfContents;
        try {
            //String[] nombreArchivo = nombre.split("_");
            pdfContents = Files.readAllBytes(file.toPath());
            HttpHeaders headers = new HttpHeaders();
            //headers.add("Content-Disposition", "inline" + "; filename=" + nombreArchivo[nombreArchivo.length - 1]);
            headers.add("Content-Disposition", "inline" + "; filename=" + nombre);
            decryptEncrypt.encrypt(filename);
            return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "resource/download/document/{nombre}", method = RequestMethod.GET, produces = "application/pdf")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable String nombre) {
        String filename = utils.addRutaArchivo(nombre);
        decryptEncrypt.decrypt(filename);
        File file = new File(filename);
        byte[] pdfContents;
        try {
            pdfContents = Files.readAllBytes(file.toPath());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment" + "; filename=" + nombre);
            decryptEncrypt.encrypt(filename);
            return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "pdfArchivo/{nombre}", method = RequestMethod.POST)
    public void getArchivo(@PathVariable String nombre, HttpServletResponse response) {
        String filename = utils.addRutaArchivo(nombre);
        System.out.println("Direccion del archivo" + filename);
        decryptEncrypt.decrypt(filename);
        File file = new File(filename);
        byte[] pdfContents;
        try {
            pdfContents = Files.readAllBytes(file.toPath());
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", "attachment; filename=" + "archivo");
            response.setContentLength(pdfContents.length);
            response.getOutputStream().write(pdfContents);
            response.getOutputStream().flush();
            decryptEncrypt.encrypt(filename);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    @RequestMapping(value = "resource/pdfb64/{archivo}/descarga/{descarga}", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/pdf")
    public ResponseEntity<byte[]> downloadPdfB64(@PathVariable String archivo, @PathVariable String descarga) {

        String filename = Utils.base64ToString(archivo);
        decryptEncrypt.decrypt(filename);
        File file = new File(filename);

        try {
            String archivoDescarga = file.getName();

            System.out.println("path " + file.getPath());
            byte[] pdfContents = Files.readAllBytes(file.toPath());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", (descarga.equals("DOWNLOAD") ? "attachment" : "inline") + "; filename=" + archivoDescarga);
            decryptEncrypt.encrypt(filename);
            return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "libros/archivo/{ruta}", method = RequestMethod.GET, produces = "application/pdf")
    public ResponseEntity<byte[]> getArchivoLibros(@PathVariable String ruta) {
        byte[] pdfContents;
        try {
            ruta = "/" + ruta.replace("-", "/");
            System.out.println("ruta: " + ruta);
            File file = new File(ruta);
            pdfContents = Files.readAllBytes(file.toPath());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline" + "; filename=" + file.getName());
            return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @RequestMapping(value = "anotaciones/{ruta}", method = RequestMethod.GET, produces = "application/json")
    public List<InfoAnotaciones> getInformacionAnotaciones(@PathVariable String ruta) {
        try {
            InfoAnotaciones info;
            List<InfoAnotaciones> resultado = new ArrayList<>();
            ruta = "/" + ruta.replace("-", "/");
            File file = new File(ruta);
            if (file.exists()) {
                try (PDDocument document = PDDocument.load(file)) {
                    int totalPages = document.getNumberOfPages();
                    for (int i = 0; i < totalPages; i++) {
                        List<PDAnnotation> annotations = document.getPage(i).getAnnotations();
                        for (PDAnnotation ann : annotations) {
                            if (ann.getContents() != null) {
                                info = new InfoAnotaciones();
                                info.setPage(i + 1);
                                // Tipo de anotación (Text, Highlight, Underline, etc.)
                                info.setType(ann.getSubtype());
                                // Contenido del comentario
                                info.setContent(ann.getContents());
                                // Autor/Usuario
                                info.setAuthor(ann.getAnnotationName());
                                // Fecha de modificación (string PDF timestamp)
                                if (ann.getModifiedDate() != null) {
                                    info.setModifiedDate(Utils.parsePdfDate(ann.getModifiedDate()));
                                }
                                resultado.add(info);
                            }
                        }
                    }
                }
            }
            return resultado;
        } catch (IOException e) {
            System.out.println(e.toString());
            return new ArrayList<>();
        }
    }

}
