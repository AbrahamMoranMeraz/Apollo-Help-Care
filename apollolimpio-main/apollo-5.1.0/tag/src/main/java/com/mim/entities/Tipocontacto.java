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
@Table(name = "tipocontacto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tipocontacto.findAll", query = "SELECT t FROM Tipocontacto t"),
    @NamedQuery(name = "Tipocontacto.findByIdtipocontacto", query = "SELECT t FROM Tipocontacto t WHERE t.idtipocontacto = :idtipocontacto"),
    @NamedQuery(name = "Tipocontacto.findByNombre", query = "SELECT t FROM Tipocontacto t WHERE t.nombre = :nombre")})
public class Tipocontacto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idtipocontacto")
    private Integer idtipocontacto;
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipocontactoIdtipocontacto")
    private List<Contacto> contactoList;

    public Tipocontacto() {
    }

    public Tipocontacto(Integer idtipocontacto) {
        this.idtipocontacto = idtipocontacto;
    }

    public Integer getIdtipocontacto() {
        return idtipocontacto;
    }

    public void setIdtipocontacto(Integer idtipocontacto) {
        this.idtipocontacto = idtipocontacto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<Contacto> getContactoList() {
        return contactoList;
    }

    public void setContactoList(List<Contacto> contactoList) {
        this.contactoList = contactoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idtipocontacto != null ? idtipocontacto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tipocontacto)) {
            return false;
        }
        Tipocontacto other = (Tipocontacto) object;
        if ((this.idtipocontacto == null && other.idtipocontacto != null) || (this.idtipocontacto != null && !this.idtipocontacto.equals(other.idtipocontacto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mim.entities.Tipocontacto[ idtipocontacto=" + idtipocontacto + " ]";
    }
    
}
