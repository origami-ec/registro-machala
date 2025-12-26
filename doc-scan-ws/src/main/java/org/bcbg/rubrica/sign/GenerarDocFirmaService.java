package org.bcbg.rubrica.sign;

import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.bcbg.model.Imagenes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GenerarDocFirmaService {

    @Value("${app.archivosPCLocal}")
    private String archivosPCLocal;
    @Value("${app.imagenesPCLocal}")
    private String imagenesPCLocal;
    @Value("${app.imagenDoc}")
    private String imagenDoc;
    private String fecha;

    public List<Imagenes> pdfToImagen(String archivo, String pagina) {

        List<Imagenes> files = new ArrayList<>();

        String fileName, tempName;
        BufferedImage bim;
        //archivo = archivo.replace("\\", File.separator);
        //System.out.println(archivo);
        File file = new File(archivo.trim());
        String fecha =  String.valueOf(new Date().getTime());
        String nameFile =fecha + "_" + file.getName().replace(".pdf", "_");
        fileName = imagenesPCLocal + fecha + "_" + file.getName().replace(".pdf", "_");

        String url = imagenDoc + nameFile;
        try (final PDDocument document = PDDocument.load(file)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            Integer page = Integer.parseInt(pagina) - 1;//SE RESTA PORQUE CUANDO SE LEE EMPIEZA DESDE CERO
            System.out.println("page: " + pagina);
            PDPage page1 = document.getPage(page);

            bim = pdfRenderer.renderImageWithDPI(page, 100, ImageType.RGB);
            bim = resize(bim, Float.valueOf(page1.getMediaBox().getWidth()).intValue(), Float.valueOf(page1.getMediaBox().getHeight()).intValue());
            tempName = fileName + page + ".png";
            files.add(new Imagenes("Pagina # " + page, url + page + ".png", tempName));
            ImageIOUtil.writeImage(bim, tempName, 100);
            return files;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] docImagen(String archivo) {
        try {
            archivo = imagenesPCLocal + archivo;
            System.out.println(archivo);
            InputStream targetStream = null;
            targetStream = new FileInputStream(archivo);
            return IOUtils.toByteArray(targetStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }





}
