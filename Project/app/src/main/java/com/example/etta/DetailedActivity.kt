//package com.example.etta
//
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.ContextCompat
//import com.example.etta.databinding.ActivityDetailedBinding
//
//class DetailedActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityDetailedBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityDetailedBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//
//        val intent = Intent(this, MainActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        ContextCompat.startActivity(intent)


//
//
////        val intent = this.intent
//        if (intent != null) {
//            val name = intent.getStringExtra("name")
//            val ingredients = intent.getIntExtra("ingredients", R.string.maggiIngredients)
//            val desc = intent.getIntExtra("desc", R.string.maggieDesc)
//            val image = intent.getIntExtra("image", R.drawable.mag)
//
//
//
//            binding.detailName.text = name
//            binding.detailIngredients.text = getString(ingredients)
//            binding.detailDesc.text = getString(desc)
//            binding.detailImage.setImageResource(image)
//        }
//    }
//}
//
//
//



package com.example.etta

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.etta.databinding.ActivityDetailedBinding


class DetailedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()

        val intentData = intent
        if (intentData != null) {
            val name = intentData.getStringExtra("name")
            val ingredients = intentData.getStringExtra("ingredients")
            val desc = intentData.getStringExtra("desc")
            val image = intentData.getIntExtra("image", R.drawable.mag)

            binding.detailName.text = name
            binding.detailIngredients.text = ingredients
            binding.detailDesc.text = desc
            binding.detailImage.setImageResource(image)
        }
    }
}
