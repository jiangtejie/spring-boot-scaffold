package com.example.scaffold.common.page;

import com.example.scaffold.common.converter.DtoConverter;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.BaseMapper;
import java.util.List;

/**
 * MyBatis-Flex 分页助手 — 一行代码完成 Service 层分页。
 *
 * <pre>{@code
 * // Service 层使用示例
 * QueryWrapper qw = QueryWrapper.create().where("status = ?", 1);
 * PageResult<UserDto> page = PageHelper.paginate(mapper, query, qw, UserDto::from);
 * }</pre>
 */
public final class PageHelper {

    private PageHelper() {}

    /**
     * 分页查询并转换为 DTO。
     *
     * @param mapper    MyBatis-Flex BaseMapper
     * @param query     分页参数
     * @param qw        查询条件
     * @param converter 实体 → DTO 转换函数
     * @param <E>       实体类型
     * @param <D>       DTO 类型
     */
    public static <E, D> PageResult<D> paginate(
            BaseMapper<E> mapper,
            PageQuery query,
            QueryWrapper qw,
            java.util.function.Function<E, D> converter) {

        Page<E> page = new Page<>(query.page(), query.size());
        mapper.paginate(page, qw);

        List<D> dtoList = DtoConverter.convertList(page.getRecords(), converter);
        return PageResults.of(dtoList, query, page.getTotalRow());
    }

    /**
     * 分页查询（不转换，直接返回实体）。
     */
    public static <E> PageResult<E> paginate(
            BaseMapper<E> mapper,
            PageQuery query,
            QueryWrapper qw) {

        Page<E> page = new Page<>(query.page(), query.size());
        mapper.paginate(page, qw);

        return PageResults.of(page.getRecords(), query, page.getTotalRow());
    }
}
