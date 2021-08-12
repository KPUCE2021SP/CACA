package com.example.myapplication.Mypage;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010 \u001a\u00020!H\u0002J\u001a\u0010\"\u001a\u00020!2\b\u0010#\u001a\u0004\u0018\u00010\u00162\u0006\u0010$\u001a\u00020%H\u0002J\u0010\u0010&\u001a\u00020\u00042\u0006\u0010\'\u001a\u00020\u0004H\u0002J\"\u0010(\u001a\u00020!2\u0006\u0010)\u001a\u00020\u00042\u0006\u0010*\u001a\u00020\u00042\b\u0010+\u001a\u0004\u0018\u00010,H\u0014J\u0012\u0010-\u001a\u00020!2\b\u0010.\u001a\u0004\u0018\u00010/H\u0014J\u0018\u00100\u001a\u0002012\u0006\u00102\u001a\u0002012\u0006\u00103\u001a\u00020\u0004H\u0002J\u0018\u00104\u001a\u00020!2\u0006\u00105\u001a\u0002062\u0006\u00107\u001a\u00020\u0018H\u0002J\b\u00108\u001a\u00020!H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001a\u0010\u000b\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0017\u001a\u00020\u0018X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u001a\u0010\u001d\u001a\u00020\u0018X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u001a\"\u0004\b\u001f\u0010\u001c\u00a8\u00069"}, d2 = {"Lcom/example/myapplication/Mypage/MypageActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "PICK_IMAGE_REQUEST", "", "fbAuth", "Lcom/google/firebase/auth/FirebaseAuth;", "getFbAuth", "()Lcom/google/firebase/auth/FirebaseAuth;", "setFbAuth", "(Lcom/google/firebase/auth/FirebaseAuth;)V", "fbFire", "Lcom/google/firebase/firestore/FirebaseFirestore;", "getFbFire", "()Lcom/google/firebase/firestore/FirebaseFirestore;", "setFbFire", "(Lcom/google/firebase/firestore/FirebaseFirestore;)V", "filePath", "Landroid/net/Uri;", "firebaseStorage", "Lcom/google/firebase/storage/FirebaseStorage;", "storageReference", "Lcom/google/firebase/storage/StorageReference;", "uemail", "", "getUemail", "()Ljava/lang/String;", "setUemail", "(Ljava/lang/String;)V", "uid", "getUid", "setUid", "ImagePicker", "", "displayImageRef", "imageRef", "view", "Landroid/widget/ImageView;", "exifOrientationToDegress", "exifOrientation", "onActivityResult", "requestCode", "resultCode", "data", "Landroid/content/Intent;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "rotate", "Landroid/graphics/Bitmap;", "bitmap", "degree", "setContent", "layout", "Landroid/widget/LinearLayout;", "content", "uploadProfile", "app_debug"})
public final class MypageActivity extends androidx.appcompat.app.AppCompatActivity {
    @org.jetbrains.annotations.NotNull()
    private com.google.firebase.auth.FirebaseAuth fbAuth;
    @org.jetbrains.annotations.NotNull()
    private com.google.firebase.firestore.FirebaseFirestore fbFire;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String uid;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String uemail;
    private final int PICK_IMAGE_REQUEST = 1;
    private android.net.Uri filePath;
    private com.google.firebase.storage.FirebaseStorage firebaseStorage;
    private com.google.firebase.storage.StorageReference storageReference;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.NotNull()
    public final com.google.firebase.auth.FirebaseAuth getFbAuth() {
        return null;
    }
    
    public final void setFbAuth(@org.jetbrains.annotations.NotNull()
    com.google.firebase.auth.FirebaseAuth p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.google.firebase.firestore.FirebaseFirestore getFbFire() {
        return null;
    }
    
    public final void setFbFire(@org.jetbrains.annotations.NotNull()
    com.google.firebase.firestore.FirebaseFirestore p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getUid() {
        return null;
    }
    
    public final void setUid(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getUemail() {
        return null;
    }
    
    public final void setUemail(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void displayImageRef(com.google.firebase.storage.StorageReference imageRef, android.widget.ImageView view) {
    }
    
    private final void ImagePicker() {
    }
    
    @java.lang.Override()
    protected void onActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.Nullable()
    android.content.Intent data) {
    }
    
    private final void uploadProfile() {
    }
    
    private final int exifOrientationToDegress(int exifOrientation) {
        return 0;
    }
    
    private final android.graphics.Bitmap rotate(android.graphics.Bitmap bitmap, int degree) {
        return null;
    }
    
    private final void setContent(android.widget.LinearLayout layout, java.lang.String content) {
    }
    
    public MypageActivity() {
        super();
    }
}