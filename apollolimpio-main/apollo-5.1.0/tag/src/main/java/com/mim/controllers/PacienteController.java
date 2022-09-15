/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mim.controllers;

import com.mim.entities.Contacto;
import com.mim.entities.Paciente;
import com.mim.entities.Tipocontacto;
import com.mim.entities.Usuario;
import com.mim.sesion.ContactoFacade;
import com.mim.sesion.PacienteFacade;
import com.mim.sesion.TipoContactoFacade;
import com.mim.sesion.UsuarioFacade;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.util.SerializableSupplier;

/**
 *
 * @author DAMMA
 */
@Named("pacienteCtrl")
@ViewScoped
public class PacienteController implements Serializable {

    @Inject
    private LoginController loginCtrl;
    @EJB
    private PacienteFacade pacFacade;
    @EJB
    private ContactoFacade contactoFacade;
    @EJB
    private TipoContactoFacade tipocontactoFacade;
    @EJB
    private UsuarioFacade usuFacade;
    private List<Paciente> pacientes;
    private List<Paciente> selectedPacientes;
    private List<Tipocontacto> tipoContactos;
    private List<Contacto> contactos;
    private Contacto selectedContacto;
    private Paciente selectedPaciente;
    private InputStream is;

    @PostConstruct
    public void init() {
        this.pacientes = this.pacFacade.findByEntidadMedica(loginCtrl.getEntidadMedica().getIdentidadmedica());
        tipoContactos = tipocontactoFacade.findAll();
    }

    public void openNew() {
        this.selectedPaciente = new Paciente();
        selectedPaciente.setUsuarioIdusuario(new Usuario());
    }

    public void saveProduct() {
        if (this.selectedPaciente.getIdpaciente() == null) {
            selectedPaciente.getUsuarioIdusuario().setNombre(selectedPaciente.getNombre());
            this.usuFacade.create(selectedPaciente.getUsuarioIdusuario());
            Usuario usuario = usuFacade.find(selectedPaciente.getUsuarioIdusuario().getCorreo(), selectedPaciente.getUsuarioIdusuario().getContrasena());
            this.selectedPaciente.setEntidadmedicaIdentidadmedica(loginCtrl.getEntidadMedica());
            this.selectedPaciente.setUsuarioIdusuario(usuario);
            this.pacFacade.create(selectedPaciente);
            this.pacientes.add(this.selectedPaciente);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Pacient Added"));
        } else {
            selectedPaciente.getUsuarioIdusuario().setNombre(selectedPaciente.getNombre());
            this.usuFacade.edit(selectedPaciente.getUsuarioIdusuario());
            this.pacFacade.edit(selectedPaciente);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Pacient Updated"));
        }

        PrimeFaces.current().executeScript("PF('manageProductDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-products");
    }

    public void onContacto(Paciente paciente) {
        selectedPaciente = paciente;
        this.contactos = this.contactoFacade.findByPaciente(this.selectedPaciente.getIdpaciente());
        selectedContacto = new Contacto();
    }

    public void saveContact() {
        if (selectedContacto.getNombre().isEmpty() || selectedContacto.getTelefono().isEmpty() || selectedContacto.getTipocontactoIdtipocontacto() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Fill in all fields to create contact"));
            PrimeFaces.current().ajax().update("form:messages");
        } else {
            selectedContacto.setPacienteIdpaciente(selectedPaciente);
            this.contactoFacade.create(selectedContacto);
            onContacto(selectedPaciente);
            PrimeFaces.current().ajax().update("dialogs:manage-contacts");
        }
    }

    public StreamedContent getImage() {
        if (is != null) {
            return DefaultStreamedContent.builder()
                    .contentType("png")
                    .name(selectedPaciente.getFoto())
                    .stream((SerializableSupplier<InputStream>) is)
                    .build();
        } else {
            return null;
        }
    }

    public void uploadFile(FileUploadEvent event) throws IOException {
        // this.proccessStream(event.getFile().getInputStream(), event.getFile().getFileName());
        this.selectedPaciente.setFoto(event.getFile().getFileName());
        is = event.getFile().getInputStream();
        System.out.println("Icono " + selectedPaciente.getFoto());
        PrimeFaces.current().ajax().update(":dialogs:panelImage");
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

    public void deleteProduct() {
        this.pacFacade.remove(selectedPaciente);
        this.usuFacade.remove(selectedPaciente.getUsuarioIdusuario());
        this.pacientes = this.pacFacade.findByEntidadMedica(loginCtrl.getEntidadMedica().getIdentidadmedica());
        this.selectedPaciente = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Pacient Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-products");
    }

    public String getDeleteButtonMessage() {
        if (hasSelectedProducts()) {
            int size = this.selectedPacientes.size();
            return size > 1 ? size + " pacients selected" : "1 pacient selected";
        }

        return "Delete";
    }

    public boolean hasSelectedProducts() {
        return this.selectedPacientes != null && !this.selectedPacientes.isEmpty();
    }

    public void deleteSelectedProducts() {
        for (Paciente p : selectedPacientes) {
            this.pacFacade.remove(p);
            this.usuFacade.remove(p.getUsuarioIdusuario());
        }
        this.pacientes = this.pacFacade.findByEntidadMedica(loginCtrl.getEntidadMedica().getIdentidadmedica());
        this.selectedPacientes = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Pacients Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-products");
        PrimeFaces.current().executeScript("PF('dtProducts').clearFilters()");
    }

    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public void setPacientes(List<Paciente> pacientes) {
        this.pacientes = pacientes;
    }

    public Paciente getSelectedPaciente() {
        return selectedPaciente;
    }

    public void setSelectedPaciente(Paciente selectedPaciente) {
        this.selectedPaciente = selectedPaciente;
    }

    public List<Paciente> getSelectedPacientes() {
        return selectedPacientes;
    }

    public void setSelectedPacientes(List<Paciente> selectedPacientes) {
        this.selectedPacientes = selectedPacientes;
    }

    public InputStream getIs() {
        return is;
    }

    public void setIs(InputStream is) {
        this.is = is;
    }

    public List<Contacto> getContactos() {
        return contactos;
    }

    public void setContactos(List<Contacto> contactos) {
        this.contactos = contactos;
    }

    public Contacto getSelectedContacto() {
        return selectedContacto;
    }

    public void setSelectedContacto(Contacto selectedContacto) {
        this.selectedContacto = selectedContacto;
    }

    public List<Tipocontacto> getTipoContactos() {
        return tipoContactos;
    }

    public void setTipoContactos(List<Tipocontacto> tipoContactos) {
        this.tipoContactos = tipoContactos;
    }

}
