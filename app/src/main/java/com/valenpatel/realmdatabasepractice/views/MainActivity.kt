package com.valenpatel.realmdatabasepractice.views

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.valenpatel.realmdatabasepractice.adapter.ArticleAdapter
import com.valenpatel.realmdatabasepractice.models.ArticleModel
import com.valenpatel.realmdatabasepractice.viewmodel.ArticleViewModel
import com.valenpatel.realmdatabasepractice.R
import com.valenpatel.realmdatabasepractice.databinding.ActivityMainBinding
import io.realm.Realm

class MainActivity : AppCompatActivity(), ArticleAdapter.OnItemClickListener {

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
            adapter = ArticleAdapter(this, it as MutableList<ArticleModel>, this)
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
        binding.edtxtTitle.text.clear()
        binding.edtxtDescription.text.clear()
//        Toast.makeText(this, "Data Inserted", Toast.LENGTH_SHORT).show()
//        Toast.makeText(this, arrylist.toString(), Toast.LENGTH_SHORT).show()
    }


    override fun onItemClick(position: Int) {

        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.alert_dialog_update_realm_db_layout, null)

        val title = dialogView.findViewById<EditText>(R.id.edtxt_Title_Update)
        val description = dialogView.findViewById<EditText>(R.id.edtxt_Description_Update)

        val article = viewModel.allArticles.value?.get(position)
        if (article != null) {
            title.setText(article.title)
            description.setText(article.description)

            val alertDialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .create()

            dialogView.findViewById<Button>(R.id.updateButton).setOnClickListener {
                val updatedTitle = title.text.toString()
                val updatedDescription = description.text.toString()

                if (updatedTitle.isNotBlank() && updatedDescription.isNotBlank()) {
                    viewModel.updateArticle(article.id, updatedTitle, updatedDescription)

                    adapter.notifyItemChanged(position)
                    alertDialog.dismiss()

                    Toast.makeText(this, "Article updated successfully", Toast.LENGTH_SHORT).show()
                } else {

                    Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            alertDialog.show()
        }
    }




}