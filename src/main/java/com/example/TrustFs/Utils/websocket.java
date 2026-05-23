package com.example.TrustFs.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.DataInputStream;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

@Component
public class websocket extends Thread {
    DataInputStream inputStream;
    OutputStream outputStream;
    public ServerSocket socket = null;
    public HashMap<Integer,Integer>hashMap=null;
    FileEditor editor=null ;
    public websocket(){}
    public websocket(ServerSocket socket,HashMap<Integer,Integer>map,String path){
        this.hashMap= map;
        this.socket = socket;
        try{editor = new FileEditor(path);}
        catch (Exception e){
            System.out.println("error in the file reachness"+e);
        }

    }
    public void run(){
        try{
            Socket sockets = socket.accept();
            System.out.println("new client arrived");
            inputStream = new DataInputStream(sockets.getInputStream());
            outputStream = new DataOutputStream(sockets.getOutputStream());
            while(true){
                System.out.println("waiting for new sms from client");
                String request = inputStream.readUTF();
                System.out.println("messaage from client "+request);
                String[] parse = request.split("~");

                if(parse[0].equals("1")){
                    System.out.println("adding a line");

                    editor.addfirst(parse[1]);
                    for(int i =0 ;i<editor.lines.size();i++){System.out.println(editor.lines.get(i));}
                }
                else if(parse[0].equals("2")){
                    int line = Integer.parseInt(parse[1]);
                    editor.Addline(line,parse[2]);
                }
                else if(parse[0].equals(("3"))){
                    int line = Integer.parseInt(parse[1]);
                    editor.removeLine(line);
                }
                else if(parse[0].equals(("4"))){
                    int line = Integer.parseInt(parse[1]);
                    editor.replaceLine(line,parse[2]);
                }
                else if(parse[0].equals("5")){
                    editor.addLast(parse[1]);
                }
                else {
                    editor.save();
                    break;
                }

            }
        }
        catch(Exception exp){
            System.out.println("socket not accepting ");
        }
        finally{
            try{
                inputStream.close();
                outputStream.close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
