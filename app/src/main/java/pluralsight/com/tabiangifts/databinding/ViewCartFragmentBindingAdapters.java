package pluralsight.com.tabiangifts.databinding;

import android.databinding.BindingAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import pluralsight.com.tabiangifts.adapters.CartItemAdapter;
import pluralsight.com.tabiangifts.models.CartItem;


/**
 * Created by User on 2/9/2018.
 */

public class ViewCartFragmentBindingAdapters {

    private static final String TAG = "ViewCartFragmentBinding";


    @BindingAdapter("cartItems")
    public static void setCartItems(RecyclerView view, List<CartItem> cartItems){
        if(cartItems == null){
            return;
        }
        RecyclerView.LayoutManager layoutManager = view.getLayoutManager();
        if(layoutManager == null){
            view.setLayoutManager(new LinearLayoutManager(view.getContext()));
        }
        CartItemAdapter adapter = (CartItemAdapter) view.getAdapter();
        if(adapter == null){
            adapter = new CartItemAdapter(view.getContext(), cartItems);
            view.setAdapter(adapter);
        }
        else{
            adapter.updateCartItems(cartItems);
        }
    }

}





















