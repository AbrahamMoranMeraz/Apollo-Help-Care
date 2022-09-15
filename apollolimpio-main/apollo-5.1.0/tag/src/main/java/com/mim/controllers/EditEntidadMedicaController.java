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
@Named("editEntMedicaCtrl")
@ViewScoped
public class EditEntidadMedicaController implements Serializable {

    @EJB
    private EntidadMedicaFacade entMedFacade;
    @EJB
    private UsuarioFacade usuarioFacade;
    @Inject
    private LoginController loginCtrl;
    private Usuario selectedUsuario;
    private Entidadmedica selectedEntidadMedica;
    private String icono;
    private InputStream is;

    @PostConstruct
    public void init() {
        selectedEntidadMedica = loginCtrl.getEntidadMedica();
        selectedUsuario=selectedEntidadMedica.getUsuarioIdusuario();
    }

    public void save() throws IOException {
        this.usuarioFacade.edit(selectedUsuario);
        this.entMedFacade.edit(selectedEntidadMedica);
        init();
        FacesMessage msg = new FacesMessage("Successful", "Edit :" + selectedEntidadMedica.getNombre());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        PrimeFaces.current().ajax().update(":form");
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
            Logger.getLogger(EditEntidadMedicaController.class
                    .getName()).log(Level.SEVERE, (String) null, var15);
        } finally {
            try {
                os.close();

            } catch (IOException var14) {
                Logger.getLogger(EditEntidadMedicaController.class
                        .getName()).log(Level.SEVERE, (String) null, var14);
            }
        }
    }

    public Entidadmedica getSelectedEntidadMedica() {
        return selectedEntidadMedica;
    }

    public void setSelectedEntidadMedica(Entidadmedica selectedEntidadMedica) {
        this.selectedEntidadMedica = selectedEntidadMedica;
    }

    public Usuario getSelectedUsuario() {
        return selectedUsuario;
    }

    public void setSelectedUsuario(Usuario selectedUsuario) {
        this.selectedUsuario = selectedUsuario;
    }

 
}
