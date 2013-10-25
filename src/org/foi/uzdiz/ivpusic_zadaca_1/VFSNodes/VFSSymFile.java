/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.ivpusic_zadaca_1.VFSNodes;

import org.foi.uzdiz.ivpusic_zadaca_1.VFSTypes.VFS;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ipusic
 */
public class VFSSymFile extends VFSFile {

    @SuppressWarnings("LeakingThisInConstructor")
    public VFSSymFile(File file, VFSNode parent) throws IOException {
        super(file, parent);
        if (VFS.symLinks == null) {
            System.err.println(String.format("File system '%s' does not support symbolic links! Please remove those files! Program will exit now...", VFS.type));
            System.exit(0);
        }
        isSymLink = true;
        VFS.symLinks.add(this);
    }
}
