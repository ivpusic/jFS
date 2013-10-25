/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.ivpusic_zadaca_1.VFSTypes;

import org.foi.uzdiz.ivpusic_zadaca_1.VFSAux.VFSIdenticator.VFSLSType;
import org.foi.uzdiz.ivpusic_zadaca_1.VFSNodes.VFSNode;
import java.util.ArrayList;

/**
 *
 * @author ipusic
 */
public abstract class VFS {

    protected static VFS instance;
    public static VFSNode root;
    public static String type;
    public static int maxFileNameLength;
    public static int maxFileExtLength;
    public static boolean hasBrokenFiles;
    public static ArrayList<VFSNode> symLinks;
    protected StringBuilder printOffset;
    protected StringBuilder printFormat;
    protected ArrayList<String> printArgs;

    public void resetNodeInfoOffset() {
        printOffset.setLength(printOffset.length() - 3);
    }

    public void printBasicNodeInfo(VFSNode node) {
        printArgs.add(printOffset.toString());
        printFormat.append("%s");

        printArgs.add(String.valueOf(node.id));
        printFormat.append("ID: %s");

        printArgs.add(node.file.getName());
        printFormat.append(", Name: %s");

        printArgs.add(String.valueOf(node.file.length()));
        printFormat.append(", Length: %sB");

        if (node.children != null) {
            printArgs.add(String.valueOf(node.children.size()));
            printFormat.append(", Children: %s");
        }

        if (node.broken) {
            printFormat.append(", broken file");
        }
    }

    public void checkIfNodeExist(VFSNode node, int id) throws Exception {
        if (node == null) {
            throw new Exception(String.format("Cannot find file with id %s", id));
        }
    }

    public abstract void printNodeInfo(VFSNode node, boolean addOffset);

    public static VFS getVFS() {
        return instance;
    }

    public void listAll() {
        root.ls(VFSLSType.ALL);

        if (hasBrokenFiles) {
            System.err.println("File system has broken files! Please check file names, extensions, etc.!");
        }
    }

    public void listFrom(int id) throws Exception {
        VFSNode node = root.findChild(id);
        checkIfNodeExist(node, id);

        node.ls(VFSLSType.ALL);
    }

    public void listSpecific(int id) throws Exception {
        VFSNode node = root.findChild(id);
        checkIfNodeExist(node, id);

        node.ls(VFSLSType.SPECIFIC);
    }

    public void listParents(int id) throws Exception {
        VFSNode node = root.findChild(id);
        checkIfNodeExist(node, id);

        node.parent().ls(VFSLSType.PARENTS);
    }

    public void remove(int id) throws Exception {
        if (id == 0) {
            throw new Exception("Cannot remove root element!");
        }
        VFSNode node = root.findChild(id);
        checkIfNodeExist(node, id);

        if (node.parent() != null) {
            VFSNode parent = node.parent();
            parent.rm(id, node.isSymLink);
        }
    }

    public abstract void copy(int from, String to) throws Exception;

    public void move(int from, int to) throws Exception {
        VFSNode node = root.findChild(from);
        checkIfNodeExist(node, from);
        node.mv(to);
    }
}
