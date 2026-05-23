package com.example.TrustFs.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

@Component
public class Filesharer {

    public String path = "/home/hemant/Desktop/TrustFs/Storage/user1/";
    public int startServer(CountDownLatch countDownLatch,String filename) {

        try {
            
            File file = new File(path+filename);
            if(file.exists()){
                System.out.println("file found of the name "+file.toString());
            }
            else throw new FileNotFoundException("FILE IS NOT FOUND IN THE DIRECTORY"+ file.toString());
            ServerSocket server = new ServerSocket(0);


            int port =  server.getLocalPort();
            System.out.println("Server started at a port : "+port);
            new Thread(()->{
                try{
                   


                    FileInputStream fis = new FileInputStream(file);
                    countDownLatch.countDown();
                    Socket socket = server.accept();

                    OutputStream out =
                            socket.getOutputStream();

                    byte[] buffer = new byte[8192];
                    System.out.println("sending a bytes of data");
                    int count;

                    while((count = fis.read(buffer)) != -1) {
                        System.out.println("sending a bytes: "+buffer);
                        out.write(buffer, 0, count);
                    }
                    System.out.println("file sended to the controller ");
                    fis.close();
                    System.out.println("closing a  output socket");
                    out.close();
                }
                catch(Exception exp){
                    System.out.println("error in filesharer server : "+exp);
                }



            }).start();
            return port;
        }
        catch(FileNotFoundException e) {
            System.out.println("File Error :"+e);
        }
        catch(SocketException e) {
            System.out.println("Socket Error");
        }
        catch(IOException e) {
            System.out.println("General IO Error");
        }
        return -1;

    }


}
