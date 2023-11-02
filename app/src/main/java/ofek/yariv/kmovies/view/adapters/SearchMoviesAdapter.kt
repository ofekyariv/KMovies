package ofek.yariv.kmovies.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ofek.yariv.kmovies.R
import ofek.yariv.kmovies.model.data.Movie
import ofek.yariv.kmovies.model.network.api.Api

class SearchMoviesAdapter(
    private val onMovieClickListener: (Movie) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<Movie>()

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movie = items[position]
        movie.let { (holder as MovieViewHolder).bind(it, onMovieClickListener) }
    }

    fun submitList(newItems: List<Movie>) {
        val removedItems = items.size
        items.clear()
        notifyItemRangeRemoved(0, removedItems)
        items.addAll(newItems)
        notifyItemRangeInserted(0, items.size)
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivMoviePoster: ImageView = itemView.findViewById(R.id.ivMoviePoster)
        private val tvMovieRating: TextView = itemView.findViewById(R.id.tvMovieRating)

        fun bind(movie: Movie, onMovieClickListener: (Movie) -> Unit) {
            if (movie.posterPath == null) {
                return
            }
            Glide.with(itemView.context).load(Api.getPosterPath(movie.posterPath))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivMoviePoster)

            tvMovieRating.text = String.format("%.1f", movie.voteAverage)

            itemView.setOnClickListener {
                onMovieClickListener(movie)
            }
        }
    }
}