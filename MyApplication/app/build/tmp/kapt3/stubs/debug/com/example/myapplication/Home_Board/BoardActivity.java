package com.example.myapplication.Home_Board;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010!\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u001d\u001a\u00020\u001e2\b\u0010\u001f\u001a\u0004\u0018\u00010 H\u0014R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR \u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\n0\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R \u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\n0\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0012\"\u0004\b\u0017\u0010\u0014R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u001a\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\f\"\u0004\b\u001c\u0010\u000e\u00a8\u0006!"}, d2 = {"Lcom/example/myapplication/Home_Board/BoardActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "fbAuth", "Lcom/google/firebase/auth/FirebaseAuth;", "getFbAuth", "()Lcom/google/firebase/auth/FirebaseAuth;", "setFbAuth", "(Lcom/google/firebase/auth/FirebaseAuth;)V", "mentionPerson", "", "getMentionPerson", "()Ljava/lang/String;", "setMentionPerson", "(Ljava/lang/String;)V", "mutableList", "", "getMutableList", "()Ljava/util/List;", "setMutableList", "(Ljava/util/List;)V", "mutableuidList", "getMutableuidList", "setMutableuidList", "storageReference", "Lcom/google/firebase/storage/StorageReference;", "uid", "getUid", "setUid", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "app_debug"})
public final class BoardActivity extends androidx.appcompat.app.AppCompatActivity {
    @org.jetbrains.annotations.NotNull()
    private com.google.firebase.auth.FirebaseAuth fbAuth;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<java.lang.String> mutableList;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String uid;
    private com.google.firebase.storage.StorageReference storageReference;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<java.lang.String> mutableuidList;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String mentionPerson = "";
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.NotNull()
    public final com.google.firebase.auth.FirebaseAuth getFbAuth() {
        return null;
    }
    
    public final void setFbAuth(@org.jetbrains.annotations.NotNull()
    com.google.firebase.auth.FirebaseAuth p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getMutableList() {
        return null;
    }
    
    public final void setMutableList(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getUid() {
        return null;
    }
    
    public final void setUid(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getMutableuidList() {
        return null;
    }
    
    public final void setMutableuidList(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getMentionPerson() {
        return null;
    }
    
    public final void setMentionPerson(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    public BoardActivity() {
        super();
    }
}