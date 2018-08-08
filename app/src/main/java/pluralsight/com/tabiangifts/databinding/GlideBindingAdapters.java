package pluralsight.com.tabiangifts.databinding;

import android.app.Activity;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

import pluralsight.com.tabiangifts.R;


/**
 * Created by User on 2/7/2018.
 */

public class GlideBindingAdapters {

    private static final String TAG = "GlideBindingAdapters";


    @BindingAdapter("imageUrl")
    public static void setImage(ImageView view, String imageUrl){

        Context context = view.getContext();

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background);

        Glide.with(context)
                .setDefaultRequestOptions(options)
                .load(imageUrl)
                .into(view);
    }

    @BindingAdapter("imageUrl")
    public static void setImage(ImageView view, int imageUrl){

        Context context = view.getContext();

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background);

        Glide.with(context)
                .setDefaultRequestOptions(options)
                .load(imageUrl)
                .into(view);
    }


    @BindingAdapter({"requestListener", "imageResource"})
    public static void bindRequestListener(ImageView view, RequestListener listener, int imageResource){
        Log.d(TAG, "bindRequestListener: setting image resource.");

        Context context = view.getContext();

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background);

        Glide.with(context)
                .setDefaultRequestOptions(options)
                .load(imageResource)
                .listener(listener)
                .into(view);
    }

}
















