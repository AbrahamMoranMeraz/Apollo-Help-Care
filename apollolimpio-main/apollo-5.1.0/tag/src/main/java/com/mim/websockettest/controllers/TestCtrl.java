/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mim.websockettest.controllers;

import jakarta.faces.push.Push;
import jakarta.faces.push.PushContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

/**
 *
 * @author robb
 */
@Named("test")
@ViewScoped
public class TestCtrl implements Serializable {

    @Inject
    @Push
    private PushContext channelName;

    public TestCtrl() {
    }

    public void sendEvent() {
        System.out.println("si funciono....");
        channelName.send("hola...");
    }
}
