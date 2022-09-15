/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mim.controllers;

import com.mim.api.AlertaAPI;
import com.mim.api.HistorialMedicoapi;
import com.mim.dto.AudioMensajeDTO;
import com.mim.entities.Alerta;
import com.mim.entities.Audiomensaje;
import com.mim.entities.Historialmedico;
import com.mim.entities.Paciente;
import com.mim.entities.Usuario;
import com.mim.sesion.AlertaFacade;
import com.mim.sesion.AudioMensajeFacade;
import com.mim.sesion.HistorialMedicoFacade;
import com.mim.sesion.PacienteFacade;
import com.mim.sesion.UsuarioFacade;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.push.Push;
import jakarta.faces.push.PushContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.ResponseBody;
import org.primefaces.PrimeFaces;
import org.primefaces.component.audio.AudioType;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.util.SerializableSupplier;
import retrofit2.Response;

/**
 *
 * @author DAMMA
 */
@Named("alertaDetailsCtrl")
@ViewScoped
public class AlertaDetailsController implements Serializable {

    @Inject
    @Push
    private PushContext channelName;
    @Inject
    private PreviewCtrl prev;
    @Inject
    private LoginController loginCtrl;
    @EJB
    private PacienteFacade pacFacade;
    @EJB
    private AlertaFacade alerFacade;
    @EJB
    private AudioMensajeFacade audioMenFacade;
    @EJB
    private HistorialMedicoFacade historialFacade;
    private List<Historialmedico> historial;
    private List<Alerta> alertas;
    private List<Audiomensaje> listaAudioMensaje;
    private MapModel emptyModel;

    private StreamedContent media;
    private Alerta selectedAlerta;
    private Integer id;
    private String coordenadas;
    private String code = "";

    @PostConstruct
    public void init() {

    }

    public void inicializar() throws KeyManagementException, NoSuchAlgorithmException, IOException {

        this.selectedAlerta = this.alerFacade.find(id);
        if (selectedAlerta.getEstatus().equals("Open")) {
            selectedAlerta.setEstatus("Aware");
            this.alerFacade.edit(selectedAlerta);
            channelName.send("Holaaaaaaaaaaaa");
        }
        historial = historialFacade.findByPaciente(this.selectedAlerta.getPacienteIdpaciente().getIdpaciente());
        System.out.println("Coordenadas: " + selectedAlerta.getLat() + "," + selectedAlerta.getLng());
        coordenadas = selectedAlerta.getLat() + ", " + selectedAlerta.getLng();
        emptyModel = new DefaultMapModel();
        emptyModel.addOverlay(new Marker(new LatLng(Double.valueOf(selectedAlerta.getLat()), Double.valueOf(selectedAlerta.getLng())), "location"));
        this.listaAudioMensaje = audioMenFacade.findByAlerta(id);
    }

    public StreamedContent getMedia() {
        return media;
    }

    public void preview(Historialmedico map) {
        System.out.println("preview() entidad:" + map);
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

    public void check() {
        if (code.equals(this.selectedAlerta.getCodigo())) {
            this.selectedAlerta.setEstatus("Close");
            this.alerFacade.edit(selectedAlerta);
            channelName.send("Holaaaaaaaaaaaa");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Alert has been closed"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid code"));
        }
        PrimeFaces.current().ajax().update(":form:messages ");
    }

    public List<Historialmedico> getHistorial() {
        return historial;
    }

    public void setHistorial(List<Historialmedico> historial) {
        this.historial = historial;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Audiomensaje> getListaAudioMensaje() {
        return listaAudioMensaje;
    }

    public void setListaAudioMensaje(List<Audiomensaje> listaAudioMensaje) {
        this.listaAudioMensaje = listaAudioMensaje;
    }

}
