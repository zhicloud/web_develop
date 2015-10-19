
package com.zhicloud.ms.transform.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.zhicloud.ms.app.helper.AppHelper;
import com.zhicloud.ms.common.util.DateUtil;
import com.zhicloud.ms.exception.AppException;

/**
 * @ClassName: ExportExcelUtils
 * @Description: 通用导出工具类
 * @author 张本缘 于 2015年9月24日 上午11:53:55
 */
public class ExportExcelUtils {
    // 单个sheet允许的最大记录数
    private static final Integer constant = 65534;

    /**
     * @Description:导出Excel
     * @param request
     * @param response
     * @param datalist 数据集合
     * @param titledata excel标题
     * @param columns 字段二维数组
     * @param entityclass 实体类
     * @throws Exception
     */
    public static <T> void export(HttpServletRequest request, HttpServletResponse response, List<T> datalist,
            String titledata, String[][] columns, Class<T> entityclass) throws Exception {
        // 创建excel工作簿
        Workbook wb = new HSSFWorkbook();
        // 获取当前日期
        SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMdd");
        Date currentTime = new java.util.Date();

        HSSFCellStyle style = (HSSFCellStyle) wb.createCellStyle();
        // 生成一个字体
        HSSFFont font = (HSSFFont) wb.createFont();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        // 生成一个字体
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        for (int k = 0; k < (datalist.size() / constant) + 1; k++) {
            int tempcount = 0;
            Sheet sheet = wb.createSheet(titledata + formatter.format(currentTime) + "(" + k + ")");
            // 创建创建表头
            Row row = sheet.createRow(0);
            // 设置第一行表头
            for (int i = 0; i < columns.length; i++) {
                HSSFCell cell = (HSSFCell) row.createCell(i);
                cell.setCellStyle(style);
                cell.setCellValue(columns[i][0]);
                sheet.setColumnWidth(i, columns[i][0].getBytes().length * 800);
            }
            String cellValue = "";
            // 填充内容
            for (int i = (k * constant); i < ((k + 1) * constant > datalist.size() ? datalist.size() : (k + 1)
                    * constant); i++) {
                tempcount++;
                Object clazz = (Object) datalist.get(i);
                row = sheet.createRow(tempcount);
                for (int j = 0; j < columns.length; j++) {
                    Method method = entityclass.getMethod("get" + columns[j][1]);
                    if (method.invoke(clazz) == null) {
                        row.createCell(j).setCellValue("");
                        // sheet.setColumnWidth(j, 3000);
                    } else {
                        Class<?> r = method.getReturnType();
                        if ("java.lang.String".equals(r.getName())) {
                            cellValue = (String) method.invoke(clazz);
                        }
                        if ("java.lang.Integer".equals(r.getName())) {
                            cellValue = ((Integer) method.invoke(clazz)).toString();
                        }
                        if ("java.math.BigDecimal".equals(r.getName())) {
                            cellValue = ((BigDecimal) method.invoke(clazz)).toString();
                        }
                        if ("java.util.Date".equals(r.getName())) {
                            cellValue = DateUtil.dateToString((Date) method.invoke(clazz), "yyyy-MM-dd HH:mm:ss");
                        }
                        row.createCell(j).setCellValue(cellValue);
                        // sheet.setColumnWidth(j, cellValue.getBytes().length * 512);
                    }
                }
            }
        }
        String fileName = titledata + formatter.format(currentTime) + ".xls";

        String filePath = AppHelper.getServerHome() + "/projects/" + AppHelper.APP_NAME + "/"
                + formatter.format(currentTime);

        // 若无该文件夹自动创建
        File fp = new File(filePath);

        if (!fp.exists()) {
            fp.mkdirs();
        }

        String path = filePath + "/" + fileName;

        FileOutputStream fileOut = new FileOutputStream(path);
        // 把上面创建的工作簿输出到文件中
        wb.write(fileOut);
        // 关闭输出流
        fileOut.close();

        File file = new File(path);

        InputStream in = new FileInputStream(file);
        OutputStream os = response.getOutputStream();
        setFileDownloadHeader(request, response, file.getName());
        response.addHeader("Content-Length", file.length() + "");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/vnd.ms-excel");
        int data = 0;
        while ((data = in.read()) != -1) {
            os.write(data);
        }
        os.close();
        in.close();
        wb.close();
        file.delete();

    }

