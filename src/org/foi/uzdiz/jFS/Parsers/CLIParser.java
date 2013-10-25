/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.jFS.Parsers;

import org.foi.uzdiz.jFS.VFSTypes.VFS;
import java.io.IOException;

/**
 *
 * @author ipusic
 */
public class CLIParser {

    private String getErrorMsg(String command, Exception ex) {
        return String.format("Error while executing '%s' command! Reason: %s", command, ex.getMessage());
    }

    public void logParseError(String command, String helpMsg) {
        System.err.println(String.format("Error while parsing '%s' command! Example usage: %s", command, helpMsg));
    }

    public void parseLS(String command[], VFS vfs) throws Exception {
        try {
            if (command.length == 1) {
                vfs.listAll();
            } else {
                if (command.length == 3) {
                    switch (command[1]) {
                        case "--show-from":
                            vfs.listFrom(Integer.parseInt(command[2]));
                            return;
                        case "--show-only":
                            vfs.listSpecific(Integer.parseInt(command[2]));
                            return;
                        case "--show-parents":
                            vfs.listParents(Integer.parseInt(command[2]));
                            return;
                    }
                }
                logParseError("ls", "ls --show-something 2");
            }
        } catch (Exception e) {
            System.err.println(getErrorMsg("ls", e));
        }

    }

    public void parseRM(String command[], VFS vfs) {
        try {
            if (command.length == 2) {
                vfs.remove(Integer.parseInt(command[1]));
            } else {
                logParseError("rm", "rm 4");
            }
        } catch (Exception e) {
            System.err.println(getErrorMsg("rm", e));
        }

    }

    public void parseCP(String command[], VFS vfs) throws IOException {
        try {
            if (command.length == 3) {
                vfs.copy(Integer.parseInt(command[1]), command[2]);
            } else {
                logParseError("cp", "cp 3 fileName");
            }
        } catch (Exception e) {
            System.err.println(getErrorMsg("cp", e));
        }

    }

    public void parseMV(String command[], VFS vfs) {
        try {
            if (command.length == 3) {
                vfs.move(Integer.parseInt(command[1]), Integer.parseInt(command[2]));
            } else {
                logParseError("mv", "mv 5 1");
            }
        } catch (Exception e) {
            System.err.println(getErrorMsg("mv", e));
        }

    }
}