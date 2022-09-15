/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mim.websockettest.controllers;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Random;

/**
 *
 * @author robb
 */
@Named("client")
@ViewScoped
public class ClientCtrl implements Serializable {

    private int number = 0;

    public ClientCtrl() {

    }

    @PostConstruct
    private void init() {
        Random r = new Random();
        int low = 100000000;
        int high = 999999999;
        number = r.nextInt(high - low) + low;
    }

    public void doSomething() {
        System.out.println("holaaaa : " + number);
    }
}
