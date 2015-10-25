/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Valter Kasper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package sk.kasper.movieapp.ui.movie;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.lang.reflect.Type;
import java.util.ArrayList;

import sk.kasper.movieapp.models.BookmarkMovieEvent;
import sk.kasper.movieapp.models.Movie;
import sk.kasper.movieapp.network.OmdbApi;
import sk.kasper.movieapp.network.TasteKidApi;


/**
 * Service for movie app
 */
public class MovieService {

	private static final String TAG = "MovieService";
	private static final String PREF_BOOKMARKS = "pref-bookmarks";
	private final TasteKidApi tasteKidApi;
	private final OmdbApi omdbApi;
    private final Bus bus;
	private final SharedPreferences sharedPref;

	public MovieService(TasteKidApi tasteKidApi, OmdbApi omdbApi, Bus bus, final SharedPreferences sharedPref) {
		this.tasteKidApi = tasteKidApi;
		this.omdbApi = omdbApi;
        this.bus = bus;
		this.sharedPref = sharedPref;
	}

    @Subscribe
	public void bookmarkMovie(BookmarkMovieEvent event) {
		Log.d(TAG, "bookmarkMovie() called with: " + "event = [" + event + "]");
		Gson gson = new Gson();

		Type collectionType = new TypeToken<ArrayList<Movie>>() {}.getType();
		final ArrayList<Movie> movies = gson.fromJson(sharedPref.getString(PREF_BOOKMARKS, "[]"), collectionType);
		if (event.movie.bookmarked) {
			movies.remove(event.movie);
		} else {
			movies.add(event.movie);
		}

		sharedPref.edit().putString(PREF_BOOKMARKS, gson.toJson(movies)).apply();
	}
}
