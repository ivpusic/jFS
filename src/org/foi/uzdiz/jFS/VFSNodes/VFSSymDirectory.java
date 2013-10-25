/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.jFS.VFSNodes;

import org.foi.uzdiz.jFS.VFSTypes.VFS;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author ipusic
 */
public class VFSSymDirectory extends VFSDirectory {

    @SuppressWarnings("LeakingThisInConstructor")
    public VFSSymDirectory(File file, VFSNode parent) throws IOException, Exception {
        super(file, parent, true);
        if (VFS.symLinks == null) {
            System.err.println(String.format("File system '%s' does not support symbolic links! Please remove those files! Program will exit now...", VFS.type));
            System.exit(0);
        }
        VFS.symLinks.add(this);
    }
}