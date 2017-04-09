package com.hc.example;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;



public class XMlToDoc{

    private  static  final  String basePath = "D:/cache/test/";   //模版存储路径，项目里我就放在resource下
   public static void main(String[] args)  throws  Exception{

     makeWord();
     makePdfByXcode();

    }

    /**
     * 生成docx
     * @throws Exception
     */
     static  void makeWord() throws Exception{
        /** 初始化配置文件 **/
        Configuration configuration = new Configuration();
        String fileDirectory = basePath;
        /** 加载文件 **/
        configuration.setDirectoryForTemplateLoading(new File(fileDirectory));
        /** 加载模板 **/
        Template template = configuration.getTemplate("document.xml");
        /** 准备数据 **/
        Map<String,String> dataMap = new HashMap<String, String>();
        /** 在ftl文件中有${textDeal}这个标签**/
        dataMap.put("id","黄浦江吴彦祖");
        dataMap.put("number","20");
        dataMap.put("language","java,php,python,c++.......");
        dataMap.put("example","Hello World!");

        /** 指定输出word文件的路径 **/
        String outFilePath = basePath+"data.xml";
        File docFile = new File(outFilePath);
        FileOutputStream fos = new FileOutputStream(docFile);
        Writer out = new BufferedWriter(new OutputStreamWriter(fos),10240);
        template.process(dataMap,out);
        if(out != null){
            out.close();
        }
        try {
            ZipInputStream zipInputStream = ZipUtils.wrapZipInputStream(new FileInputStream(new File(basePath+"test.zip")));
            ZipOutputStream zipOutputStream = ZipUtils.wrapZipOutputStream(new FileOutputStream(new File(basePath+"test.docx")));
            String itemname = "word/document.xml";
            ZipUtils.replaceItem(zipInputStream, zipOutputStream, itemname, new FileInputStream(new File(basePath+"data.xml")));
            System.out.println("success");

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * 生成pdf
     */
     static  void makePdfByXcode(){
        long startTime=System.currentTimeMillis();
        try {
            XWPFDocument document=new XWPFDocument(new FileInputStream(new File(basePath+"test.docx")));
        //    document.setParagraph(new Pa );
            File outFile=new File(basePath+"test.pdf");
            outFile.getParentFile().mkdirs();
            OutputStream out=new FileOutputStream(outFile);
            //    IFontProvider fontProvider = new AbstractFontRegistry();
            PdfOptions options= PdfOptions.create();  //gb2312
            PdfConverter.getInstance().convert(document,out,options);

        }
        catch (  Exception e) {
            e.printStackTrace();
        }
        System.out.println("Generate ooxml.pdf with " + (System.currentTimeMillis() - startTime) + " ms.");
    }



}
































































