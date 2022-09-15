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
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.util.SerializableSupplier;

/**
 *
 * @author DAMMA
 */
@Named("alertasCtrl")
@ViewScoped
public class AlertasController implements Serializable {

    @Inject
    private LoginController loginCtrl;
    @EJB
    private PacienteFacade pacFacade;
    @EJB
    private AlertaFacade alerFacade;

    private List<Alerta> alertas;
    private MapModel emptyModel;

    private Alerta selectedAlerta;
    private Integer id;
    private String coordenadas;

    @PostConstruct
    public void init() {
        alertas = this.alerFacade.findAllOrderByDate(loginCtrl.getEntidadMedica().getIdentidadmedica());
    }

    public void onAlerta(Alerta alerta) {
        selectedAlerta=alerta;
        coordenadas = this.selectedAlerta.getLat() + ", " + this.selectedAlerta.getLng();
        emptyModel = new DefaultMapModel();
        emptyModel.addOverlay(new Marker(new LatLng(Double.valueOf(selectedAlerta.getLat()), Double.valueOf(selectedAlerta.getLng())), "location"));
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MapModel getEmptyModel() {
        return emptyModel;
    }

    public void setEmptyModel(MapModel emptyModel) {
        this.emptyModel = emptyModel;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

}
