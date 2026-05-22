package com.example.TrustFs.Security;

import java.io.File;

public class security {
    public static void main(String []args){
        File path = new File("/home/bodhicoder/Desktop/TrustFs/Storage/user1");
        if(path.exists()){
            System.out.println("found a file");
            
        }
        else System.out.println("not found a file");


    }
}
