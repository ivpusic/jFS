/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.ivpusic_zadaca_1.VFSTypes;

import org.foi.uzdiz.ivpusic_zadaca_1.VFSNodes.VFSDirectory;
import org.foi.uzdiz.ivpusic_zadaca_1.VFSNodes.VFSNode;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ipusic
 */
public class VFSEXT4 extends VFS {

    private VFSEXT4(String initPath) throws IOException, Exception {
        type = "EXT4";
        maxFileExtLength = Integer.MAX_VALUE;
        maxFileNameLength = Integer.MAX_VALUE;
        symLinks = new ArrayList<>();
        try {
            root = new VFSDirectory(new File(initPath), null, false);
        } catch (Exception e) {
            System.err.println("Cannot make VFS. Please check provided root path, are symbolic links valid, etc.!");
            System.exit(0);
        }

        printOffset = new StringBuilder();

        for (VFSNode link : symLinks) {
            File f = Files.readSymbolicLink(link.file.toPath()).toFile();
            VFSNode nodeTarget = root.findChild(f);
            if (nodeTarget != null) {
                link.children = root.findChild(f).children;
            }
        }
    }

    public static VFSEXT4 getVFS(String initPath) throws IOException, Exception {
        synchronized (VFSEXT4.class) {
            if (instance == null) {
                instance = new VFSEXT4(initPath);
            }
        }
        return (VFSEXT4) instance;
    }

    @Override
    @SuppressWarnings("MismatchedReadAndWriteOfArray")
    public void printNodeInfo(VFSNode node, boolean addOffset) {

        StringBuilder permissions = new StringBuilder();
        printFormat = new StringBuilder();
        printArgs = new ArrayList<>();

        printBasicNodeInfo(node);

        if (node.file.canRead()) {
            permissions.append("r");
        }

        if (node.file.canWrite()) {
            permissions.append("w");
        }

        if (node.file.canExecute()) {
            permissions.append("x");
        }

        if (permissions.length() > 0) {
            printArgs.add(permissions.toString());
            printFormat.append(", Permissions: %s");
        }

        if (node.isSymLink) {
            try {
                File targetFile = Files.readSymbolicLink(node.file.toPath()).toFile();
                if (targetFile.exists()) {
                    printFormat.append(", Symbolic link to --> %s");
                    printArgs.add(targetFile.getAbsolutePath());
                } else {
                    printFormat.append(", Broken symlink. Not readable by this VFS!");
                }
            } catch (IOException ex) {
                Logger.getLogger(VFSEXT4.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        System.out.println(String.format(printFormat.toString(), (Object[]) printArgs.toArray()));

        if (addOffset) {
            printOffset.append("   ");
        }
    }

    @Override
    public void copy(int from, String to) throws Exception {
        VFSNode node = root.findChild(from);
        checkIfNodeExist(node, from);

        node.cp(to);
    }
}
