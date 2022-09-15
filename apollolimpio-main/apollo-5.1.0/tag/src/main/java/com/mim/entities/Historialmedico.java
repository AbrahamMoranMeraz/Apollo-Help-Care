/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mim.entities;

import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DAMMA
 */
@Entity
@Table(name = "historialmedico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Historialmedico.findAll", query = "SELECT h FROM Historialmedico h"),
    @NamedQuery(name = "Historialmedico.findByIdhistorialmedico", query = "SELECT h FROM Historialmedico h WHERE h.idhistorialmedico = :idhistorialmedico"),
    @NamedQuery(name = "Historialmedico.findByNombre", query = "SELECT h FROM Historialmedico h WHERE h.nombre = :nombre"),
    @NamedQuery(name = "Historialmedico.findByDetalles", query = "SELECT h FROM Historialmedico h WHERE h.detalles = :detalles"),
    @NamedQuery(name = "Historialmedico.findByTratamiento", query = "SELECT h FROM Historialmedico h WHERE h.tratamiento = :tratamiento")})
public class Historialmedico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idhistorialmedico")
    private Integer idhistorialmedico;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "detalles")
    private String detalles;
    @Column(name = "tratamiento")
    private String tratamiento;
     @Column(name = "archivo")
    private String archivo;
    @JoinColumn(name = "paciente_idpaciente", referencedColumnName = "idpaciente")
    @ManyToOne(optional = false)
    private Paciente pacienteIdpaciente;

    public Historialmedico() {
    }

    public Historialmedico(Integer idhistorialmedico) {
        this.idhistorialmedico = idhistorialmedico;
    }

    public Integer getIdhistorialmedico() {
        return idhistorialmedico;
    }

    public void setIdhistorialmedico(Integer idhistorialmedico) {
        this.idhistorialmedico = idhistorialmedico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public Paciente getPacienteIdpaciente() {
        return pacienteIdpaciente;
    }

    public void setPacienteIdpaciente(Paciente pacienteIdpaciente) {
        this.pacienteIdpaciente = pacienteIdpaciente;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idhistorialmedico != null ? idhistorialmedico.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Historialmedico)) {
            return false;
        }
        Historialmedico other = (Historialmedico) object;
        if ((this.idhistorialmedico == null && other.idhistorialmedico != null) || (this.idhistorialmedico != null && !this.idhistorialmedico.equals(other.idhistorialmedico))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mim.entities.Historialmedico[ idhistorialmedico=" + idhistorialmedico + " ]";
    }
    
}
