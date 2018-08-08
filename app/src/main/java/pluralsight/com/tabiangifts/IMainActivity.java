package pluralsight.com.tabiangifts;

import android.view.View;

import pluralsight.com.tabiangifts.models.CartItem;
import pluralsight.com.tabiangifts.models.Product;

/**
 * Created by User on 2/15/2018.
 */

public interface IMainActivity {

    void inflateViewProductFragment(Product product);

    void showQuantityDialog();

    void setQuantity(int quantity);

    void addToCart(Product product, int quantity);

    void inflateViewCartFragment();

    void setCartVisibility(boolean visibility);

    void updateQuantity(Product product, int quantity);

    void removeCartItem(CartItem cartItem);

}
