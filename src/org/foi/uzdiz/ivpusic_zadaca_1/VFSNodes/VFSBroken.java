/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.uzdiz.ivpusic_zadaca_1.VFSNodes;

import java.io.File;

/**
 *
 * @author ipusic
 */
public class VFSBroken extends VFSFile {
    public VFSBroken(File file, VFSNode parent) {
        super(file, parent);
        broken = true;
    }
}
