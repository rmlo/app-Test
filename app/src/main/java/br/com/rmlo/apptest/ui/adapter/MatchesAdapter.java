package br.com.rmlo.apptest.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import br.com.rmlo.apptest.databinding.MatchItemBinding;
import br.com.rmlo.apptest.domain.Match;
import br.com.rmlo.apptest.ui.DetailActivity;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.ViewHolder> {

    private final List<Match> matches;

    public List<Match> getMatches() {
        return matches;
    }

    public MatchesAdapter(List<Match> matches) {
        this.matches = matches;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater LayoutInflater = android.view.LayoutInflater.from(parent.getContext());
        MatchItemBinding binding = MatchItemBinding.inflate(LayoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        Match match = matches.get(position);

        //Adapta os dados da partida (recuperada da API) para o nosso layout.
        Glide.with(context).load(match.getHomeTeam().getImage()).into(holder.binding.ivHomeTeam);
        holder.binding.tvHomeTeamName.setText(match.getHomeTeam().getName());
        if(match.getHomeTeam().getScore() != null){
            holder.binding.tvHomeTeamScore.setText(String.valueOf(match.getHomeTeam().getScore()));
        }
        Glide.with(context).load(match.getAwayTeam().getImage()).into(holder.binding.ivAwayTeam);
        holder.binding.tvAwayName.setText(match.getAwayTeam().getName());
        if(match.getAwayTeam().getScore() != null){
            holder.binding.tvAwayTeamScore.setText(String.valueOf(match.getAwayTeam().getScore()));

        }

        holder.itemView.setOnClickListener(view ->{
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(DetailActivity.Extras.MATCH, match);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final MatchItemBinding binding;

        public ViewHolder(MatchItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
