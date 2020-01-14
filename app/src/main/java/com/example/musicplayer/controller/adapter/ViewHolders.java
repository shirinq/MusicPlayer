package com.example.musicplayer.controller.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicplayer.R;
import com.example.musicplayer.Utils.ID3Tags;
import com.example.musicplayer.Utils.PictureUtils;
import com.example.musicplayer.Utils.SquareImage;
import com.example.musicplayer.Utils.SquareRoundedImage;
import com.example.musicplayer.model.Album;
import com.example.musicplayer.model.Artist;
import com.example.musicplayer.model.Qualifier;
import com.example.musicplayer.model.Song;

import org.jaudiotagger.tag.datatype.Artwork;

import de.hdodenhof.circleimageview.CircleImageView;


public class ViewHolders {

    private CallBacks callBacks;
    private Context mContext;

    public ViewHolders(Context context) {
        callBacks = (CallBacks) context;
        mContext = context;
    }

    //Handle PlaySong and SongList fragment invoker;
    public interface CallBacks {
        void PlaySong(Song song);

        void SongList(String albumOrArtist, Qualifier qualifier);
    }

    /**
     * SONG VIEW HOLDER CLASS
     */

    public class MusicItems extends RecyclerView.ViewHolder implements MusicRecyclerAdapter.BindCallBack<Song> {

        private TextView mTVMusicName, mTVMusicArtist, mDuration;
        private CircleImageView mIVMusicCover;
        private Song mSong;

        public MusicItems(@NonNull View itemView) {
            super(itemView);

            mIVMusicCover = itemView.findViewById(R.id.item_song_art);
            mTVMusicArtist = itemView.findViewById(R.id.item_song_artist);
            mTVMusicName = itemView.findViewById(R.id.item_song_title);
            mDuration = itemView.findViewById(R.id.item_song_duration);

            itemView.setOnClickListener(view -> {
                callBacks.PlaySong(mSong);
            });

        }

        @Override
        public void bindHolder(Song song) {

            mSong = song;

            mTVMusicArtist.setText(mSong.getArtist());
            mTVMusicName.setText(mSong.getTitle());
            mDuration.setText(mSong.getDuration());
            mIVMusicCover.setImageDrawable(mContext.getResources().getDrawable(R.drawable.song_placeholder));

            SetArt art = new SetArt();
            art.execute();

        }

        private class SetArt extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... voids) {
                Artwork artwork = ID3Tags.getArtwork(mSong.getFilePath());
               try {
                   return BitmapFactory.decodeByteArray(artwork.getBinaryData(), 0, artwork.getBinaryData().length);

               }catch (Exception e){
                   return null;
               }
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                Glide.with(mContext).asDrawable()
                        .override(400, 400)
                        .load(bitmap)
                        .placeholder(R.drawable.song_placeholder)
                        .into(mIVMusicCover);
            }
        }
    }


    /**
     * ALBUM VIEW HOLDER CLASS
     */
    public class AlbumItems extends RecyclerView.ViewHolder implements MusicRecyclerAdapter.BindCallBack<Album> {

        private CircleImageView mAlbumArt;
        private TextView mTitle;
        private TextView mArtist;
        private Album mAlbum;


        public AlbumItems(@NonNull View itemView) {
            super(itemView);

            mAlbumArt = itemView.findViewById(R.id.item_album_cover);
            mTitle = itemView.findViewById(R.id.item_album_title);
            mArtist = itemView.findViewById(R.id.item_album_artist);
            itemView.setOnClickListener(view ->
                callBacks.SongList(mAlbum.getTitle(), Qualifier.ALBUM));

        }

        @Override
        public void bindHolder(Album album) {
            mAlbum = album;
            mTitle.setText(album.getTitle());
            mArtist.setText(album.getAlbumArtist());
            mAlbumArt.setImageDrawable(mContext.getResources().getDrawable(R.drawable.song_placeholder));

            SetArt art = new SetArt();
            art.execute();
        }

        private class SetArt extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                return mAlbum.getArtworkPath();
            }

            @Override
            protected void onPostExecute(String artFile) {
                if(artFile == null){
                    mAlbumArt.setImageDrawable(mContext.getResources().getDrawable(R.drawable.song_placeholder));
                    return;
                }
                Glide.with(mContext).asDrawable()
                        .override(300, 300)
                        .load(artFile)
                        .into(mAlbumArt);
            }
        }
    }


    /**
     * ARTIST VIEW HOLDER CLASS
     */

    public class ArtistItems extends RecyclerView.ViewHolder implements MusicRecyclerAdapter.BindCallBack<Artist> {

        private CircleImageView mImage;
        private TextView mName;
        private Artist mArtist;

        public ArtistItems(@NonNull View itemView) {
            super(itemView);

            mName = itemView.findViewById(R.id.item_song_artist);
            mImage = itemView.findViewById(R.id.item_artist_art);

            itemView.setOnClickListener(view ->
                callBacks.SongList(mArtist.getName(), Qualifier.ARTIST));

        }

        @Override
        public void bindHolder(Artist artist) {
            mArtist = artist;
            mName.setText(artist.getName());
        }
    }
}
