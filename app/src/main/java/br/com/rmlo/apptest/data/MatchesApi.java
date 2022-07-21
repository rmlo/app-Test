package br.com.rmlo.apptest.data;

import java.util.List;

import br.com.rmlo.apptest.domain.Match;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MatchesApi {

        @GET("matches.json")
        Call<List<Match>> getMatches();
}
