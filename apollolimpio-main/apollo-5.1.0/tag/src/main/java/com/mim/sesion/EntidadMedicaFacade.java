/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mim.sesion;

import com.mim.entities.Entidadmedica;
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
public class EntidadMedicaFacade extends AbstractFacade<Entidadmedica> {

    @PersistenceContext(
            unitName = "hackaton2022"
    )
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return this.em;
    }

    public EntidadMedicaFacade() {
        super(Entidadmedica.class);
    }

    public Entidadmedica findByUsuario(int id) {
        TypedQuery<Entidadmedica> query = this.em.createQuery("SELECT c FROM Entidadmedica c WHERE c.usuarioIdusuario.idusuario = :id", Entidadmedica.class);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
