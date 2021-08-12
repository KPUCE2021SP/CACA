package com.example.myapplication.LoginRegister;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 \u00192\u00020\u0001:\u0002\u0019\u001aB\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0013\u001a\u00020\u00142\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u0010\u001a\u00020\tH\u0002J\u0012\u0010\u0015\u001a\u00020\u00142\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017H\u0014J\b\u0010\u0018\u001a\u00020\u0014H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\u00020\r8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\u000eR\u0014\u0010\u000f\u001a\u00020\r8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u000eR\u000e\u0010\u0010\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2 = {"Lcom/example/myapplication/LoginRegister/LoginActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "editTextEmail", "Landroid/widget/EditText;", "editTextPassword", "editor", "Landroid/content/SharedPreferences$Editor;", "email", "", "firebaseAuth", "Lcom/google/firebase/auth/FirebaseAuth;", "isValidEmail", "", "()Z", "isValidPasswd", "password", "sharedPreferences", "Landroid/content/SharedPreferences;", "loginUser", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "signIn", "Companion", "MySharedPreferences", "app_debug"})
public final class LoginActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.google.firebase.auth.FirebaseAuth firebaseAuth;
    private android.widget.EditText editTextEmail;
    private android.widget.EditText editTextPassword;
    private java.lang.String email = "";
    private java.lang.String password = "";
    private android.content.SharedPreferences sharedPreferences;
    private android.content.SharedPreferences.Editor editor;
    private static final java.util.regex.Pattern PASSWORD_PATTERN = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.myapplication.LoginRegister.LoginActivity.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void signIn() {
    }
    
    private final boolean isValidEmail() {
        return false;
    }
    
    private final boolean isValidPasswd() {
        return false;
    }
    
    private final void loginUser(java.lang.String email, java.lang.String password) {
    }
    
    public LoginActivity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\t\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\n\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\bJ\u0016\u0010\u000b\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\u0004J\u0016\u0010\r\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/example/myapplication/LoginRegister/LoginActivity$MySharedPreferences;", "", "()V", "MY_ACCOUNT", "", "clearUser", "", "context", "Landroid/content/Context;", "getUserId", "getUserPass", "setUserId", "input", "setUserPass", "app_debug"})
    public static final class MySharedPreferences {
        private static final java.lang.String MY_ACCOUNT = "account";
        @org.jetbrains.annotations.NotNull()
        public static final com.example.myapplication.LoginRegister.LoginActivity.MySharedPreferences INSTANCE = null;
        
        public final void setUserId(@org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        java.lang.String input) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getUserId(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
        
        public final void setUserPass(@org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        java.lang.String input) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getUserPass(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
        
        public final void clearUser(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
        }
        
        private MySharedPreferences() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0019\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\b"}, d2 = {"Lcom/example/myapplication/LoginRegister/LoginActivity$Companion;", "", "()V", "PASSWORD_PATTERN", "Ljava/util/regex/Pattern;", "kotlin.jvm.PlatformType", "getPASSWORD_PATTERN", "()Ljava/util/regex/Pattern;", "app_debug"})
    public static final class Companion {
        
        public final java.util.regex.Pattern getPASSWORD_PATTERN() {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}