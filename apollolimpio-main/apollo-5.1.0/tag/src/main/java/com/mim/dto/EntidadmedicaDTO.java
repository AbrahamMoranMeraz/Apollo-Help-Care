/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mim.dto;


import com.mim.entities.Entidadmedica;
import java.io.Serializable;

/**
 *
 * @author robb
 */
public class EntidadmedicaDTO implements Serializable {

   

    private Integer identidadmedica;
    private String nombre;
    private String telefono;
    private String icono;
    private String ubicacion;
    private UsuarioDTO usuarioIdusuario;
    
     public static EntidadmedicaDTO convert(Entidadmedica entidad) {
         EntidadmedicaDTO res=new EntidadmedicaDTO();
         res.setIcono(entidad.getIcono());
         res.setIdentidadmedica(entidad.getIdentidadmedica());
         res.setNombre(entidad.getNombre());
         res.setTelefono(entidad.getTelefono());
         res.setUbicacion(entidad.getUbicacion());
         res.setUsuarioIdusuario(UsuarioDTO.convert(entidad.getUsuarioIdusuario()));
         
        return res;
    }

    public EntidadmedicaDTO() {
    }

    public Integer getIdentidadmedica() {
        return identidadmedica;
    }

    public void setIdentidadmedica(Integer identidadmedica) {
        this.identidadmedica = identidadmedica;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public UsuarioDTO getUsuarioIdusuario() {
        return usuarioIdusuario;
    }

    public void setUsuarioIdusuario(UsuarioDTO usuarioIdusuario) {
        this.usuarioIdusuario = usuarioIdusuario;
    }

}
