package com.example.template.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/test")
    public Map<String, String> test() {
        Map<String, String> map = new HashMap<>();
        map.put("response", "1");
        return map;
    }

    @RequestMapping("/stream")
    public StreamingResponseBody handleRequest() {

        return out -> {
            for (int i = 0; i < 1000; i++) {
                out.write((i + " - ").getBytes());
                out.flush();
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @GetMapping("/stream")
    public StreamingResponseBody download(HttpServletResponse response) throws FileNotFoundException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"demo.pdf\"");
        InputStream inputStream = new FileInputStream(new File("D:\\temp\\uuid.txt"));
        return outputStream -> {
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                System.out.println("Writing some bytes..");
                outputStream.write(data, 0, nRead);
            }
        };
    }

    @GetMapping("/suspend")
    public String suspend() throws InterruptedException {
        Long sleep = (long) (Math.random() * (500 - 100)) + 100 ;
        TimeUnit.MILLISECONDS.sleep(sleep);
        return "1";
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data" )
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        String destFilename = System.currentTimeMillis() + ".txt";
//        Path path = Paths.get(URI.create("D:\\temp\\download" + File.separator + destFilename));
        File file1 = new File("D:\\temp\\download" + File.separator + destFilename);
        file.transferTo(file1);
        return "1";
    }

}
