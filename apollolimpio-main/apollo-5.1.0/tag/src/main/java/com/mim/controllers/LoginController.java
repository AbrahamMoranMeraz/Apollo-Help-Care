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
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import org.primefaces.PrimeFaces;

/**
 *
 * @author DAMMA
 */
@Named("loginCtrl")
@SessionScoped
public class LoginController implements Serializable {

    @EJB
    private UsuarioFacade usuFacade;
    @EJB
    private EntidadMedicaFacade entMedFacade;
    private Entidadmedica entidadMedica;
    private Usuario selectedUsuario;
    private String correo;
    private String contrasena;

    @PostConstruct
    public void init() {
        selectedUsuario = null;
    }

    public void obtenerEntidadMedica(Usuario usuario) {
        this.entidadMedica = this.entMedFacade.findByUsuario(selectedUsuario.getIdusuario());
    }

    public void login() throws IOException {
        if (correo != null && contrasena != null) {
            selectedUsuario = usuFacade.find(correo, contrasena);
            System.out.println("Correo: " + correo + "Password: " + contrasena + "usuario: " + selectedUsuario);
            if (selectedUsuario != null) {
                obtenerEntidadMedica(selectedUsuario);
                FacesContext.getCurrentInstance().getExternalContext().redirect("dashboard.xhtml");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Wrong credentials"));
                PrimeFaces.current().ajax().update(":formLogin:messages");
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Please fill in all the fields"));
            PrimeFaces.current().ajax().update(":formLogin:messages");
        }
    }

    public void logout() {
        this.selectedUsuario = null;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }

    public void renderListener() throws IOException {
        if (this.selectedUsuario == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
        }
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Usuario getSelectedUsuario() {
        return selectedUsuario;
    }

    public void setSelectedUsuario(Usuario selectedUsuario) {
        this.selectedUsuario = selectedUsuario;
    }

    public Entidadmedica getEntidadMedica() {
        return entidadMedica;
    }

    public void setEntidadMedica(Entidadmedica entidadMedica) {
        this.entidadMedica = entidadMedica;
    }

}
