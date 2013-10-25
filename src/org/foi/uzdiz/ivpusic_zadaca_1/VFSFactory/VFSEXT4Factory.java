/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.uzdiz.ivpusic_zadaca_1.VFSFactory;

import org.foi.uzdiz.ivpusic_zadaca_1.VFSTypes.VFS;
import org.foi.uzdiz.ivpusic_zadaca_1.VFSTypes.VFSEXT4;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ipusic
 */
public class VFSEXT4Factory extends VFSFactory {

    @Override
    public VFS makeVFS(String initPath) {
        try {
            return VFSEXT4.getVFS(initPath);
        } catch (IOException ex) {
            Logger.getLogger(VFSEXT4Factory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(VFSEXT4Factory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}