/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.ivpusic_zadaca_1.VFSFactory;

import org.foi.uzdiz.ivpusic_zadaca_1.VFSTypes.VFS;

/**
 *
 * @author ipusic
 */
public abstract class VFSFactory {

    private static VFSFactory instance;

    // TODO: add enum!!!
    public static VFSFactory getVFSFactory(String type) {

        synchronized (VFSFactory.class) {
            if (instance != null) {
                return instance;
            } else {
                switch (type) {
                    case "FAT":
                        instance = new VFSFATFactory();
                        break;
                    case "EXT4":
                        instance = new VFSEXT4Factory();
                        break;
                    default:
                        if (type != null) {
                            System.err.println("Unsupported file system!");
                            System.exit(0);
                        }
                        break;
                }
                return instance;
            }
        }
    }

    public abstract VFS makeVFS(String initPath);
}
