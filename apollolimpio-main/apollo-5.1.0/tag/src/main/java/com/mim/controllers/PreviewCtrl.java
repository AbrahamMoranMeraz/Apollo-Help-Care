/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mim.controllers;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.mim.api.HistorialMedicoapi;
import com.mim.entities.Historialmedico;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import okhttp3.ResponseBody;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import retrofit2.Response;

/**
 *
 * @author marco
 */
@Named("pv")
@SessionScoped
public class PreviewCtrl implements Serializable {

    private String nombre;
    private byte[] buf;
    private Historialmedico obj;

    public PreviewCtrl() {
    }

    public void passData(byte[] buf, String nombre) {
        this.nombre = nombre;
        this.buf = buf;
        ByteArrayInputStream is = new ByteArrayInputStream(buf);
        // this.content= DefaultStreamedContent.builder()
        //                     .stream(() -> is).contentType("application/pdf").name(nombre).build();
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public StreamedContent getContent() {

        if (buf == null) {

            try {
                Document document = new Document(PageSize.LETTER);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PdfWriter writer;
                writer = PdfWriter.getInstance(document, baos);
                if (!document.isOpen()) {
                    document.open();
                }

                document.add(new Paragraph("notFound"));
                document.close(); // no need to close PDFwriter?

                InputStream stream = new ByteArrayInputStream(baos.toByteArray());
                return DefaultStreamedContent.builder()
                        .stream(() -> stream).contentType("application/pdf").name(nombre).build();

            } catch (DocumentException e) {
                e.printStackTrace();
            }

        }

        ByteArrayInputStream is = new ByteArrayInputStream(buf);

        return DefaultStreamedContent.builder()
                .stream(() -> is).contentType("application/pdf").name(nombre).build();
    }

    public Historialmedico getObj() {
        return obj;
    }

    public void setObj(Historialmedico obj) {
        this.obj = obj;
    }

}
