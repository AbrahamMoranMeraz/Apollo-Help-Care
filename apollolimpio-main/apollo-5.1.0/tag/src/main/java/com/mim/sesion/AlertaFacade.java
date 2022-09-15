/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mim.sesion;

import com.mim.entities.Alerta;
import com.mim.entities.Usuario;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

@Stateless
public class AlertaFacade extends AbstractFacade<Alerta> {

    @PersistenceContext(
            unitName = "hackaton2022"
    )
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return this.em;
    }

    public AlertaFacade() {
        super(Alerta.class);
    }
    
     public List<Alerta> findAllOrderByDate(int id) {
        TypedQuery<Alerta> query = this.em.createQuery("SELECT c FROM Alerta c WHERE c.pacienteIdpaciente.entidadmedicaIdentidadmedica.identidadmedica = :id ORDER BY c.fecha DESC", Alerta.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    public List<Alerta> findByEntidadMedica(int id) {
        TypedQuery<Alerta> query = this.em.createQuery("SELECT c FROM Alerta c WHERE c.pacienteIdpaciente.entidadmedicaIdentidadmedica.identidadmedica = :id ORDER BY c.fecha DESC", Alerta.class);
        query.setParameter("id", id);
        return query.getResultList();
    }
    
     public List<Alerta> findByEntidadMedicaOpen(int id) {
        TypedQuery<Alerta> query = this.em.createQuery("SELECT c FROM Alerta c WHERE c.pacienteIdpaciente.entidadmedicaIdentidadmedica.identidadmedica = :id AND c.estatus = 'Open' OR c.pacienteIdpaciente.entidadmedicaIdentidadmedica.identidadmedica = :id AND c.estatus = 'Aware' ORDER BY c.fecha DESC", Alerta.class);
        query.setParameter("id", id);
        return query.getResultList();
    }
    
     public List<Alerta> findByPaciente(int id) {
        TypedQuery<Alerta> query = this.em.createQuery("SELECT c FROM Alerta c WHERE c.pacienteIdpaciente.idpaciente = :id", Alerta.class);
        query.setParameter("id", id);
        return query.getResultList();
    }
}
