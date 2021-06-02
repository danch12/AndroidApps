package android.bignerdranch.mapboxtest;


import java.util.List;

public class FreshSongFragment extends SongListFragment {

    @Override
    protected List<Song> createSongList(String genre, double journeyTime) {
        SongCatalogue songCatalogue=  new SongCatalogue(kaesApi,journeyTime,genre,this);
        return songCatalogue.getSongs();
    }

}