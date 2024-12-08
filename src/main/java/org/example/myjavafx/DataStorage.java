package org.example.myjavafx;//package com.example.org.example.myjavafx.library;

import java.io.*;

/*
public class org.example.myjavafx.DataStorage {

    public static void serializeObject(Object obj, String filename) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(filename);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(obj);
        out.close();
        fileOut.close();
    }

    public static Object deserializeObject(String filename) throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream(filename);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        Object obj = in.readObject();
        in.close();
        fileIn.close();
        return obj;
    }

    // Other relevant methods...
}
*/


public class DataStorage {

    private static final String MEDIALAB_FOLDER = "medialab/";

    public static void serializeObject(Object obj, String filename) throws IOException {
        String filePath = MEDIALAB_FOLDER + filename;
        try (FileOutputStream fileOut = new FileOutputStream(filePath);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(obj);
            out.close();
            fileOut.close();
        }
    }

    public static Object deserializeObject(String filename) throws IOException, ClassNotFoundException {
        String filePath = MEDIALAB_FOLDER + filename;
        try (FileInputStream fileIn = new FileInputStream(filePath);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            Object obj = in.readObject(); // Read the object before closing the streams
            return obj;
        }
    }

    // Other relevant methods...
}
