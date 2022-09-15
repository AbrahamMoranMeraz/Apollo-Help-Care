/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mim.service;

import com.mim.entities.Historialmedico;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.File;

/**
 *
 * @author robb
 */
@jakarta.ejb.Stateless
@Path("com.mim.historialmedico")
public class HistorialmedicoFacadeREST extends AbstractFacade<Historialmedico> {

    @PersistenceContext(unitName = "hackaton2022")
    private EntityManager em;

    public HistorialmedicoFacadeREST() {
        super(Historialmedico.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Historialmedico entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Historialmedico entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Historialmedico find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Historialmedico> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Historialmedico> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    @GET
    @Path("document/{id}")
    @Produces({"application/pdf"})
    public Response retrieveArchive(@PathParam("id") Integer id) {
        if (id != null) {
            TypedQuery<Historialmedico> query = this.em.createQuery("SELECT c FROM Historialmedico c WHERE c.idhistorialmedico = :id", Historialmedico.class);
            query.setParameter("id", id);

            String name = query.getSingleResult().getArchivo();
            System.out.println("Valor " + name);
            if (name != null) {

                File file = null;

                file = new File("/opt/wildfly/hackaton/pdfs/" + name);

                Response.ResponseBuilder response = Response.ok(file);
                response.header("Content-Disposition", "attachment; filename=" + name);
                response.header("nombre", name);
                response.header("Access-Control-Allow-Origin", "*");
                response.header("Access-Control-Allow-Credentials", "true");
                response.header("Access-Control-Allow-Headers",
                        "origin, content-type, accept, authorization");
                response.header("Access-Control-Allow-Methods",
                        "GET, POST, PUT, DELETE, OPTIONS, HEAD");
                return response.build();
            } else {
                return Response.noContent().build();
            }
        } else {
            return Response.noContent().build();
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
