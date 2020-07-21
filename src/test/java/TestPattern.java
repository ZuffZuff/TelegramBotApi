import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.*;

public class TestPattern {
    public static void main(String[] args) {
       // System.out.println(Pattern.matches("^[А-Я]{1}[а-я]{1,30}\\s[А-Я]{1}[а-я]{1,30}","Алла Боря"));
        String pathS = ".\\\\src\\\\main\\\\resources\\\\DocumentForSend\\\\NameAndSecondNAme.docx".replaceAll("\\\\", "/");
        String pathForFileCopy = ".\\src\\main\\resources\\Examples\\ExampleOfContract.docx".replaceAll("\\\\", "/");
        Path p = Paths.get(pathS);
//        System.out.println(Files.exists(p));
        try {
            Files.createFile(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(Files.exists(p));
//        try(FileInputStream fis =new FileInputStream(pathForFileCopy)){
//            try(FileOutputStream fos = new FileOutputStream(pathS)){
//                byte[] buffer = new byte[fis.available()];
//                while(fis.available()>0){
//                    fis.read(buffer);
//                }
//                fos.write(buffer);
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            editorMsWord(pathForFileCopy, pathS, "Анна Мария");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static void editorMsWord(String pathForFileCopy, String pathS, String s) throws FileNotFoundException {
        try(ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(pathS))){
            ZipFile zf = new ZipFile(pathForFileCopy);
            for(Enumeration<? extends ZipEntry> iteration = zf.entries(); iteration.hasMoreElements();){
                ZipEntry ze = iteration.nextElement();
                //System.out.println(ze.getName());
                if(!ze.isDirectory()){
                    ZipEntry newZE = new ZipEntry(ze.getName());
                    zos.putNextEntry(newZE);
                    try(InputStream fis = zf.getInputStream(ze)) {
                        byte[] bufferDocument;
                        if (ze.getName().equals("word/document.xml")) {
                            bufferDocument = new byte[(int) ze.getSize()];
                            while (fis.available() > 0) {
                                fis.read(bufferDocument);
                            }
                            String forSearch=new String(bufferDocument, "UTF-8");
//                            for(int i=0; i<bufferDocument.length; i++){
//                                forSearch=forSearch + (char)bufferDocument[i];
//                                if(bufferDocument[i]==10){
//                                    System.out.println(forSearch);
//                                    forSearch="";
//                                }else{
//                                    forSearch=forSearch + (char)bufferDocument[i];
//                                }
//                            }
                            System.out.println(forSearch);
                            forSearch=forSearch.replaceAll("Username", s);
                            System.out.println(forSearch);
                            zos.write(bufferDocument=forSearch.getBytes());

                        }else {
                                bufferDocument = new byte[(int) ze.getSize()];
                                while (fis.available() > 0) {
                                    fis.read(bufferDocument);
                                }
                                zos.write(bufferDocument);
                            }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
