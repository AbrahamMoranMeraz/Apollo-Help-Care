/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mim.dto;

import java.io.Serializable;

/**
 *
 * @author DAMMA
 */
public class AudioMensajeDTO implements Serializable {
    
    private Integer idAudioMensaje;
    private String nombre;
    private Integer idAlerta;

    public AudioMensajeDTO(Integer idAudioMensaje, String nombre, Integer idAlerta) {
        this.idAudioMensaje = idAudioMensaje;
        this.nombre = nombre;
        this.idAlerta = idAlerta;
    }

    
    public Integer getIdAudioMensaje() {
        return idAudioMensaje;
    }

    public void setIdAudioMensaje(Integer idAudioMensaje) {
        this.idAudioMensaje = idAudioMensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getIdAlerta() {
        return idAlerta;
    }

    public void setIdAlerta(Integer idAlerta) {
        this.idAlerta = idAlerta;
    }
    
    
}
