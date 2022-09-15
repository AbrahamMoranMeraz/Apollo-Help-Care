/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mim.controllers;

import com.mim.entities.Entidadmedica;
import com.mim.entities.Paciente;
import com.mim.entities.Usuario;
import com.mim.sesion.EntidadMedicaFacade;
import com.mim.sesion.PacienteFacade;
import com.mim.sesion.UsuarioFacade;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.util.SerializableSupplier;

/**
 *
 * @author DAMMA
 */
@Named("entMedicaCtrl")
@ViewScoped
public class EntidadMedicaController implements Serializable {

    @EJB
    private UsuarioFacade usuFacade;
    @EJB
    private EntidadMedicaFacade entMedFacade;
    @Inject
    private LoginController loginCtrl;
    private Usuario selectedUsuario;
    private Entidadmedica selectedEntidadMedica;
    private String icono;
    private InputStream is;
    private Integer steps = 0;

    @PostConstruct
    public void init() {
        selectedUsuario = new Usuario();
        selectedEntidadMedica = new Entidadmedica();
    }

    public void save() throws IOException {
        usuFacade.create(selectedUsuario);
        selectedEntidadMedica.setUsuarioIdusuario(usuFacade.find(selectedUsuario.getCorreo(), selectedUsuario.getContrasena()));
        this.entMedFacade.create(selectedEntidadMedica);
        loginCtrl.setSelectedUsuario(selectedUsuario);
        loginCtrl.obtenerEntidadMedica(selectedUsuario);
        FacesMessage msg = new FacesMessage("Successful", "Welcome :" + selectedUsuario.getNombre());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        FacesContext.getCurrentInstance().getExternalContext().redirect("dashboard.xhtml");
    }

    public String onBack() {
        System.out.println("onBack()");
        steps = steps - 1;
        PrimeFaces.current().ajax().update(":form:step");
        return "";
    }

    public String onFlowProcess(FlowEvent event) {
        steps = steps + 1;
        PrimeFaces.current().ajax().update(":form:step");
        return event.getNewStep();
    }

    public StreamedContent getImage() {
        if (is != null) {
            return DefaultStreamedContent.builder()
                    .contentType("png")
                    .name(selectedEntidadMedica.getIcono())
                    .stream((SerializableSupplier<InputStream>) is)
                    .build();
        } else {
            return null;
        }
    }

    public void uploadFile(FileUploadEvent event) throws IOException {
        // this.proccessStream(event.getFile().getInputStream(), event.getFile().getFileName());
        this.selectedEntidadMedica.setIcono(event.getFile().getFileName());
        is = event.getFile().getInputStream();
        System.out.println("Icono " + selectedEntidadMedica.getIcono());
        PrimeFaces.current().ajax().update(":form:panelImage");
    }

    private void proccessStream(InputStream stream, String nombre) {
        FileOutputStream os = null;

        try {
            File fileToUpload = null;
            fileToUpload = new File("/opt/wildfly/hackaton/" + nombre);
            os = new FileOutputStream(fileToUpload);
            byte[] b = new byte[2048];

            int length;
            while ((length = stream.read(b)) != -1) {
                os.write(b, 0, length);

            }
        } catch (IOException var15) {
            Logger.getLogger(EntidadMedicaController.class
                    .getName()).log(Level.SEVERE, (String) null, var15);
        } finally {
            try {
                os.close();

            } catch (IOException var14) {
                Logger.getLogger(EntidadMedicaController.class
                        .getName()).log(Level.SEVERE, (String) null, var14);
            }
        }
    }

    public Usuario getSelectedUsuario() {
        return selectedUsuario;
    }

    public void setSelectedUsuario(Usuario selectedUsuario) {
        this.selectedUsuario = selectedUsuario;
    }

    public Entidadmedica getSelectedEntidadMedica() {
        return selectedEntidadMedica;
    }

    public void setSelectedEntidadMedica(Entidadmedica selectedEntidadMedica) {
        this.selectedEntidadMedica = selectedEntidadMedica;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public InputStream getIs() {
        return is;
    }

    public void setIs(InputStream is) {
        this.is = is;
    }

    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

}
