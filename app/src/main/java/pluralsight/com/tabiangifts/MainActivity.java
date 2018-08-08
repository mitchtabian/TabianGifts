package pluralsight.com.tabiangifts;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pluralsight.com.tabiangifts.databinding.ActivityMainBinding;
import pluralsight.com.tabiangifts.models.CartItem;
import pluralsight.com.tabiangifts.models.CartViewModel;
import pluralsight.com.tabiangifts.models.Product;
import pluralsight.com.tabiangifts.util.PreferenceKeys;
import pluralsight.com.tabiangifts.util.Products;

public class MainActivity extends AppCompatActivity implements IMainActivity{

    private static final String TAG = "MainActivity";

    //data binding
    ActivityMainBinding mBinding;


    //vars
    private boolean mClickToExit = false;
    private Runnable mCheckoutRunnable;
    private Handler mCheckoutHandler;
    private int mCheckoutTimer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.cart.setOnTouchListener(new CartTouchListener());
        mBinding.proceedToCheckout.setOnClickListener(mCheckOutListener);

        getShoppingCart();
        init();
    }

    private void init(){
        MainFragment fragment = new MainFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, fragment, getString(R.string.fragment_main));
        transaction.commit();
    }


    private void getShoppingCart(){
        Log.d(TAG, "getShoppingCart: getting shopping cart.");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> serialNumbers = preferences.getStringSet(PreferenceKeys.shopping_cart, new HashSet<String>());

        // Retrieve the quantities of each item from the cart
        Products products = new Products();
        List<CartItem> cartItems = new ArrayList<>();
        for(String serialNumber : serialNumbers){
            int quantity = preferences.getInt(serialNumber, 0);

            cartItems.add(new CartItem(products.PRODUCT_MAP.get(serialNumber), quantity));
        }

        CartViewModel viewModel = new CartViewModel();
        viewModel.setCart(cartItems);
        try{
            viewModel.setCartVisible(mBinding.getCartView().isCartVisible());
        }catch (NullPointerException e){
            Log.e(TAG, "getShoppingCart: NullPointerException: " + e.getMessage() );
        }
        mBinding.setCartView(viewModel);
    }

    public void checkout(){
        Log.d(TAG, "checkout: checking out.");

        mBinding.progressBar.setVisibility(View.VISIBLE);

        mCheckoutHandler = new Handler();
        mCheckoutRunnable  = new Runnable() {
            @Override
            public void run() {
                mCheckoutHandler.postDelayed(mCheckoutRunnable, 200);
                mCheckoutTimer += 200;
                if(mCheckoutTimer >= 1600){
                    emptyCart();
                    mBinding.progressBar.setVisibility(View.GONE);
                    mCheckoutHandler.removeCallbacks(mCheckoutRunnable);
                    mCheckoutTimer = 0;
                }
            }
        };
        mCheckoutRunnable.run();
    }

    private void emptyCart(){
        Log.d(TAG, "emptyCart: emptying cart.");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> serialNumbers = preferences.getStringSet(PreferenceKeys.shopping_cart, new HashSet<String>());
        SharedPreferences.Editor editor = preferences.edit();

        for(String serialNumber : serialNumbers){
            editor.remove(serialNumber);
            editor.commit();
        }

        editor.remove(PreferenceKeys.shopping_cart);
        editor.commit();
        Toast.makeText(this, "thanks for shopping!", Toast.LENGTH_SHORT).show();
        removeViewCartFragment();
        getShoppingCart();
    }

    public View.OnClickListener mCheckOutListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            checkout();
        }
    };

    public static class CartTouchListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                view.setBackgroundColor(view.getContext().getResources().getColor(R.color.blue4));
                view.performClick();

                IMainActivity iMainActivity = (IMainActivity)view.getContext();
                iMainActivity.inflateViewCartFragment();
            }
            else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                view.setBackgroundColor(view.getContext().getResources().getColor(R.color.blue6));
            }

            return true;
        }
    }

    public void removeViewCartFragment(){
        getSupportFragmentManager().popBackStack();
        ViewCartFragment fragment = (ViewCartFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_view_cart));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(fragment != null){
            transaction.remove(fragment);
            transaction.commit();
        }
    }

    @Override
    public void setCartVisibility(boolean visibility) {
        mBinding.getCartView().setCartVisible(visibility);
    }

    @Override
    public void inflateViewCartFragment(){
        ViewCartFragment fragment = (ViewCartFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_view_cart));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(fragment == null){
            fragment = new ViewCartFragment();
            transaction.replace(R.id.main_container, fragment, getString(R.string.fragment_view_cart));
            transaction.addToBackStack(getString(R.string.fragment_view_cart));
            transaction.commit();
        }
    }

    @Override
    public void inflateViewProductFragment(Product product) {
        Log.d(TAG, "inflateViewProductFragment: called.");

        ViewProductFragment fragment = new ViewProductFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.intent_product), product);
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, fragment, getString(R.string.fragment_view_product));
        transaction.addToBackStack(getString(R.string.fragment_view_product));
        transaction.commit();

    }

    @Override
    public void showQuantityDialog() {
        Log.d(TAG, "showQuantityDialog: showing Quantity Dialog.");
        ChooseQuantityDialog dialog = new ChooseQuantityDialog();
        dialog.show(getSupportFragmentManager(), getString(R.string.dialog_choose_quantity));
    }

    /*
       Set quantity in ViewProductFragment
    */
    @Override
    public void setQuantity(int quantity) {
        Log.d(TAG, "selectQuantity: selected quantity: " + quantity);

        ViewProductFragment fragment = (ViewProductFragment)getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_view_product));
        if(fragment != null){
            fragment.mBinding.getProductView().setQuantity(quantity);
        }
    }

    @Override
    public void addToCart(Product product, int quantity) {
        Log.d(TAG, "addToCart: adding "+ quantity + " " + product.getTitle() + "to cart.");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        //add the new products serial number (if it hasn't already been added)
        Set<String> serialNumbers = preferences.getStringSet(PreferenceKeys.shopping_cart, new HashSet<String>());
        serialNumbers.add(String.valueOf(product.getSerial_number()));
        editor.putStringSet(PreferenceKeys.shopping_cart, serialNumbers);
        editor.commit();

        //add the quantity
        int currentQuantity = preferences.getInt(String.valueOf(product.getSerial_number()), 0);

        //commit the updated quantity
        editor.putInt(String.valueOf(product.getSerial_number()), (currentQuantity + quantity));
        editor.commit();

        //reset the quantity in ViewProductFragment
        setQuantity(1);

        //update the bindings
        getShoppingCart();

        // notify the user
        Toast.makeText(this, "added to cart", Toast.LENGTH_SHORT).show();
    }

    /*
        Updates the quantity of an item in the cart (ViewCartFragment)
     */
    @Override
    public void updateQuantity(Product product, int quantity) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        //add the quantity
        int currentQuantity = preferences.getInt(String.valueOf(product.getSerial_number()), 0);

        //commit the updated quantity
        editor.putInt(String.valueOf(product.getSerial_number()), (currentQuantity + quantity));
        editor.commit();

        getShoppingCart();
    }

    @Override
    public void removeCartItem(CartItem cartItem){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        editor.remove(String.valueOf(cartItem.getProduct().getSerial_number()));
        editor.commit();

        Set<String> serialNumbers = preferences.getStringSet(PreferenceKeys.shopping_cart, new HashSet<String>());
        if(serialNumbers.size() == 1){
            editor.remove(PreferenceKeys.shopping_cart);
            editor.commit();
        }
        else{
            serialNumbers.remove(String.valueOf(cartItem.getProduct().getSerial_number()));
            editor.putStringSet(PreferenceKeys.shopping_cart, serialNumbers);
            editor.commit();
        }

        getShoppingCart();

        //remove the item from the list in ViewCartFragment
        ViewCartFragment fragment = (ViewCartFragment)getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_view_cart));
        if(fragment != null){
            fragment.updateCartItems();
        }
    }


    @Override
    public void onBackPressed() {
        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
        Log.d(TAG, "onBackPressed: backstack count: " + backStackCount);
        if (backStackCount == 0 && mClickToExit) {
            super.onBackPressed();
        }
        if (backStackCount == 0 && !mClickToExit) {
            Toast.makeText(this, "1 more click to exit.", Toast.LENGTH_SHORT).show();
            mClickToExit = true;
        }
        else{
            mClickToExit = false;
            super.onBackPressed();
        }
    }
}











