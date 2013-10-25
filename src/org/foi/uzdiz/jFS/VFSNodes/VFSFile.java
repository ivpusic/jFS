/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.jFS.VFSNodes;

import org.foi.uzdiz.jFS.VFSAux.VFSIdenticator;
import org.foi.uzdiz.jFS.VFSAux.VFSIdenticator.VFSLSType;
import org.foi.uzdiz.jFS.VFSTypes.VFS;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author ipusic
 */
public class VFSFile extends VFSNode {

    public VFSFile(File file, VFSNode parent) {
        this.file = file;
        this.parent = parent;

        id = VFSIdenticator.getIdenticator().generateId();
    }

    @Override
    public void ls(VFSLSType type) {
        switch (type) {
            case ALL:
                VFS.getVFS().printNodeInfo(this, false);
                break;
            case PARENTS:
                if (parent != null) {
                    parent.ls(VFSLSType.PARENTS);
                }
                break;
            case SPECIFIC:
                VFS.getVFS().printNodeInfo(this, false);
                break;
        }
    }

    @Override
    public void cp(String name) {
        File newFile;
        try {
            newFile = new File(String.format("%s/%s", parent.file.getPath(), name));
            FileUtils.copyFile(file, newFile);
            if (parent != null) {
                parent.children.add(new VFSFile(newFile, parent));
            } else {
                children.add(new VFSFile(newFile, this));
            }
        } catch (IOException ex) {
            Logger.getLogger(VFSFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void addNode(VFSNode node) {
        throw new UnsupportedOperationException("Files cannot contain childrens! Only directories can!");
    }

    @Override
    public VFSNode findChild(int id) {
        if (this.id == id) {
            return this;
        }
        return null;
    }

    @Override
    public VFSNode findChild(File file) {
        if (this.file.getPath().equals(file.getPath())) {
            return this;
        }
        return null;
    }

    @Override
    public VFSNode parent() {
        return parent;
    }

    @Override
    public void updateFileRef(File f) {
        file = new File(String.format("%s/%s", f.getPath(), file.getName()));
    }

    @Override
    public void rm(int id, boolean sym) {
    }
}
