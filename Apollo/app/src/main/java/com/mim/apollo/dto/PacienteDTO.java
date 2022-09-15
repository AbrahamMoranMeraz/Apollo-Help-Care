/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mim.apollo.dto;


import java.io.Serializable;

/**
 *
 * @author robb
 */
public class PacienteDTO implements Serializable {
    
    private Integer idpaciente;
    private String nombre;
    private String ubicacion;
    private String telefono;
    private String foto;
    private Integer edad;
    private EntidadmedicaDTO entidadmedicaIdentidadmedica;
    private UsuarioDTO usuarioIdusuario;
    

    
    public PacienteDTO() {
    }
    
    public Integer getIdpaciente() {
        return idpaciente;
    }
    
    public void setIdpaciente(Integer idpaciente) {
        this.idpaciente = idpaciente;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getUbicacion() {
        return ubicacion;
    }
    
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getFoto() {
        return foto;
    }
    
    public void setFoto(String foto) {
        this.foto = foto;
    }
    
    public Integer getEdad() {
        return edad;
    }
    
    public void setEdad(Integer edad) {
        this.edad = edad;
    }
    
    public EntidadmedicaDTO getEntidadmedicaIdentidadmedica() {
        return entidadmedicaIdentidadmedica;
    }
    
    public void setEntidadmedicaIdentidadmedica(EntidadmedicaDTO entidadmedicaIdentidadmedica) {
        this.entidadmedicaIdentidadmedica = entidadmedicaIdentidadmedica;
    }
    
    public UsuarioDTO getUsuarioIdusuario() {
        return usuarioIdusuario;
    }
    
    public void setUsuarioIdusuario(UsuarioDTO usuarioIdusuario) {
        this.usuarioIdusuario = usuarioIdusuario;
    }
    
}
