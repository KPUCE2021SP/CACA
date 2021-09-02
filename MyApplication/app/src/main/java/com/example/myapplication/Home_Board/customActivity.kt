package com.example.myapplication.Home_Board

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_custom.*
import kotlinx.android.synthetic.main.activity_home.*
import android.graphics.BitmapFactory

import android.net.Uri
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.mypage_activity.*


class customActivity: Activity() {

    var fbAuth = FirebaseAuth.getInstance() // 로그인
    var fbFire = FirebaseFirestore.getInstance()
    var uid = fbAuth?.uid.toString() // uid

    private val PICK_IMAGE_REQUEST = 1
    private var filePath: Uri? = null
    private var firebaseStorage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    val db: FirebaseFirestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom)

        val FamilyName = intent.getStringExtra("FamilyName")

        val layout1: (HorizontalScrollView) =findViewById(R.id.scrollview_color);
        val layout2: (HorizontalScrollView) =findViewById(R.id.scrollview_deco);
        val layout3: (HorizontalScrollView) =findViewById(R.id.scrollview_eye);


        val color = mutableListOf<String>("standing_1.png", "standing_2.png", "standing_3.png", "standing_4.png", "standing_5.png",
            "standing_6.png", "standing_7.png", "standing_8.png", "standing_9.png", "standing_10.png")
        val acc = mutableListOf<String>("flushing.png", "beret.png", "glass.png", "crown.png", "flower.png", "baby.png", "sister.png")
        val emotion = mutableListOf<String>("angry.png", "hungry.png", "sadness.png", "smile.png")
        val eyes = mutableListOf<String>("normaleye.png", "blackeye.png",)
        val dancing = mutableListOf<String>("dancing_1.png", "dancing_2.png", "dancing_3.png", "dancing_4.png",
            "dancing_5.png", "dancing_6.png", "dancing_7.png", "dancing_8.png", "dancing_9.png", "dancing_10.png")





        btn_color.setOnClickListener {
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.INVISIBLE);
            layout3.setVisibility(View.INVISIBLE);
    }
        btn_deco.setOnClickListener {
            layout1.setVisibility(View.INVISIBLE);
            layout2.setVisibility(View.VISIBLE);
            layout3.setVisibility(View.INVISIBLE);
        }

        btn_face.setOnClickListener {
            layout1.setVisibility(View.INVISIBLE);
            layout2.setVisibility(View.INVISIBLE);
            layout3.setVisibility(View.VISIBLE);
        }

        val bodyframe: FrameLayout = findViewById(R.id.body)
        //color 가져오기
        var body_color : String = ""    //DB에 저장할 바디 이름
        var custom_acc : String = ""    //DB에 저장할 악세사리
        var custom_eye : String = ""
        var custom_emotion : String = ""    //감정
        var custom_dancing : String = ""   //춤추기
        val storage = Firebase.storage




        customSaveBtn.setOnClickListener {  //저장하기
            val custom_format = hashMapOf(
                    "bodyColor" to body_color,
                    "customAcc" to custom_acc,
                    "customEye" to custom_eye,
                    "dancing" to custom_dancing,
            )
            db.collection("Chats").document(FamilyName.toString()).collection("CUSTOM")
                    .document(FamilyName.toString())
                    .set(custom_format)

            finish()
        }

        //--------------------기본 화면-------------------

        body_color = color[0].toString()
        val bodyName = "gs://cacafirebase-554ac.appspot.com/custom_image/color/" + body_color
        val customRef_body = storage.getReferenceFromUrl(bodyName)
        customRef_body?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
            val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
            body_Iv.setImageBitmap(customRef)
        }?.addOnFailureListener {
//            Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
        }

        custom_acc = acc[0].toString()
        val accName = "gs://cacafirebase-554ac.appspot.com/custom_image/acc/" + custom_acc
        val customRef_acc = storage.getReferenceFromUrl(accName)
        customRef_acc?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
            val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
            acc_Iv.setImageBitmap(customRef)
        }?.addOnFailureListener {
//            Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
        }


        custom_eye = eyes[0].toString()
        val eyeName = "gs://cacafirebase-554ac.appspot.com/custom_image/acc/" + custom_eye
        val customRef_eye = storage.getReferenceFromUrl(eyeName)
        customRef_eye?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
            val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
            eye_Iv.setImageBitmap(customRef)
        }?.addOnFailureListener {
//            Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
        }




        //---------------body color --------------------------

        ImageButton_color1.setOnClickListener {
            body_color = color[0].toString()
            custom_dancing = dancing[0].toString()
            val bodyName = "gs://cacafirebase-554ac.appspot.com/custom_image/color/" + body_color
            val customRef_body = storage.getReferenceFromUrl(bodyName)
            customRef_body?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                body_Iv.setImageBitmap(customRef)
            }?.addOnFailureListener {
//                Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
            }
