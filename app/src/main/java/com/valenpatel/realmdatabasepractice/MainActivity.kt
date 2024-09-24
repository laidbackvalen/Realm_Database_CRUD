package com.valenpatel.realmdatabasepractice

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.valenpatel.realmdatabasepractice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: ArticleViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ArticleAdapter
    lateinit var arrylist: ArrayList<ArticleModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)



        viewModel.allArticles.observe(this) {
//            binding.text.text = it.toString()
            recyclerView = binding.recyclerViewMain
            adapter = ArticleAdapter(this, it as MutableList<ArticleModel>)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)

        }

        binding.insertButton.setOnClickListener {
            val title = binding.edtxtTitle.text.toString()
            val description = binding.edtxtDescription.text.toString()
            insertData(title, description)
        }

        arrylist = viewModel.allArticles.value as ArrayList<ArticleModel>
//        arrylist.forEach {
//            Log.d("TEST", "onCreate: $it")
//        }
//        Log.d("TEST", "onCreate: $arrylist")

    }

    private fun insertData(title: String, description: String) {
        viewModel.addArticle(title, description)
        Toast.makeText(this, "Data Inserted", Toast.LENGTH_SHORT).show()
        Toast.makeText(this, arrylist.toString(), Toast.LENGTH_SHORT).show()
    }
}