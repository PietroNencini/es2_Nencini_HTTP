package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.file.Files;

public class Main {
    public static String getWantedContentType(String content_request) {
        String[] splitted = content_request.split("\\.");
        String ext = splitted[splitted.length -1];
        switch(ext) {
            case "html":
                return "text/html";
            case "css":
                return "text/css";
            case "png":
                return "image/png";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "js":
                return "application/javascript";
            default:
                return "text/plain";
        }

    }
    public static void main(String[] args) throws IOException{
        System.out.println("Il server è ora in esecuzione");
        ServerSocket ss = new ServerSocket(8080);
        while(true) {
            Socket s = ss.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            // Ricevo la prima riga della richiesta
            String[] lines = in.readLine().split(" ");
            String method = lines[0];
            String resource = lines[1];
            String version = lines[2];

            resource = URLDecoder.decode(resource, "UTF-8");

            String header;
            do {
                header = in.readLine();
                System.out.println(header);
            } while(!header.isEmpty());
            
            String resourcePath = resource.equals("/") ? "/index.html" : resource;
            File file = new File("htdocs" + resourcePath);


            if(file.exists()) {
                out.writeBytes("HTTP/1.1 200 OK\n");
                out.writeBytes("Content-Type: " + getWantedContentType(resourcePath) + "\n");
                out.writeBytes("Content-Length: " + file.length() + "\n");
                out.writeBytes("\r\n");
                InputStream input = new FileInputStream(file);
                byte[] buf = new byte[65536];
                int n;
                while((n = input.read(buf)) != -1)
                    out.write(buf, 0, n);
                input.close();
                //byte[] fileBytes = Files.readAllBytes(file.toPath());
                //out.write(fileBytes);
            } else {
                String errorMsg = "Pagina non trovata";
                out.writeBytes("HTTP/1.1 404 NOT_FOUND\n");
                out.writeBytes("Content-Type: text/plain\n");
                out.writeBytes("Content-Length: "+ errorMsg.length() +"");
                out.writeBytes("\n");
                out.writeBytes(errorMsg);
            }
            System.out.println("Richiesta terminata");
            s.close();
        }
    }
}