    /**
     * @Description:设置文件头
     * @param request
     * @param response
     * @param fileName
     */
    public static void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) {
        final String userAgent = request.getHeader("USER-AGENT");
        try {
            String finalFileName = null;
            if (StringUtils.contains(userAgent, "MSIE")) {// IE浏览器
                finalFileName = URLEncoder.encode(fileName, "UTF8");
            } else if (StringUtils.contains(userAgent, "Mozilla")) {// google,火狐浏览器
                finalFileName = new String(fileName.getBytes(), "ISO8859-1");
            } else {
                finalFileName = URLEncoder.encode(fileName, "UTF8");// 其他浏览器
            }
            response.setHeader("Content-Disposition", "attachment; filename=\"" + finalFileName + "\"");// 这里设置一下让浏览器弹出下载提示框，而不是直接在浏览器中打开
        } catch (UnsupportedEncodingException e) {
            throw new AppException(e);
        }
    }

    /**
     * @Description:导出消费Excel，特殊处理
     * @param request
     * @param response
     * @param datalist 数据集合
     * @param titledata excel标题
     * @param columns 字段二维数组
     * @param entityclass 实体类
     * @throws Exception
     */
    public static <T> void exportConsumData(HttpServletRequest request, HttpServletResponse response,
            List<Map<String, String>> datalist, String titledata, List<Map<String, String>> titles,
            Map<String, String> amounts) throws Exception {
        // 创建excel工作簿
        Workbook wb = new HSSFWorkbook();
        // 获取当前日期
        SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMdd");
        // 计算合计值
        Map<String, Float> sumamount = new LinkedHashMap<String, Float>();
        Date currentTime = new java.util.Date();

        HSSFCellStyle style = (HSSFCellStyle) wb.createCellStyle();
        // 生成一个字体
        HSSFFont font = (HSSFFont) wb.createFont();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        // 生成一个字体
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        for (int k = 0; k < (datalist.size() / constant) + 1; k++) {
            int tempcount = 0;
            Sheet sheet = wb.createSheet(titledata + formatter.format(currentTime) + "(" + k + ")");
            // 创建创建表头
            Row row = sheet.createRow(0);
            // 设置第一行表头
            for (int i = 0; i < titles.size(); i++) {
                HSSFCell cell = (HSSFCell) row.createCell(i);
                cell.setCellStyle(style);
                cell.setCellValue(titles.get(i).get("name"));
                sheet.setColumnWidth(i, titles.get(i).get("name").getBytes().length * 800);
            }
            // 填充内容
            for (int i = (k * constant); i < ((k + 1) * constant > datalist.size() ? datalist.size() : (k + 1)
                    * constant); i++) {
                tempcount++;
                Map<String, String> map = datalist.get(i);
                row = sheet.createRow(tempcount);
                for (int j = 0; j < titles.size(); j++) {
                    if (j > 0) {
                        Float old = sumamount.get(titles.get(j).get("code"));
                        if (old == null) {
                            old = 0f;
                        }
                        String ne = map.get(titles.get(j).get("code"));
                        Float ne_value = 0f;
                        if (ne == null || ne == "" || "null".equals(ne)) {
                            ne_value = 0f;
                        } else {
                            ne_value = Float.parseFloat(ne);
                        }
                        sumamount.put(titles.get(j).get("code"), old + ne_value);
                    }
                    row.createCell(j).setCellValue(map.get(titles.get(j).get("code")));
                    // sheet.setColumnWidth(j, cellValue.getBytes().length * 512);
                }
            }
            // 计算合计行和余额行
            for (int j = 0; j < 2; j++) {
                row = sheet.createRow(tempcount + j + 1);
                if (j == 0) {// 合计
                    row.createCell(0).setCellValue("总计");
                    for (int i = 1; i < titles.size(); i++) {
                        Float f = sumamount.get(titles.get(i).get("code"));
                        if (f == null) {
                            row.createCell(i).setCellValue("");
                        } else {
                            BigDecimal b = new BigDecimal(f);
                            float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                            row.createCell(i).setCellValue(f1+"");
                        }
                    }
                }
                if (j == 1) {// 余额
                    row.createCell(0).setCellValue("余额");
                    for (int i = 1; i < titles.size(); i++) {
                        Float f = Float.parseFloat(amounts.get(titles.get(i).get("code")));
                        if (f == null) {
                            row.createCell(i).setCellValue("");
                        } else {
                            BigDecimal b = new BigDecimal(f);
                            float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                            row.createCell(i).setCellValue(f1+"");
                        }
                        // row.createCell(i + 1).setCellValue(amounts.get(titles.get(i).get("code")));
                    }
                }
            }
        }
        String fileName = titledata + formatter.format(currentTime) + ".xls";

        String filePath = AppHelper.getServerHome() + "/projects/" + AppHelper.APP_NAME + "/"
                + formatter.format(currentTime);

        // 若无该文件夹自动创建
        File fp = new File(filePath);

        if (!fp.exists()) {
            fp.mkdirs();
        }

        String path = filePath + "/" + fileName;

        FileOutputStream fileOut = new FileOutputStream(path);
        // 把上面创建的工作簿输出到文件中
        wb.write(fileOut);
        // 关闭输出流
        fileOut.close();

        File file = new File(path);

        InputStream in = new FileInputStream(file);
        OutputStream os = response.getOutputStream();
        setFileDownloadHeader(request, response, file.getName());
        response.addHeader("Content-Length", file.length() + "");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/vnd.ms-excel");
        int data = 0;
        while ((data = in.read()) != -1) {
            os.write(data);
        }
        os.close();
        in.close();
        wb.close();
        file.delete();

    }
}
