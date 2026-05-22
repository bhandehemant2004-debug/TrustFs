package com.example.TrustFs.Controller;

import com.example.TrustFs.Services.Filesharer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

@RestController
public class Controller {
    @Autowired
    Filesharer filesharer;
    @GetMapping("/download/{filename}")
    public ResponseEntity<StreamingResponseBody> download(@PathVariable String filename) throws IOException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        int port = new Filesharer().startServer(latch, filename);
        System.out.println("port is :"+port);
        if(port == -1){
        
            return ResponseEntity.noContent().build();
        }
        latch.await();
        StreamingResponseBody stream=null;
        try {
            stream = outputStream -> {

            //request a fileserver from the controller
                System.out.println("request a fileserver from the controller ");
                Socket socket = new Socket("localhost", port);

                InputStream in = socket.getInputStream();
                System.out.println("got a input stream");
                byte[] buffer = new byte[8192];
                int count;

                try {

                    while ((count = in.read(buffer)) != -1) {
                        System.out.println("receving a buffer : "+buffer);
                        outputStream.write(buffer, 0, count);
                    }

                    outputStream.flush();

                } finally {

                    in.close();
                    socket.close();
                    System.out.println("finished the input process");
                }
            };
        } catch (Exception exp) {
            System.out.println("error in request from the controller");
        }
        System.out.println("returning a response");
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=file.txt"
                )
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(stream);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {

        try {

            File directory = new File("/home/hemant/Desktop/app/firstproject/Storage/user1");

            if (!directory.exists()) {
                boolean created = directory.mkdirs();
                System.out.println("Directory created : " + created);
            }

            File destination =new File(directory,file.getOriginalFilename());

            System.out.println("Saving file to : "+ destination.getAbsolutePath());


            file.transferTo(destination);
            System.out.println("File exists : "+ destination.exists());

            return ResponseEntity.ok("Uploaded");

        } catch (Exception e) {
            System.out.println("Error : " + e);
            return ResponseEntity.internalServerError().body("Failed");
        }
    }

}
