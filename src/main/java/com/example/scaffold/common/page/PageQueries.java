package com.example.scaffold.common.page;

import com.example.scaffold.common.error.AppBizException;
import com.example.scaffold.common.error.AppErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * 从查询参数解析 {@link PageQuery}。约定：{@code page} 从 0 开始；{@code size} 为每页条数；{@code sort} 可重复。
 */
public final class PageQueries {

    private static final String P_PAGE = "page";
    private static final String P_SIZE = "size";
    private static final String P_SORT = "sort";

    private PageQueries() {}

    public static PageQuery from(HttpServletRequest request, int defaultSize, int maxSize) {
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        for (var e : request.getParameterMap().entrySet()) {
            for (String v : e.getValue()) {
                map.add(e.getKey(), v);
            }
        }
        return fromQueryParams(map, defaultSize, maxSize);
    }

    public static PageQuery fromQueryParams(MultiValueMap<String, String> query, int defaultSize, int maxSize) {
        if (defaultSize < 1) defaultSize = 20;
        if (maxSize < 1) maxSize = 100;
        defaultSize = Math.min(defaultSize, maxSize);
        int page = parsePage(query);
        int size = parseSize(query, defaultSize, maxSize);
        Sort sort = parseSort(query.get(P_SORT));
        return new PageQuery(page, size, sort);
    }

    private static int parsePage(MultiValueMap<String, String> query) {
        String raw = first(query, P_PAGE);
        if (raw == null || raw.isBlank()) return 0;
        try {
            int p = Integer.parseInt(raw.trim());
            if (p < 0) throw new AppBizException(AppErrorCode.PARAM_INVALID, "参数 page 不能为负");
            return p;
        } catch (NumberFormatException e) {
            throw new AppBizException(AppErrorCode.PARAM_FORMAT_ERROR, "参数 page 不是合法整数");
        }
    }

    private static int parseSize(MultiValueMap<String, String> query, int defaultSize, int maxSize) {
        String raw = first(query, P_SIZE);
        if (raw == null || raw.isBlank()) return defaultSize;
        try {
            int s = Integer.parseInt(raw.trim());
            if (s < 1) throw new AppBizException(AppErrorCode.PARAM_INVALID, "参数 size 必须 >= 1");
            return Math.min(s, maxSize);
        } catch (NumberFormatException e) {
            throw new AppBizException(AppErrorCode.PARAM_FORMAT_ERROR, "参数 size 不是合法整数");
        }
    }

    private static String first(MultiValueMap<String, String> query, String name) {
        var list = query.get(name);
        return (list == null || list.isEmpty()) ? null : list.getFirst();
    }

    static Sort parseSort(List<String> sortParams) {
        if (sortParams == null || sortParams.isEmpty()) return Sort.unsorted();
        List<Order> orders = new ArrayList<>();
        for (String item : sortParams) {
            if (item == null || item.isBlank()) continue;
            String[] parts = item.split(",");
            String field = parts[0].trim();
            if (field.isEmpty()) continue;
            Direction direction = Direction.ASC;
            if (parts.length > 1) {
                String d = parts[1].trim().toLowerCase();
                if ("desc".equals(d)) direction = Direction.DESC;
                else if (!"asc".equals(d) && !d.isEmpty())
                    throw new AppBizException(AppErrorCode.PARAM_FORMAT_ERROR, "sort 方向仅支持 asc 或 desc: " + item);
            }
            orders.add(new Order(direction, field));
        }
        return orders.isEmpty() ? Sort.unsorted() : Sort.by(orders);
    }
}
