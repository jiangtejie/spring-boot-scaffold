package com.example.scaffold.module.system;

public final class SystemDto {

    private SystemDto() {}

    public record PingResponse(String applicationName, String status) {}
}
