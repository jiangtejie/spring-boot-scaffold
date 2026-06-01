package com.example.scaffold.common.geo;

import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.lionsoul.ip2region.xdb.Version;

/**
 * IP 归属地查询 — 基于 Ip2region 离线数据库。
 *
 * <pre>{@code
 * String region = IpRegionUtils.search("8.8.8.8");
 * // → "美国|0|0|谷歌|0"
 * }</pre>
 *
 * <p>首次调用时从 classpath 加载 xdb 文件，后续直接使用缓存。
 * <p>使用前请将 ip2region.xdb 放入 {@code resources/ip2region/} 目录。
 */
@Slf4j
public final class IpRegionUtils {

    private static volatile Searcher searcher;

    private IpRegionUtils() {}

    public static String search(String ip) {
        try {
            return getSearcher().search(ip);
        } catch (Exception e) {
            log.warn("IP 查询失败: {} → {}", ip, e.getMessage());
            return "未知";
        }
    }

    private static Searcher getSearcher() throws Exception {
        if (searcher == null) {
            synchronized (IpRegionUtils.class) {
                if (searcher == null) {
                    var url = IpRegionUtils.class.getClassLoader()
                            .getResource("ip2region/ip2region.xdb");
                    if (url == null) {
                        log.warn("ip2region.xdb 未找到，下载: https://github.com/lionsoul2014/ip2region");
                        throw new IllegalStateException("ip2region.xdb 缺失");
                    }
                    try (var in = url.openStream()) {
                        var buffer = Searcher.loadContentFromInputStream(in);
                        searcher = Searcher.newWithBuffer(Version.IPv4, buffer);
                    }
                }
            }
        }
        return searcher;
    }
}
