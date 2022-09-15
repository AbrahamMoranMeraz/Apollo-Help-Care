/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mim.websockettest.controllers;

import jakarta.faces.FacesException;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.ExternalContextFactory;

/**
 *
 * @author robb
 */
public class Factory extends ExternalContextFactory {

    public Factory(ExternalContextFactory wrapped) {
        super(wrapped);
    }

    @Override
    public ExternalContext getExternalContext(Object context, Object request, Object response) throws FacesException {
        return new Custom(getWrapped().getExternalContext(context, request, response));
    }
}
