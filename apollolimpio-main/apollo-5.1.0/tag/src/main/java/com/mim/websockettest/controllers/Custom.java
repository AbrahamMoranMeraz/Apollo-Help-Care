/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mim.websockettest.controllers;

import jakarta.faces.FacesException;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.ExternalContextFactory;
import jakarta.faces.context.ExternalContextWrapper;

/**
 *
 * @author robb
 */
public class Custom extends ExternalContextWrapper{
    public Custom(ExternalContext wrapped) {
        super(wrapped);
    }

    @Override
    public String encodeWebsocketURL(String url) {
        return super.encodeWebsocketURL(url).replaceFirst("ws://", "wss://");
    }

    @Override
    public ExternalContext getWrapped() {
        return super.getWrapped(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
    
    

  
}
