package pluralsight.com.tabiangifts.models;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import pluralsight.com.tabiangifts.BR;
import pluralsight.com.tabiangifts.IMainActivity;


/**
 * Created by User on 2/9/2018.
 */

public class CartItemViewModel extends BaseObservable{

    private static final String TAG = "CartItemViewModel";

    private CartItem cartItem;

    @Bindable
    public CartItem getCartItem(){
        return cartItem;
    }

    public void setCartItem(CartItem cartItem){
        Log.d(TAG, "setQuantity: updating cart");
        this.cartItem = cartItem;
        notifyPropertyChanged(BR.cartItem);
    }

    public void increaseQuantity(Context context){
        CartItem cartItem = getCartItem();
        cartItem.setQuantity(cartItem.getQuantity() + 1);
        setCartItem(cartItem);
        IMainActivity iMainActivity = (IMainActivity) context;
        iMainActivity.updateQuantity(cartItem.getProduct(), 1);
    }

    public void decreaseQuantity(Context context){
        CartItem cartItem = getCartItem();
        IMainActivity iMainActivity = (IMainActivity) context;
        if(cartItem.getQuantity() > 1){
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            setCartItem(cartItem);
            iMainActivity.updateQuantity(cartItem.getProduct(), -1);
        }
        else if(cartItem.getQuantity() == 1){
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            setCartItem(cartItem);
            iMainActivity.removeCartItem(cartItem);
        }
    }

    public String getQuantityString(CartItem cartItem){
        return ("Qty: " + String.valueOf(cartItem.getQuantity()));
    }

}



























