package android.bignerdranch.mapboxtest;


import java.util.List;

public class FreshSongFragment extends SongListFragment {

    @Override
    protected List<Song> createSongList(String genre, double journeyTime) {
        FreshSongCatalogue freshSongCatalogue =  new FreshSongCatalogue(kaesApi,journeyTime,genre,this);
        return freshSongCatalogue.getSongs();
    }

}