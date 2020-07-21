import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class WordEditor {

    public static void editorMsWord(String pathForFileCopy, String pathS, String s) throws FileNotFoundException {
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
