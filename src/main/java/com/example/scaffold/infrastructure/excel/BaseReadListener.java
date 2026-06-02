package com.example.scaffold.infrastructure.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;

/**
 * EasyExcel 读取监听器基类 — 分批处理大数据量 Excel。
 *
 * <pre>{@code
 * EasyExcelUtils.readWithListener("data.xlsx", UserDto.class,
 *     new BaseReadListener<>(batch -> userService.saveBatch(batch), 500));
 * }</pre>
 */
@Slf4j
public class BaseReadListener<T> extends AnalysisEventListener<T> {

    private final List<T> batch = new ArrayList<>();
    private final Consumer<List<T>> batchConsumer;
    private final int batchSize;

    public BaseReadListener(Consumer<List<T>> batchConsumer, int batchSize) {
        this.batchConsumer = batchConsumer;
        this.batchSize = batchSize;
    }

    public BaseReadListener(Consumer<List<T>> batchConsumer) {
        this(batchConsumer, 1000);
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        batch.add(data);
        if (batch.size() >= batchSize) {
            batchConsumer.accept(new ArrayList<>(batch));
            batch.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (!batch.isEmpty()) {
            batchConsumer.accept(new ArrayList<>(batch));
        }
        log.info("Excel 读取完成，共 {} 行", context.readRowHolder().getRowIndex());
    }
}