//            bodyframe.foreground = ContextCompat.getDrawable(this, R.drawable.standing_1)

        }

        ImageButton_color2.setOnClickListener {
            body_color = color[1].toString()
            custom_dancing = dancing[1].toString()
            val bodyName = "gs://cacafirebase-554ac.appspot.com/custom_image/color/" + body_color
            val customRef_body = storage.getReferenceFromUrl(bodyName)
            customRef_body?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                body_Iv.setImageBitmap(customRef)
            }?.addOnFailureListener {
//                Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
            }
//            bodyframe.foreground = ContextCompat.getDrawable(this, R.drawable. standing_2)

        }

        ImageButton_color3.setOnClickListener {
            body_color = color[2].toString()
            custom_dancing = dancing[2].toString()
            val bodyName = "gs://cacafirebase-554ac.appspot.com/custom_image/color/" + body_color
            val customRef_body = storage.getReferenceFromUrl(bodyName)
            customRef_body?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                body_Iv.setImageBitmap(customRef)
            }?.addOnFailureListener {
//                Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
            }

//            bodyframe.foreground = ContextCompat.getDrawable(this, R.drawable. standing_3)

        }

        ImageButton_color4.setOnClickListener {
            body_color = color[3].toString()
            custom_dancing = dancing[3].toString()
            val bodyName = "gs://cacafirebase-554ac.appspot.com/custom_image/color/" + body_color
            val customRef_body = storage.getReferenceFromUrl(bodyName)
            customRef_body?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                body_Iv.setImageBitmap(customRef)
            }?.addOnFailureListener {
//                Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
            }
//            bodyframe.foreground = ContextCompat.getDrawable(this, R.drawable. standing_4)

        }

        ImageButton_color5.setOnClickListener {
            body_color = color[4].toString()
            custom_dancing = dancing[4].toString()
            val bodyName = "gs://cacafirebase-554ac.appspot.com/custom_image/color/" + body_color
            val customRef_body = storage.getReferenceFromUrl(bodyName)
            customRef_body?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                body_Iv.setImageBitmap(customRef)
            }?.addOnFailureListener {
//                Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
            }
//            bodyframe.foreground = ContextCompat.getDrawable(this, R.drawable. standing_5)

        }

        ImageButton_color6.setOnClickListener {
            body_color = color[5].toString()
            custom_dancing = dancing[5].toString()
            val bodyName = "gs://cacafirebase-554ac.appspot.com/custom_image/color/" + body_color
            val customRef_body = storage.getReferenceFromUrl(bodyName)
            customRef_body?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                body_Iv.setImageBitmap(customRef)
            }?.addOnFailureListener {
 //               Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
            }
//            bodyframe.foreground = ContextCompat.getDrawable(this, R.drawable. standing_6)

        }

        ImageButton_color7.setOnClickListener {
            body_color = color[6].toString()
            custom_dancing = dancing[6].toString()
            val bodyName = "gs://cacafirebase-554ac.appspot.com/custom_image/color/" + body_color
            val customRef_body = storage.getReferenceFromUrl(bodyName)
            customRef_body?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                body_Iv.setImageBitmap(customRef)
            }?.addOnFailureListener {
               // Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
            }
//            bodyframe.foreground = ContextCompat.getDrawable(this, R.drawable. standing_7)

        }

        ImageButton_color8.setOnClickListener {
            body_color = color[7].toString()
            custom_dancing = dancing[7].toString()
            val bodyName = "gs://cacafirebase-554ac.appspot.com/custom_image/color/" + body_color
            val customRef_body = storage.getReferenceFromUrl(bodyName)
            customRef_body?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                body_Iv.setImageBitmap(customRef)
            }?.addOnFailureListener {
                //Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
            }
//            bodyframe.foreground = ContextCompat.getDrawable(this, R.drawable. standing_8)

        }

        ImageButton_color9.setOnClickListener {
            body_color = color[8].toString()
            custom_dancing = dancing[8].toString()
            val bodyName = "gs://cacafirebase-554ac.appspot.com/custom_image/color/" + body_color
            val customRef_body = storage.getReferenceFromUrl(bodyName)
            customRef_body?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                body_Iv.setImageBitmap(customRef)
            }?.addOnFailureListener {
               // Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
            }
//            bodyframe.foreground = ContextCompat.getDrawable(this, R.drawable. standing_9)

        }

        ImageButton_color10.setOnClickListener {
            body_color = color[9].toString()
            custom_dancing = dancing[9].toString()
            val bodyName = "gs://cacafirebase-554ac.appspot.com/custom_image/color/" + body_color
            val customRef_body = storage.getReferenceFromUrl(bodyName)
            customRef_body?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                body_Iv.setImageBitmap(customRef)
            }?.addOnFailureListener {
              //  Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
            }
