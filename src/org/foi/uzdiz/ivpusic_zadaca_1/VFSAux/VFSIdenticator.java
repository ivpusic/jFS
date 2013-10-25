/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.ivpusic_zadaca_1.VFSAux;

/**
 *
 * @author ipusic
 */
public class VFSIdenticator {

    private int id;
    private static volatile VFSIdenticator instance;

    public static enum VFSLSType {
        ALL,
        SPECIFIC,
        PARENTS
    }

    public int generateId() {
        synchronized (VFSIdenticator.class) {
            return id++;
        }
    }

    public static VFSIdenticator getIdenticator() {
        synchronized (VFSIdenticator.class) {
            if (instance == null) {
                instance = new VFSIdenticator();
            }
        }
        return instance;
    }
}