package com.example.etta
import android.util.Log
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.etta.databinding.ActivityDetailedBinding


class DetailedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailedBinding
//    override fun onNewIntent(intent: Intent) {
//        super.onNewIntent(intent)
//        val intentData = intent
//
//        val name = intentData.getStringExtra("name")
//
//        Log.v("fatimeName",name+"")
//
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val intent = Intent(this, MainActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//        startActivity(intent)
//        finish()

        val intentData = getIntent()
//        val intentData = intent
        if (intentData != null) {
            val name = intentData.getStringExtra("name")

//            Log.v("fatimeName",name+"")
            Log.v("fatimeName",name+" :*")
            val ingredients = intentData.getStringExtra("ingredients")
            val desc = intentData.getStringExtra("desc")
            val image = intentData.getIntExtra("image", R.drawable.mag)

            Log.d("ListAdapter", "name: ${name}")
            Log.d("ListAdapter", "ingredients: ${ingredients}")
            Log.d("ListAdapter", "desc: ${desc}")
            Log.d("ListAdapter", "image: ${image}")
            Log.d("ListAdapter", "Starting DetailedActivity with Intent: $intent")


            binding.detailName.text = name
            binding.detailIngredients.text = ingredients
            binding.detailDesc.text = desc
            binding.detailImage.setImageResource(image)
        }
    }
}
