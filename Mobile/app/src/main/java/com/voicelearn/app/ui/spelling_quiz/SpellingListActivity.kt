package com.voicelearn.app.ui.spelling_quiz

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.voicelearn.app.R
import com.voicelearn.app.adapter.ItemAdapter
import com.voicelearn.app.api.responses.Quiz
import com.voicelearn.app.databinding.ActivitySpellingListBinding
import com.voicelearn.app.helper.ViewModelFactory
import com.voicelearn.app.ui.spelling_quiz.view_model.SpellingListViewModel

class SpellingListActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySpellingListBinding.inflate(layoutInflater)
    }
    private val viewModel: SpellingListViewModel by viewModels {
        ViewModelFactory(this)
    }

    private lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val stringAB: String = resources.getString(R.string.spelling_quiz_page_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = stringAB

        val level = intent.getStringExtra(LEVEL_EXTRA)
        level?.let {
            viewModel.getSpellingListByLevel(level)
        }

        viewModel.spellingList.observe(this) {
            setDataAdapter(it)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun setDataAdapter(quizzes: List<Quiz>) {
        adapter = ItemAdapter(this@SpellingListActivity, quizzes)
        binding.apply {
            rcSpellingList.setHasFixedSize(true)
            rcSpellingList.adapter = adapter
            rcSpellingList.layoutManager = LinearLayoutManager(this@SpellingListActivity)
        }
        adapter.setOnItemClickCallback(object : ItemAdapter.OnItemClickCallback {
            override fun onItemClicked(id: String) {
                Intent(this@SpellingListActivity, SpellingSubmitActivity::class.java).also {
                    it.putExtra(SpellingSubmitActivity.EXTRA_ID, id)
                    startActivity(it)
                }
            }
        })
    }

    companion object {
        private const val LEVEL_EXTRA = "level"
    }
}