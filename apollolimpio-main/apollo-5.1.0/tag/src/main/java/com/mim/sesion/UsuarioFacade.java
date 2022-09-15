/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mim.sesion;

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
public class UsuarioFacade extends AbstractFacade<Usuario> {

    @PersistenceContext(
            unitName = "hackaton2022"
    )
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return this.em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }

    public Usuario find(String correo, String password) {
        TypedQuery<Usuario> query = this.em.createQuery("SELECT c FROM Usuario c WHERE c.correo = :usr AND c.contrasena = :pass ", Usuario.class);
        query.setParameter("usr", correo);
        query.setParameter("pass", password);
        try {
            return (Usuario) query.getSingleResult();
        } catch (NoResultException var5) {
            return null;
        }
    }
    
    public Usuario findByNombre(String nombre) {
        TypedQuery<Usuario> query = this.em.createQuery("SELECT c FROM Usuario c WHERE c.nombre = :nombre", Usuario.class);
        query.setParameter("nombre", nombre);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
