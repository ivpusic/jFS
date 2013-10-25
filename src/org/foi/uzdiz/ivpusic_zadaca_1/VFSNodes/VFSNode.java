/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.ivpusic_zadaca_1.VFSNodes;

import org.foi.uzdiz.ivpusic_zadaca_1.VFSAux.VFSIdenticator.VFSLSType;
import org.foi.uzdiz.ivpusic_zadaca_1.VFSTypes.VFS;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author ipusic
 */
public abstract class VFSNode {

    public int id;
    public boolean isSymLink;
    public boolean broken;
    public File file;
    protected VFSNode parent;
    public ArrayList<VFSNode> children;

    public abstract void addNode(VFSNode node);

    public abstract VFSNode findChild(int id);

    public abstract VFSNode findChild(File file);

    public abstract VFSNode parent();

    public abstract void ls(VFSLSType type);

    public abstract void rm(int id, boolean sym);

    public abstract void cp(String name);

    public abstract void updateFileRef(File f);

    public void mv(int toNode) throws Exception {
        VFSNode node = VFS.root.findChild(toNode);
        if (node.children == null) {
            throw new Exception("Cannot move element to file!");
        }
        parent.children.remove(this);
        File newFile = new File(String.format("%s/%s", node.file.getPath(), file.getName()));
        this.parent = node;
        this.file.renameTo(newFile);
        this.updateFileRef(node.file);
        node.children.add(this);
    }
}