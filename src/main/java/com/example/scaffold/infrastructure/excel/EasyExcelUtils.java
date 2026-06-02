package com.example.scaffold.infrastructure.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * EasyExcel 导入导出工具。
 *
 * <pre>{@code
 * // 导出
 * EasyExcelUtils.export(response, "用户列表", "用户", UserExcelDto.class, userList);
 *
 * // 导入
 * List<UserExcelDto> list = EasyExcelUtils.read(file, UserExcelDto.class);
 * }</pre>
 */
public final class EasyExcelUtils {

    private EasyExcelUtils() {}

    /** Web 导出 */
    public static <T> void export(HttpServletResponse response, String fileName,
                                   String sheetName, Class<T> clazz, List<T> data) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String encoded = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename=" + encoded + ".xlsx");

        EasyExcel.write(response.getOutputStream(), clazz)
                .sheet(sheetName)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .doWrite(data);
    }

    /** 从文件路径读取 */
    public static <T> List<T> read(String filePath, Class<T> clazz) {
        return EasyExcel.read(filePath).head(clazz).sheet().doReadSync();
    }

    /** 带监听器读取（大数据量） */
    public static <T> void readWithListener(String filePath, Class<T> clazz, BaseReadListener<T> listener) {
        EasyExcel.read(filePath, clazz, listener).sheet().doRead();
    }
}
