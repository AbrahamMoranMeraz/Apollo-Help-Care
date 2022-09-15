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
@Table(name = "tipoalerta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tipoalerta.findAll", query = "SELECT t FROM Tipoalerta t"),
    @NamedQuery(name = "Tipoalerta.findByIdtipoalerta", query = "SELECT t FROM Tipoalerta t WHERE t.idtipoalerta = :idtipoalerta"),
    @NamedQuery(name = "Tipoalerta.findByNombre", query = "SELECT t FROM Tipoalerta t WHERE t.nombre = :nombre")})
public class Tipoalerta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idtipoalerta")
    private Integer idtipoalerta;
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoalertaIdtipoalerta")
    private List<Alerta> alertaList;

    public Tipoalerta() {
    }

    public Tipoalerta(Integer idtipoalerta) {
        this.idtipoalerta = idtipoalerta;
    }

    public Integer getIdtipoalerta() {
        return idtipoalerta;
    }

    public void setIdtipoalerta(Integer idtipoalerta) {
        this.idtipoalerta = idtipoalerta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<Alerta> getAlertaList() {
        return alertaList;
    }

    public void setAlertaList(List<Alerta> alertaList) {
        this.alertaList = alertaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idtipoalerta != null ? idtipoalerta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tipoalerta)) {
            return false;
        }
        Tipoalerta other = (Tipoalerta) object;
        if ((this.idtipoalerta == null && other.idtipoalerta != null) || (this.idtipoalerta != null && !this.idtipoalerta.equals(other.idtipoalerta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mim.entities.Tipoalerta[ idtipoalerta=" + idtipoalerta + " ]";
    }
    
}
