/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mim.entities;

import java.io.Serializable;
import java.util.List;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 *
 * @author DAMMA
 */
@Entity
@Table(name = "entidadmedica")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Entidadmedica.findAll", query = "SELECT e FROM Entidadmedica e"),
    @NamedQuery(name = "Entidadmedica.findByIdentidadmedica", query = "SELECT e FROM Entidadmedica e WHERE e.identidadmedica = :identidadmedica"),
    @NamedQuery(name = "Entidadmedica.findByNombre", query = "SELECT e FROM Entidadmedica e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "Entidadmedica.findByTelefono", query = "SELECT e FROM Entidadmedica e WHERE e.telefono = :telefono")})
public class Entidadmedica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "identidadmedica")
    private Integer identidadmedica;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "icono")
    private String icono;
    @Column(name = "ubicacion")
    private String ubicacion;
    @JoinColumn(name = "usuario_idusuario", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario usuarioIdusuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entidadmedicaIdentidadmedica")
    private List<Paciente> pacienteList;

    public Entidadmedica() {
    }

    public Entidadmedica(Integer identidadmedica) {
        this.identidadmedica = identidadmedica;
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

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Usuario getUsuarioIdusuario() {
        return usuarioIdusuario;
    }

    public void setUsuarioIdusuario(Usuario usuarioIdusuario) {
        this.usuarioIdusuario = usuarioIdusuario;
    }

    @XmlTransient
    public List<Paciente> getPacienteList() {
        return pacienteList;
    }

    public void setPacienteList(List<Paciente> pacienteList) {
        this.pacienteList = pacienteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (identidadmedica != null ? identidadmedica.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Entidadmedica)) {
            return false;
        }
        Entidadmedica other = (Entidadmedica) object;
        if ((this.identidadmedica == null && other.identidadmedica != null) || (this.identidadmedica != null && !this.identidadmedica.equals(other.identidadmedica))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mim.entities.Entidadmedica[ identidadmedica=" + identidadmedica + " ]";
    }
    
}
