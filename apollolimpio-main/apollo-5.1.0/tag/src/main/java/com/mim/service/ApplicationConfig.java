/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mim.service;

import jakarta.ws.rs.core.Application;
import java.util.Set;

/**
 *
 * @author robb
 */
@jakarta.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * populated with all resources defined in the project. If required, comment
     * out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(AlertaFacadeREST.class);
        resources.add(ContactoFacadeREST.class);
        resources.add(EntidadmedicaFacadeREST.class);
        resources.add(HistorialmedicoFacadeREST.class);
        resources.add(PacienteFacadeREST.class);
        resources.add(TipoalertaFacadeREST.class);
        resources.add(TipocontactoFacadeREST.class);
        resources.add(UsuarioFacadeREST.class);
    }
}
