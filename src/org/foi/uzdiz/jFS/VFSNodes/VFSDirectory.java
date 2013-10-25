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
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import static org.foi.uzdiz.jFS.VFSTypes.VFS.hasBrokenFiles;
import static org.foi.uzdiz.jFS.VFSTypes.VFS.maxFileNameLength;

/**
 *
 * @author ipusic
 */
public class VFSDirectory extends VFSNode implements FileFilter {

    public VFSDirectory(File file, VFSNode parent, boolean isSymLink) throws IOException, Exception {
        this.file = file;
        this.parent = parent;
        this.isSymLink = isSymLink;

        children = new ArrayList<>();
        id = VFSIdenticator.getIdenticator().generateId();
        if (!isSymLink) {
            internalInit(file);
        }
    }

    private void internalInit(File directory) throws IOException, Exception {

        File[] fList = directory.listFiles();
        for (File f : fList) {
            String fileParts[] = f.getName().split("\\.");

            if (fileParts.length > 1) {
                // TODO: support case --> ivan.pusic.txt
                if (fileParts[0].length() > VFS.maxFileNameLength) {
                    VFS.hasBrokenFiles = true;
                    continue;
                }
                if (fileParts[1].length() > VFS.maxFileExtLength) {
                    VFS.hasBrokenFiles = true;
                    continue;
                }
            }

            if (f.isFile()) {
                if (Files.isSymbolicLink(f.toPath())) {
                    addNode(new VFSSymFile(f, this));
                } else {
                    addNode(new VFSFile(f, this));
                }
            } else if (f.isDirectory()) {
                if (Files.isSymbolicLink(f.toPath())) {
                    addNode(new VFSSymDirectory(f, this));
                } else {
                    addNode(new VFSDirectory(f, this, false));
                }
            } else {
                addNode(new VFSBroken(f, this));
            }
        }
    }

    @Override
    public void ls(VFSLSType type) {
        VFS.getVFS().printNodeInfo(this, true);
        switch (type) {
            case ALL:
                for (VFSNode node : children) {
                    node.ls(VFSLSType.ALL);
                }
                break;
            case PARENTS:
                if (parent != null) {
                    parent.ls(VFSLSType.PARENTS);
                }
                break;
            case SPECIFIC:
                for (VFSNode node : children) {
                    VFS.getVFS().printNodeInfo(node, false);
                }
                break;
        }
        VFS.getVFS().resetNodeInfoOffset();
    }

    @Override
    public void addNode(VFSNode node) {
        children.add(node);
    }

    public void delete(File f) throws IOException {
        if (f.isDirectory()) {
            for (File c : f.listFiles()) {
                delete(c);
            }
        }
        if (!f.delete()) {
            throw new FileNotFoundException("Failed to delete file: " + f);
        }
    }

    @Override
    public void rm(int id, boolean sym) {
        for (VFSNode node : children) {
            if (node.id == id) {
                if (!sym) {
                    if (node.children != null) {
                        node.children.clear();
                    }
                }
                children.remove(node);
                try {
                    delete(node.file);
                } catch (IOException ex) {
                    Logger.getLogger(VFSDirectory.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
        }
    }

    @Override
    public void cp(String name) {
        File newFile;
        try {
            newFile = new File(String.format("%s/%s", parent.file.getPath(), name));
            FileUtils.copyDirectory(file, newFile, this);
            if (parent != null) {
                parent.children.add(new VFSDirectory(newFile, parent, false));
            } else {
                children.add(new VFSDirectory(newFile, this, false));
            }
        } catch (IOException ex) {
            Logger.getLogger(VFSDirectory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(VFSDirectory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public VFSNode findChild(int id) {
        if (this.id == id) {
            return this;
        }
        VFSNode nodeChild;
        for (VFSNode node : children) {
            nodeChild = node.findChild(id);
            if (nodeChild != null) {
                return nodeChild;
            }
        }
        return null;
    }

    @Override
    public VFSNode findChild(File file) {
        if (this.file.getPath().equals(file.getPath())) {
            return this;
        }
        VFSNode nodeChild;
        for (VFSNode node : children) {
            nodeChild = node.findChild(file);
            if (nodeChild != null) {
                return nodeChild;
            }
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
        for (VFSNode node : children) {
            node.updateFileRef(file);
        }
    }

    @Override
    public boolean accept(File file) {
        VFSNode node = VFS.root.findChild(file);
        try {
            if (node == null || (node.isSymLink && !node.file.getCanonicalFile().exists())) {
                file.delete();
                return false;
            }
        } catch (IOException ex) {
            Logger.getLogger(VFSDirectory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
}
