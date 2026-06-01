package com.example.scaffold.common.mapper;

import com.example.scaffold.common.page.PageResult;
import java.util.List;
import org.mapstruct.MappingConstants;

/**
 * MapStruct 实体映射基接口。
 *
 * <p>业务 Mapper 继承此接口即可获得实体 ↔ DTO 转换能力：
 *
 * <pre>{@code
 * @Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
 * public interface UserMapper extends EntityMapper<User, UserDto> {}
 * }</pre>
 *
 * @param <E> 实体类型
 * @param <D> DTO 类型
 */
public interface EntityMapper<E, D> {

    D toDto(E entity);

    E toEntity(D dto);

    List<D> toDtoList(List<E> entities);

    List<E> toEntityList(List<D> dtos);

    /**
     * 分页转换 — 覆写此方法以自动获得 {@link PageResult} 转换。
     */
    default PageResult<D> toDtoPage(PageResult<E> page) {
        if (page == null) return null;
        return new PageResult<>(
                toDtoList(page.content()),
                page.total(), page.page(), page.size(),
                page.totalPages(), page.hasNext(), page.hasPrevious());
    }
}
