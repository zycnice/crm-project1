package com.zyc.crm.poi;

/**
 * @author zyc
 * @version 1.0
 */

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 使用apache-poi生成excel文件
 */
public class CreateExcelTest {
    public static void main(String[] args) throws IOException {
        //创建HSSFWorkbook对象，对应一个excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        //使用wb创建HSSFSheet对象，对应web文件中的一页
        HSSFSheet sheet = wb.createSheet("学生列表");
        //使用sheet创建HSSFRow对象，对应sheet中的一行
        //行号从0开始，依次增加
        HSSFRow row = sheet.createRow(0);
        //使用row创建HSSFCell对象，对应row中的列
        //列号从0开始，依次增加
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("学号");
        cell = row.createCell(1);
        cell.setCellValue("姓名");
        cell = row.createCell(2);
        cell.setCellValue("年龄");

        //生成HSSFCellStyle对象
        HSSFCellStyle cellStyle = wb.createCellStyle();
        //居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        //使用sheet创建10个HSSRow对象，对应sheet中的10行
        for (int i = 1; i <= 10 ; i++) {
            //行
            row = sheet.createRow(i);
            //列
            cell = row.createCell(0);
            //值
            cell.setCellValue(100+i);
            cell = row.createCell(1);
            cell.setCellValue("name"+i);
            cell = row.createCell(2);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(20+i);

        }

        //调用工具函数生成excel文件
        //通过输出流
        FileOutputStream os = new FileOutputStream("C:\\Users\\27266\\Desktop\\test\\studentList.xls");
        wb.write(os);

        //关闭资源
        os.close();
        wb.close();

        System.out.println("=======createOk");
    }
}