//            bodyframe.foreground = ContextCompat.getDrawable(this, R.drawable. standing_10)

        }




        // -------------------- 악세사리 acc ----------------------
        val decoframe: FrameLayout = findViewById(R.id.deco)

        ImageButton_color11.setOnClickListener {
            custom_acc = acc[0].toString()
            val accName = "gs://cacafirebase-554ac.appspot.com/custom_image/acc/" + custom_acc
            val customRef_acc = storage.getReferenceFromUrl(accName)
            customRef_acc?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                acc_Iv.setImageBitmap(customRef)
            }?.addOnFailureListener {
              //  Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
            }
//            decoframe.foreground = ContextCompat.getDrawable(this, R.drawable. flushing)

        }

        ImageButton_color12.setOnClickListener {
            custom_acc = acc[1].toString()
            val accName = "gs://cacafirebase-554ac.appspot.com/custom_image/acc/" + custom_acc
            val customRef_acc = storage.getReferenceFromUrl(accName)
            customRef_acc?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                acc_Iv.setImageBitmap(customRef)
            }?.addOnFailureListener {
              //  Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
            }
//            decoframe.foreground = ContextCompat.getDrawable(this, R.drawable. beret)

        }

        ImageButton_color13.setOnClickListener {
            custom_acc = acc[2].toString()
            val accName = "gs://cacafirebase-554ac.appspot.com/custom_image/acc/" + custom_acc
            val customRef_acc = storage.getReferenceFromUrl(accName)
            customRef_acc?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                acc_Iv.setImageBitmap(customRef)
            }?.addOnFailureListener {
             //   Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
            }
//            decoframe.foreground = ContextCompat.getDrawable(this, R.drawable. glass)

        }

        ImageButton_color14.setOnClickListener {
            custom_acc = acc[3].toString()
            val accName = "gs://cacafirebase-554ac.appspot.com/custom_image/acc/" + custom_acc
            val customRef_acc = storage.getReferenceFromUrl(accName)
            customRef_acc?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                acc_Iv.setImageBitmap(customRef)
            }?.addOnFailureListener {
              //  Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
            }
//            decoframe.foreground = ContextCompat.getDrawable(this, R.drawable. crown)

        }

        ImageButton_color15.setOnClickListener {
            custom_acc = acc[4].toString()
            val accName = "gs://cacafirebase-554ac.appspot.com/custom_image/acc/" + custom_acc
            val customRef_acc = storage.getReferenceFromUrl(accName)
            customRef_acc?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                acc_Iv.setImageBitmap(customRef)
            }?.addOnFailureListener {
               // Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
            }
//            decoframe.foreground = ContextCompat.getDrawable(this, R.drawable. flower)

        }

        ImageButton_color16.setOnClickListener {
            custom_acc = acc[5].toString()
            val accName = "gs://cacafirebase-554ac.appspot.com/custom_image/acc/" + custom_acc
            val customRef_acc = storage.getReferenceFromUrl(accName)
            customRef_acc?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                acc_Iv.setImageBitmap(customRef)
            }?.addOnFailureListener {
               // Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
            }
//            decoframe.foreground = ContextCompat.getDrawable(this, R.drawable. baby)

        }


        ImageButton_color17.setOnClickListener {
            custom_acc = acc[6].toString()
            val accName = "gs://cacafirebase-554ac.appspot.com/custom_image/acc/" + custom_acc
            val customRef_acc = storage.getReferenceFromUrl(accName)
            customRef_acc?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                acc_Iv.setImageBitmap(customRef)
            }?.addOnFailureListener {
              //  Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
            }
//            decoframe.foreground = ContextCompat.getDrawable(this, R.drawable. sister)

        }




        val faceframe: FrameLayout = findViewById(R.id.face)

        ImageButton_color18.setOnClickListener {
            custom_eye = eyes[0].toString()
            val eyeName = "gs://cacafirebase-554ac.appspot.com/custom_image/acc/" + custom_eye
            val customRef_eye = storage.getReferenceFromUrl(eyeName)
            customRef_eye?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                eye_Iv.setImageBitmap(customRef)
            }?.addOnFailureListener {
               // Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
            }
//            faceframe.foreground = ContextCompat.getDrawable(this, R.drawable. normaleye)

        }

        ImageButton_color19.setOnClickListener {
            custom_eye = eyes[1].toString()
            val eyeName = "gs://cacafirebase-554ac.appspot.com/custom_image/acc/" + custom_eye
            val customRef_eye = storage.getReferenceFromUrl(eyeName)
            customRef_eye?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
                val customRef = BitmapFactory.decodeByteArray(it, 0, it.size)
                eye_Iv.setImageBitmap(customRef)
            }?.addOnFailureListener {
              //  Toast.makeText(this, "image downloade failed", Toast.LENGTH_SHORT).show()
            }
//            faceframe.foreground = ContextCompat.getDrawable(this, R.drawable. blackeye)

        }

    }
}