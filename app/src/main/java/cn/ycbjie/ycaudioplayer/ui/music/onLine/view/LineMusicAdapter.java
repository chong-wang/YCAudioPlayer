package cn.ycbjie.ycaudioplayer.ui.music.onLine.view;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;

import cn.ycbjie.ycaudioplayer.R;
import cn.ycbjie.ycaudioplayer.inter.OnMoreClickListener;
import cn.ycbjie.ycaudioplayer.ui.music.onLine.model.bean.OnlineMusicList;
import cn.ycbjie.ycaudioplayer.util.musicUtils.FileMusicUtils;


public class LineMusicAdapter extends RecyclerArrayAdapter<OnlineMusicList.OnlineMusic> {

    LineMusicAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }


    private class ViewHolder extends BaseViewHolder<OnlineMusicList.OnlineMusic> {

        View v_playing , v_divider;
        TextView tv_title , tv_artist ;
        ImageView iv_cover , iv_more;

        ViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_line_music);
            v_playing = $(R.id.v_playing);
            iv_cover = $(R.id.iv_cover);
            tv_title = $(R.id.tv_title);
            tv_artist = $(R.id.tv_artist);
            iv_more = $(R.id.iv_more);
            v_divider = $(R.id.v_divider);
        }

        @Override
        public void setData(OnlineMusicList.OnlineMusic data) {
            super.setData(data);
            Glide.with(getContext())
                    .load(data.getPic_small())
                    .placeholder(R.drawable.default_cover)
                    .error(R.drawable.default_cover)
                    .into(iv_cover);
            tv_title.setText(data.getTitle());
            String artist = FileMusicUtils.getArtistAndAlbum(data.getArtist_name(), data.getAlbum_title());
            tv_artist.setText(artist);
            iv_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onMoreClick(getAdapterPosition());
                }
            });
            v_divider.setVisibility(isShowDivider(getAdapterPosition()) ? View.VISIBLE : View.GONE);
        }


        private boolean isShowDivider(int position) {
            return position != getAllData().size() - 1;
        }

    }

    private OnMoreClickListener mListener;
    void setOnMoreClickListener(OnMoreClickListener listener) {
        mListener = listener;
    }


}
