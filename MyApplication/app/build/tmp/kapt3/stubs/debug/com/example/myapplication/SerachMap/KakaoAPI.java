package com.example.myapplication.SerachMap;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\bf\u0018\u00002\u00020\u0001J@\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u0007\u001a\u00020\u00062\b\b\u0001\u0010\b\u001a\u00020\u00062\b\b\u0001\u0010\t\u001a\u00020\u00062\b\b\u0001\u0010\n\u001a\u00020\u000bH\'\u00a8\u0006\f"}, d2 = {"Lcom/example/myapplication/SerachMap/KakaoAPI;", "", "getSearchKeyword", "Lretrofit2/Call;", "Lcom/example/myapplication/SerachMap/ResultSearchKeyword;", "key", "", "query", "x", "y", "radius", "", "app_debug"})
public abstract interface KakaoAPI {
    
    @org.jetbrains.annotations.NotNull()
    @retrofit2.http.GET(value = "v2/local/search/keyword.json")
    public abstract retrofit2.Call<com.example.myapplication.SerachMap.ResultSearchKeyword> getSearchKeyword(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Header(value = "Authorization")
    java.lang.String key, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "query")
    java.lang.String query, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "x")
    java.lang.String x, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "y")
    java.lang.String y, @retrofit2.http.Query(value = "radius")
    int radius);
}