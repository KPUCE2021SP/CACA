package com.example.myapplication.Home_Board

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import com.example.myapplication.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.activity_album.*
import kotlinx.android.synthetic.main.activity_album_content.*
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.image

class AlbumActivity : AppCompatActivity() {
    val db: FirebaseFirestore = Firebase.firestore
    var FamilyName2 : String = ""
    var albumYear2 : String = ""




    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)

        val FamilyName = intent.getStringExtra("FamilyName")
        val albumYear = intent.getStringExtra("albumYear")
        FamilyName2 = FamilyName.toString()
        var storage: FirebaseStorage = Firebase.storage


        //가족사진 가져오기

        val imageName = "gs://cacafirebase-554ac.appspot.com/Family_Image/" + FamilyName.toString()
        val imageRef1 = storage.getReferenceFromUrl(imageName)
        imageRef1?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
            val profilebmp = BitmapFactory.decodeByteArray(it, 0, it.size)
            FamilyProfileView.setImageBitmap(profilebmp)
        }?.addOnFailureListener {
            Toast.makeText(
                    this,
                    "image downloade failed",
                    Toast.LENGTH_SHORT
            ).show()
        }

        albumTV.setText(albumYear.toString())       //년도 표시하기

        var mutableAlbumList: MutableList<String> = mutableListOf("a")
        mutableAlbumList.clear()

        db.collection("Chats").document(FamilyName.toString()).collection("BOARD")
                .get()
                .addOnSuccessListener { document2 ->
                    for (document_acc in document2) {       //해당 년도 가져오기
                        if(document_acc.id.contains(albumYear.toString())){
                            mutableAlbumList.add(document_acc.id)
                        }

                    }

                    Log.d("mutableAlbumList", mutableAlbumList.toString())


                    for (i : Int in 0..(mutableAlbumList.size - 1) step 3) {

                        val layoutInflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                        val containView = layoutInflater.inflate(
                            R.layout.activity_album_content,
                                null
                        )
                        val ContentView = containView as View

                        var album_Iv1 = ContentView.findViewById(R.id.albumView1) as ImageView
                        var album_Iv2 = ContentView.findViewById(R.id.albumView2) as ImageView
                        var album_Iv3 = ContentView.findViewById(R.id.albumView3) as ImageView

//                        album_Iv1.image = R.drawable.birth.toDrawable()
//                        album_Iv2.image = R.drawable.birth.toDrawable()
//                        album_Iv3.image = R.drawable.birth.toDrawable()


                        var imageName =
                                "gs://cacafirebase-554ac.appspot.com/Family_Board/" + FamilyName + "_" + mutableAlbumList[i]
                        Log.d("familyimageName", imageName)
                        val storage = Firebase.storage
                        var familyImgRef = storage.getReferenceFromUrl(imageName)
                        familyImgRef?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                            val profilebmp = BitmapFactory.decodeByteArray(it, 0, it.size)
                            album_Iv1.setImageBitmap(profilebmp)
                        }
                                .addOnFailureListener {
                                    album_Iv1.image = R.drawable.birth.toDrawable()
                                }
//

                        if((mutableAlbumList.size - 1)%3 < 2){ // 0, 1, 2

                            var imageName1 =
                                    "gs://cacafirebase-554ac.appspot.com/Family_Board/" + FamilyName + "_" + mutableAlbumList[i+1]
                            Log.d("familyimageName", imageName1)
                            var familyImgRef2 = storage.getReferenceFromUrl(imageName1)
                            familyImgRef2?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                                val profilebmp = BitmapFactory.decodeByteArray(it, 0, it.size)
                                album_Iv2.setImageBitmap(profilebmp)
                            }.addOnFailureListener {
                                album_Iv2.image = R.drawable.birth.toDrawable()
                            }

                        }
//
                        if((mutableAlbumList.size - 1)%3 < 1) {
                            Log.d("familyimageName", i.toString())
                            var imageName2 =
                                    "gs://cacafirebase-554ac.appspot.com/Family_Board/" + FamilyName + "_" + mutableAlbumList[i+2]
                            Log.d("familyimageName", imageName2)
                            var familyImgRef3 = storage.getReferenceFromUrl(imageName2)
                            familyImgRef3?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                                val profilebmp = BitmapFactory.decodeByteArray(it, 0, it.size)
                                album_Iv3.setImageBitmap(profilebmp)
                            }.addOnFailureListener {
                                album_Iv3.image = R.drawable.birth.toDrawable()
                            }
                        }


                        family_contain.addView(containView)
                    }


                }


    }
}