package com.imooc.bos.dao.test;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;

/**  
 * ClassName:POITest <br/>  
 * Function:  <br/>  
 * Date:     2018年3月15日 下午7:31:31 <br/>       
 */
public class POITest {

    public static void main(String[] args) throws Exception {
        // 读取文件
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream("‪a.xls"));
        // 获取工作簿
        HSSFSheet sheet = workbook.getSheetAt(0);
        // 代表一行
        for (Row row : sheet) {
            int rowNum = row.getRowNum();
            if (rowNum == 0) {
                continue;
            }

            for (Cell cell : row) {
                String value = cell.getStringCellValue();
                System.out.print(value + "\t");
            }
            //换行
            System.out.println();
        }
        // 释放资源
        workbook.close();
    }
    
    
    
    
    @Test
    public void f(){
        System.out.println("===================");
        File file = new File("‪a.xls");
        System.out.println(file.exists());
        System.out.println(file.getAbsolutePath());
    }
    
}
  
