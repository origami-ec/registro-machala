package org.origami.docs.util;

import org.apache.pdfbox.io.IOUtils;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.contenttype.ContentType;
import org.docx4j.openpackaging.io.SaveToZipFile;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.WordprocessingML.AlternativeFormatInputPart;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.CTAltChunk;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class MergeDocx {
    private static long chunk = 0;
    private static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

    public void mergeDocx(List<File> files, OutputStream os) throws Exception {

        InputStream s1 = new FileInputStream(files.get(0));
        files.remove(0);
        WordprocessingMLPackage target = WordprocessingMLPackage.load(s1);
        for (File f : files){
            InputStream is = new FileInputStream(f);
            insertDocx(target.getMainDocumentPart(), IOUtils.toByteArray(is));
            //target = WordprocessingMLPackage.load(s1);
        }
        SaveToZipFile saver = new SaveToZipFile(target);
        saver.save(os);
    }

    private static void insertDocx(MainDocumentPart main, byte[] bytes) throws Exception {
        AlternativeFormatInputPart afiPart = new AlternativeFormatInputPart(new PartName("/part" + (chunk++) + ".docx"));
        afiPart.setContentType(new ContentType(CONTENT_TYPE));
        afiPart.setBinaryData(bytes);
        Relationship altChunkRel = main.addTargetPart(afiPart);

        CTAltChunk chunk = Context.getWmlObjectFactory().createCTAltChunk();
        chunk.setId(altChunkRel.getId());

        main.addObject(chunk);
    }
}