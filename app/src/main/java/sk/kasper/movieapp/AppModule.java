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

package sk.kasper.movieapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import sk.kasper.movieapp.network.OmdbApi;
import sk.kasper.movieapp.network.TasteKidApi;
import sk.kasper.movieapp.network.TasteKidApiKey;
import sk.kasper.movieapp.storage.BookmarksStorage;
import sk.kasper.movieapp.ui.bookmarks.BookmarksContract;
import sk.kasper.movieapp.ui.bookmarks.BookmarksListActivity;
import sk.kasper.movieapp.ui.movie.GoodMovieFinder;
import sk.kasper.movieapp.ui.movie.MovieActivity;

@Module(
		injects = {MovieActivity.class, BookmarksListActivity.class}
)
public class AppModule {

	final Context context;

	public AppModule(final Context context) {this.context = context;}

	@Provides
	TasteKidApi provideTasteKidApi() {
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint(TasteKidApi.REST_TASTEKID_ENDPOINT)
				.setLogLevel(RestAdapter.LogLevel.FULL)
				.setLog(new RestAdapter.Log() {
					@Override
					public void log(final String msg) {Log.d("Retrofit: ", msg);}
				})
				.build();

		return restAdapter.create(TasteKidApi.class);
	}

	@Provides
	OmdbApi provideOmdbApi() {
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint(OmdbApi.REST_OMDB_ENDPOINT)
				.setLogLevel(RestAdapter.LogLevel.FULL)
				.setLog(new RestAdapter.Log() {
					@Override
					public void log(final String msg) {Log.d("Retrofit: ", msg);}
				})
				.build();

		return restAdapter.create(OmdbApi.class);
	}

	@Provides
	SharedPreferences provideSharedPreferences() {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	@Provides
	GoodMovieFinder provideGoodMovieFinder() {
		return new GoodMovieFinder(6.5f);
	}

	@Provides
	TasteKidApiKey provideTasteKidApiKey() {
		return new TasteKidApiKey(context.getString(R.string.taste_kid_api_key));
	}
}