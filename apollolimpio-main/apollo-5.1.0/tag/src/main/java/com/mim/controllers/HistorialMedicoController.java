/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mim.controllers;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.mim.api.HistorialMedicoapi;
import com.mim.entities.Historialmedico;
import com.mim.entities.Paciente;
import com.mim.sesion.HistorialMedicoFacade;
import com.mim.sesion.PacienteFacade;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.ResponseBody;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import retrofit2.Response;

/**
 *
 * @author DAMMA
 */
@Named("historialMedicoCtrl")
@ViewScoped
public class HistorialMedicoController implements Serializable {

    @Inject
    private PreviewCtrl prev;
    @Inject
    private LoginController loginCtrl;
    @EJB
    private PacienteFacade pacFacade;
    @EJB
    private HistorialMedicoFacade historialFacade;

    private List<Paciente> pacientes;
    private List<Historialmedico> historialmedico;
    private UploadedFile file;

    private Paciente selectedPaciente;
    private Historialmedico selectedHistorial;
    private String nombre;
    private byte[] buf;

    @PostConstruct
    public void init() {
        pacientes = this.pacFacade.findByEntidadMedica(loginCtrl.getEntidadMedica().getIdentidadmedica());
    }

    public void onPaciente(Paciente obj) {
        selectedPaciente = obj;
        selectedHistorial = new Historialmedico();
        historialmedico = historialFacade.findByPaciente(selectedPaciente.getIdpaciente());
    }

    public void save() {
        this.selectedHistorial.setPacienteIdpaciente(selectedPaciente);
        this.historialFacade.create(selectedHistorial);
        onPaciente(selectedPaciente);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Added"));
        PrimeFaces.current().ajax().update(":dialogs:manage-contacts");
    }

    public void upload(FileUploadEvent event) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        if (event.getFile() != null) {
            String nombre = "archivo" + ThreadLocalRandom.current().nextInt() + ".pdf";
            this.selectedHistorial.setArchivo(nombre);
            try {
                this.proccessStream(event.getFile().getInputStream(), nombre, "application/pdf");
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(HistorialMedicoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void proccessStream(InputStream stream, String nombre, String mediaType) throws KeyManagementException, NoSuchAlgorithmException {
        FileOutputStream os = null;
        File fileToUpload = null;
        try {
            //File fileToUpload = new File("/opt/imagenes/certi/" + nombre);
            fileToUpload = new File("/opt/wildfly/hackaton/pdfs/" + nombre);

            os = new FileOutputStream(fileToUpload);
            byte[] b = new byte[2048];

            int length;
            while ((length = stream.read(b)) != -1) {
                os.write(b, 0, length);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("File upload"));
            PrimeFaces.current().ajax().update(":form:messages ");

        } catch (IOException var15) {
            FacesMessage growl = new FacesMessage("Error", "Error al subir archivo");
            FacesContext.getCurrentInstance().addMessage(null, growl);
            Logger.getLogger(HistorialMedicoController.class.getName()).log(Level.SEVERE, (String) null, var15);
        } finally {
            try {
                if (os != null) {
                    os.close();
                    //if (sendToStorageServer(fileToUpload, nombre, mediaType)) {// CODE ADDED
                    //}
                }
            } catch (IOException var14) {
                Logger.getLogger(HistorialMedicoController.class.getName()).log(Level.SEVERE, (String) null, var14);
            }

        }

    }

    public void preview(Historialmedico map) {
        System.out.println("preview() entidad:"+map);
        try {
            byte[] array = null;
            Response<ResponseBody> resp = HistorialMedicoapi.Factory.getInstance().document(map.getIdhistorialmedico()).execute();
            if (resp.isSuccessful()) {
                array = resp.body().bytes();
                System.out.println("exitoso paso a passData");
                this.prev.passData(array, map.getNombre() + ".pdf");
                this.prev.setObj(map);
                PrimeFaces.current().ajax().update(":dialogs:dialog");
                PrimeFaces.current().executeScript("PF('pdfDialog').show()");
            } else {
                //System.out.println("Fallo el servicio......");
            }

        } catch (Exception var4) {
            var4.printStackTrace();
        }
        PrimeFaces.current().executeScript("PF('pdfDialog').show()");
    }

    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public void setPacientes(List<Paciente> pacientes) {
        this.pacientes = pacientes;
    }

    public List<Historialmedico> getHistorialmedico() {
        return historialmedico;
    }

    public void setHistorialmedico(List<Historialmedico> historialmedico) {
        this.historialmedico = historialmedico;
    }

    public Paciente getSelectedPaciente() {
        return selectedPaciente;
    }

    public void setSelectedPaciente(Paciente selectedPaciente) {
        this.selectedPaciente = selectedPaciente;
    }

    public Historialmedico getSelectedHistorial() {
        return selectedHistorial;
    }

    public void setSelectedHistorial(Historialmedico selectedHistorial) {
        this.selectedHistorial = selectedHistorial;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

}
