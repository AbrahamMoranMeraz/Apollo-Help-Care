/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mim.entities;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DAMMA
 */
@Entity
@Table(name = "audiomensaje")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Audiomensaje.findAll", query = "SELECT a FROM Audiomensaje a"),
    @NamedQuery(name = "Audiomensaje.findByIdAudioMensaje", query = "SELECT a FROM Audiomensaje a WHERE a.idAudioMensaje = :idAudioMensaje"),
    @NamedQuery(name = "Audiomensaje.findByMensaje", query = "SELECT a FROM Audiomensaje a WHERE a.mensaje = :mensaje")})
public class Audiomensaje implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idAudioMensaje")
    private Integer idAudioMensaje;
    @Column(name = "mensaje")
    private String mensaje;
    @jakarta.persistence.Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @JoinColumn(name = "idAlerta", referencedColumnName = "idalerta")
    @ManyToOne
    private Alerta idAlerta;

    public Audiomensaje() {
    }

    public Audiomensaje(Integer idAudioMensaje) {
        this.idAudioMensaje = idAudioMensaje;
    }

    public Integer getIdAudioMensaje() {
        return idAudioMensaje;
    }

    public void setIdAudioMensaje(Integer idAudioMensaje) {
        this.idAudioMensaje = idAudioMensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Alerta getIdAlerta() {
        return idAlerta;
    }

    public void setIdAlerta(Alerta idAlerta) {
        this.idAlerta = idAlerta;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAudioMensaje != null ? idAudioMensaje.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Audiomensaje)) {
            return false;
        }
        Audiomensaje other = (Audiomensaje) object;
        if ((this.idAudioMensaje == null && other.idAudioMensaje != null) || (this.idAudioMensaje != null && !this.idAudioMensaje.equals(other.idAudioMensaje))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mim.entities.Audiomensaje[ idAudioMensaje=" + idAudioMensaje + " ]";
    }
    
}
