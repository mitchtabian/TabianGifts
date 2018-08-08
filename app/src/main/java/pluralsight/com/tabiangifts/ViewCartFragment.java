package pluralsight.com.tabiangifts;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pluralsight.com.tabiangifts.databinding.FragmentViewCartBinding;
import pluralsight.com.tabiangifts.models.CartItem;
import pluralsight.com.tabiangifts.models.CartViewModel;
import pluralsight.com.tabiangifts.util.PreferenceKeys;
import pluralsight.com.tabiangifts.util.Products;


/**
 * Created by User on 2/8/2018.
 */

public class ViewCartFragment extends Fragment {

    private static final String TAG = "ViewCartFragment";

    //data binding
    FragmentViewCartBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentViewCartBinding.inflate(inflater);
        mBinding.setIMainActivity((IMainActivity)getActivity());
        mBinding.getIMainActivity().setCartVisibility(true);

        getShoppingCartList();

        return mBinding.getRoot();
    }

    private void getShoppingCartList(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Set<String> serialNumbers = preferences.getStringSet(PreferenceKeys.shopping_cart, new HashSet<String>());

        Products products = new Products();
        List<CartItem> cartItems = new ArrayList<>();
        for(String serialNumber : serialNumbers){
            int quantity = preferences.getInt(serialNumber, 0);
            cartItems.add(new CartItem(products.PRODUCT_MAP.get(serialNumber), quantity));
        }
        CartViewModel cartViewModel = new CartViewModel();
        cartViewModel.setCart(cartItems);
        mBinding.setCartView(cartViewModel);
    }

    public void updateCartItems(){
        getShoppingCartList();
    }

    @Override
    public void onDestroy() {
        mBinding.getIMainActivity().setCartVisibility(false);
        super.onDestroy();
    }

}
















