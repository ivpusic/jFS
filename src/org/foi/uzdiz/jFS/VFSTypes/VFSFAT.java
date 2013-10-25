/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.jFS.VFSTypes;

import org.foi.uzdiz.jFS.VFSNodes.VFSDirectory;
import org.foi.uzdiz.jFS.VFSNodes.VFSNode;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ipusic
 */
public class VFSFAT extends VFS {

    private VFSFAT(String initPath) throws IOException, Exception {
        type = "FAT";
        maxFileExtLength = 3;
        maxFileNameLength = 8;
        try {
            root = new VFSDirectory(new File(initPath), null, false);
        } catch (Exception e) {
            System.err.println("Cannot make VFS. Please check provided root path, or are there some broken files, eg. symbolic links, etc.!");
            System.exit(0);
        }
        printOffset = new StringBuilder();
    }

    public static VFSFAT getVFS(String initPath) throws IOException, Exception {
        synchronized (VFSEXT4.class) {
            if (instance == null) {
                instance = new VFSFAT(initPath);
            }
        }
        return (VFSFAT) instance;
    }

    @Override
    public void printNodeInfo(VFSNode node, boolean addOffset) {

        printFormat = new StringBuilder();
        printArgs = new ArrayList<>();

        printBasicNodeInfo(node);
        System.out.println(String.format(printFormat.toString(), (Object[]) printArgs.toArray()));

        if (addOffset) {
            printOffset.append("   ");
        }
    }

    public boolean validateName(String fileName) {
        Pattern pattern;
        // TODO: FIX REGEX!!!
        pattern = Pattern.compile("/\"\\*\\+,\\/\\:;<\\=>\\?\\\\\\[\\]\\|*/");
        Matcher matcher = pattern.matcher(fileName);
        //return matcher.find();
        return true;
    }

    @Override
    public void copy(int from, String to) throws Exception {
        if (to.length() > 8) {
            System.err.println(String.format("File names for '%s' FS can have maximum 8 characters!", VFS.type));
        } else if (!validateName(to)) {
            System.err.println(String.format("File name contains illegal characters for '%s' FS!", VFS.type));
        } else {
            VFSNode node = root.findChild(from);
            checkIfNodeExist(node, from);
            node.cp(to);
        }
    }
}
