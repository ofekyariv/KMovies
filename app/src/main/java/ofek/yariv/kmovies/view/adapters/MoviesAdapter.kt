package ofek.yariv.kmovies.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ofek.yariv.kmovies.R
import ofek.yariv.kmovies.model.data.Movie
import ofek.yariv.kmovies.model.network.api.Api

class MoviesAdapter(
    private val onMovieClickListener: (Movie) -> Unit
) : PagingDataAdapter<Movie, MoviesAdapter.MovieViewHolder>(MovieComparator) {

    companion object {
        private val MovieComparator = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie) =
                oldItem == newItem
        }
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivMoviePoster: ImageView = itemView.findViewById(R.id.ivMoviePoster)
        private val tvMovieRating: TextView = itemView.findViewById(R.id.tvMovieRating)

        fun bind(movie: Movie, onMovieClickListener: (Movie) -> Unit) {
            if (movie.posterPath == null) {
                return
            }
            val imageUrl = Api.getPosterPath(movie.posterPath)

            Glide.with(itemView.context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivMoviePoster)

            tvMovieRating.text = String.format("%.1f", movie.voteAverage)

            itemView.setOnClickListener {
                onMovieClickListener(movie)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        movie?.let { holder.bind(it, onMovieClickListener) }
    }
}