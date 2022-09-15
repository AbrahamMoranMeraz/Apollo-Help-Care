/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mim.dto;


import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author robb
 */
public class AlertaDTO implements Serializable {

    private Integer idalerta;
    private Date fecha;
    private String estatus;
    private PacienteDTO pacienteIdpaciente;
    private TipoalertaDTO tipoalertaIdtipoalerta;
    private String lat;
    private String lng;

    public AlertaDTO() {
    }

    public Integer getIdalerta() {
        return idalerta;
    }

    public void setIdalerta(Integer idalerta) {
        this.idalerta = idalerta;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public PacienteDTO getPacienteIdpaciente() {
        return pacienteIdpaciente;
    }

    public void setPacienteIdpaciente(PacienteDTO pacienteIdpaciente) {
        this.pacienteIdpaciente = pacienteIdpaciente;
    }

    public TipoalertaDTO getTipoalertaIdtipoalerta() {
        return tipoalertaIdtipoalerta;
    }

    public void setTipoalertaIdtipoalerta(TipoalertaDTO tipoalertaIdtipoalerta) {
        this.tipoalertaIdtipoalerta = tipoalertaIdtipoalerta;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
