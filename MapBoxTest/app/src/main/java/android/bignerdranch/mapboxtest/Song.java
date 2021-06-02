package android.bignerdranch.mapboxtest;

import java.util.Objects;

import kaaes.spotify.webapi.android.models.Track;

public class Song {
    private String songName;
    private String songArtist;
    private String id;
    private final Track spotifyTrackInfo;
    private double duration;

    public Song(Track spotifyTrackInfo,String songArtist,String songName,String id,double duration){
        this.id=id;
        this.songArtist=songArtist;
        this.songName=songName;
        this.duration=duration;
        this.spotifyTrackInfo=spotifyTrackInfo;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public void setSongArtist(String songArtist) {
        this.songArtist = songArtist;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Track getSpotifyTrackInfo() {
        return spotifyTrackInfo;
    }
}

