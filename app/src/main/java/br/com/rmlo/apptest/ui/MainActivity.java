package br.com.rmlo.apptest.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import br.com.rmlo.apptest.R;
import br.com.rmlo.apptest.data.MatchesApi;
import br.com.rmlo.apptest.databinding.ActivityMainBinding;
import br.com.rmlo.apptest.domain.Match;
import br.com.rmlo.apptest.ui.adapter.MatchesAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MatchesApi matchesApi;
    private MatchesAdapter matchesAdapter = new MatchesAdapter(Collections.emptyList());

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupHttpClient();
        setupMatchesList();
        setupMatchesRefresh();
        setupFloatingActionButton();
    }

    private void setupHttpClient() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://rmlo.github.io/matches-simulator-API/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        matchesApi = retrofit.create(MatchesApi.class);
    }

    private void setupMatchesList() {
        binding.rvMatches.setHasFixedSize(true);
        binding.rvMatches.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMatches.setAdapter(matchesAdapter);
        findMatchesFromApi();
    }

    private void findMatchesFromApi() {
        binding.srlMatches.setRefreshing(true);
        matchesApi.getMatches().enqueue(new Callback<List<Match>>() {
            @Override
            public void onResponse(@NonNull Call<List<Match>> call, @NonNull Response<List<Match>> response) {
                if(response.isSuccessful()){
                    List<Match> matches = response.body();
                    matchesAdapter = new MatchesAdapter(matches);
                    binding.rvMatches.setAdapter(matchesAdapter);
                } else {
                    showErrorMessage();
                }
                binding.srlMatches.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<List<Match>> call, @NonNull Throwable t) {
                showErrorMessage();
                binding.srlMatches.setRefreshing(false);
            }
        });
    }

    private void setupMatchesRefresh() {
        binding.srlMatches.setOnRefreshListener(this::findMatchesFromApi);
    }

    private void setupFloatingActionButton() {
        binding.fabSimulate.setOnClickListener(view -> {
            view.animate().rotationBy(360).setDuration(500).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    Random random = new Random();
                    for (int i = 0; i <matchesAdapter.getItemCount(); i++)
                         {
                             Match match = matchesAdapter.getMatches().get(i);
                             match.getHomeTeam().setScore(random.nextInt(match.getHomeTeam().getStars() + 1));
                             match.getAwayTeam().setScore(random.nextInt(match.getAwayTeam().getStars() + 1));
                             matchesAdapter.notifyItemChanged(i);
                         }
                }
            });
        });
    }

    private void showErrorMessage() {
        Snackbar.make(binding.fabSimulate,R.string.error_api, Snackbar.LENGTH_LONG).show();
    }
}
