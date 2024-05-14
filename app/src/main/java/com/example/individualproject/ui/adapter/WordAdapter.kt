package com.example.individualproject.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.individualproject.data.model.Word
import com.example.individualproject.databinding.LayoutWordItemBinding

// The Word Adapter for NewWord and CompletedWord.
class WordAdapter(
    private var words: List<Word>
): RecyclerView.Adapter<WordAdapter.WordViewHolder>() {
    var listener: Listener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): WordViewHolder {
        val binding = LayoutWordItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: WordViewHolder, position: Int
    ) { holder.bind(words[position]) }

    override fun getItemCount() = words.size

    fun getWords() = words

    fun setWords(words: List<Word>) {
        this.words = words
        notifyDataSetChanged()
    }

    inner class WordViewHolder(
        private val binding: LayoutWordItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(word: Word) {
            binding.run {
                tvTitle.text = word.title
                tvDef.text = if(word.definition.length > 40) {
                    "${word.definition.slice(0 .. 40)}..."
                } else { word.definition }
                cvWord.setOnClickListener { listener?.onClick(word) }
            }
        }
    }

    interface Listener { fun onClick(word: Word) }
}