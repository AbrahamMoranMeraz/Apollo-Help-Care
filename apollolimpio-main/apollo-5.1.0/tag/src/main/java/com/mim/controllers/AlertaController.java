/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mim.controllers;

import com.mim.entities.Alerta;
import com.mim.entities.Paciente;
import com.mim.entities.Usuario;
import com.mim.sesion.AlertaFacade;
import com.mim.sesion.PacienteFacade;
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
import java.util.ArrayList;
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
@Named("alertaCtrl")
@ViewScoped
public class AlertaController implements Serializable {

    @Inject
    private LoginController loginCtrl;
    @EJB
    private PacienteFacade pacFacade;
    @EJB
    private AlertaFacade alerFacade;
    private List<Alerta> alertas;
    private List<ResponsiveOption> responsiveOptions;
    private Alerta selectedAlerta;

    @PostConstruct
    public void init() {
        inicializarLista();
        responsiveOptions = new ArrayList<>();
        responsiveOptions.add(new ResponsiveOption("1024px", 3, 3));
        responsiveOptions.add(new ResponsiveOption("768px", 2, 2));
        responsiveOptions.add(new ResponsiveOption("400px", 1, 1));
    }

    public void inicializarLista() {
        this.alertas = this.alerFacade.findByEntidadMedicaOpen(loginCtrl.getEntidadMedica().getIdentidadmedica());
    }

    public void webSocket() {
        System.out.println("webSocket() ");
        inicializarLista();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("New changes"));
        PrimeFaces.current().ajax().update(":form");
    }

    public List<Alerta> getAlertas() {
        return alertas;
    }

    public void setAlertas(List<Alerta> alertas) {
        this.alertas = alertas;
    }

    public Alerta getSelectedAlerta() {
        return selectedAlerta;
    }

    public void setSelectedAlerta(Alerta selectedAlerta) {
        this.selectedAlerta = selectedAlerta;
    }

}
