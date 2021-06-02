package com.example.spotifyapp;

public class Song {
    private String songName;
    private String songArtist;
    private String id;

    public Song(String songArtist,String songName,String id){
        this.id=id;
        this.songArtist=songArtist;
        this.songName=songName;
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
}
