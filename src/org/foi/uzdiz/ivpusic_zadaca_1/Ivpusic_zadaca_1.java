/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.ivpusic_zadaca_1;

import org.foi.uzdiz.ivpusic_zadaca_1.VFSFactory.VFSFactory;
import org.foi.uzdiz.ivpusic_zadaca_1.Parsers.CLIParser;
import org.foi.uzdiz.ivpusic_zadaca_1.VFSTypes.VFS;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author ipusic
 */
public class Ivpusic_zadaca_1 {

    public static String VFS_TYPE;
    public static CLIParser parser;

    public static void showHelp() {
        System.out.println("------------------------------------------------------------------");
        System.out.println("ls:                     Print file system elements");
        System.out.println("ls --show-from {id}:    Print from some element");
        System.out.println("ls --show-only {id}:    Print only one element and its children");
        System.out.println("ls --show-parents {id}: Print parents");
        System.out.println("rm {id}:                Remove some element by ID");
        System.out.println("cp {id} {name}:         Copy some element to another by ID");
        System.out.println("mv {id1} {id2}:         Move element to another by ID");
        System.out.println("help                    Help");
        System.out.println("-------------------------------------------------------------------\n");
    }

    /**
     **
     * Main Virtual file system loop. Method parse arguments provided by user
     *
     * @param vfs Virtual file system to use
     * @throws IOException
     */
    public static void vfsLoop(VFS vfs) throws IOException, Exception {
        while (true) {
            String command[];
            InputStreamReader converter = new InputStreamReader(System.in);
            BufferedReader in = new BufferedReader(converter);
            System.out.print("> ");
            command = in.readLine().split(" ");

            switch (command[0]) {
                case "ls":
                    parser.parseLS(command, vfs);
                    break;
                case "cp":
                    parser.parseCP(command, vfs);
                    break;
                case "mv":
                    parser.parseMV(command, vfs);
                    break;
                case "rm":
                    parser.parseRM(command, vfs);
                    break;
                case "help":
                    showHelp();
                    break;
                case "exit":
                    System.exit(0);
                    break;
                case "":
                    break;
                default:
                    System.err.println("Nepostojeca komanda! Upisite 'help' za pomoc!");
                    break;
            }
        }
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException, Exception {

        if (args.length != 1) {
            System.err.println("You must provide path to FS root directory!");
            System.exit(0);
        }

        parser = new CLIParser();
        VFS_TYPE = System.getenv("FS_TYPE");
        //VFS_TYPE = "FAT";
        if (VFS_TYPE != null) {
            VFSFactory factory = VFSFactory.getVFSFactory(VFS_TYPE);
            VFS vfs = factory.makeVFS(args[0]);
            vfs.listAll();
            showHelp();
            vfsLoop(vfs);
        } else {
            System.err.println("You must provide DS_TIP env variable, with either FAT or EXT4 value! Example on Linux -> export DS_TIP=EXT4");
            System.exit(0);
        }
    }
}