/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mim.dto;


import com.mim.entities.Usuario;
import java.io.Serializable;

/**
 *
 * @author robb
 */
public class UsuarioDTO implements Serializable {

    private Integer idusuario;
    private String nombre;
    private String correo;
    private String contrasena;


     public static UsuarioDTO convert(Usuario user){
        UsuarioDTO res=new UsuarioDTO();
        res.setContrasena(user.getContrasena());
        res.setCorreo(user.getContrasena());
        res.setNombre(user.getNombre());
        res.setIdusuario(user.getIdusuario());
        
        return null;
    }

    public UsuarioDTO() {
    }

    public Integer getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Integer idusuario) {
        this.idusuario = idusuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

}
