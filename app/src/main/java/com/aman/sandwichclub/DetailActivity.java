package com.aman.sandwichclub;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.aman.sandwichclub.model.Sandwich;
import com.aman.sandwichclub.utils.JsonUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;


public class DetailActivity extends AppCompatActivity implements Animation.AnimationListener {

    public static final String EXTRA_POSITION = "extra_position";


    private static final int DEFAULT_POSITION = -1;
    private ImageView dimageView;
    private View bottomScrimView;

    private ProgressBar progressBar;

    TextView titleTV, descriptionTV, doriginTV, dingredientsTV, dalsoKnowTV;

    boolean enableBackBtn = false;

    LinearLayout descriptionBox, ingredientsBox, alsoKnowAsBox;
    Animation fadeInAnim, slideDownAnim, slideUpAnim, slideUpAnimOne, slideOutDown, slideOutDownOne, slideOutUp, fadeOutAnim;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //ActionBar and its title
        ActionBar actionBar = getSupportActionBar();



        //enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parsesandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }
        initViews();
        populateUI(sandwich);
        dimageView.setTransitionName(String.valueOf(position));
        Picasso.get()
                .load(sandwich.getImage())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error_loding)
                .into(dimageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

        setTitle(sandwich.getMainName());

    }

    private void populateUI(Sandwich sandwich) {
        titleTV.setText(sandwich.getMainName());
        descriptionTV.setText(sandwich.getDescription());
        if (sandwich.getPlaceOfOrigin().isEmpty() || sandwich.getPlaceOfOrigin().equals(" ")) {
            doriginTV.setText(getResources().getString(R.string.not_avail));
        } else {
            doriginTV.setText(sandwich.getPlaceOfOrigin());
        }
        settingList(sandwich.getIngredients(), dingredientsTV);
        settingList(sandwich.getAlsoKnownAs(), dalsoKnowTV);

    }

    private void settingList(List<String> alsoKnownAs, TextView dalsoKnowTV) {
        if (alsoKnownAs.isEmpty()) {
            dalsoKnowTV.setText(getResources().getString(R.string.not_avail));
            return;
        }
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < alsoKnownAs.size(); i++) {
            data.append(alsoKnownAs.get(i));
            if (i != alsoKnownAs.size() - 1)
                data.append(",");
        }

        dalsoKnowTV.setText(data.toString().replace(",", "\n"));

    }

    private void initViews() {
        progressBar = findViewById(R.id.thumb_progressbar);

        titleTV = findViewById(R.id.title_tv);
        descriptionTV = findViewById(R.id.description_tv);
        doriginTV = findViewById(R.id.origin_tv);
        dingredientsTV = findViewById(R.id.ingredients_tv);
        dalsoKnowTV = findViewById(R.id.also_known_tv);

        dimageView = findViewById(R.id.image_iv);
        bottomScrimView = findViewById(R.id.bottom_scrim);
        descriptionBox = findViewById(R.id.description_box);
        ingredientsBox = findViewById(R.id.ingredients_box);
        alsoKnowAsBox = findViewById(R.id.also_known_as_box);

        fadeInAnim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeInAnim.setDuration(1000);
        fadeInAnim.setAnimationListener(this);

        slideDownAnim = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        slideDownAnim.setDuration(1000);
        slideDownAnim.setAnimationListener(this);

        slideUpAnim = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        slideUpAnim.setDuration(1000);
        slideUpAnim.setAnimationListener(this);

        slideUpAnimOne = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        slideUpAnimOne.setDuration(500);
        slideUpAnimOne.setAnimationListener(this);

        slideOutDown = AnimationUtils.loadAnimation(this, R.anim.slide_out_down);
        slideOutDown.setAnimationListener(this);

        slideOutDownOne = AnimationUtils.loadAnimation(this, R.anim.slide_out_down);
        slideOutDownOne.setAnimationListener(this);

        slideOutUp = AnimationUtils.loadAnimation(this, R.anim.slide_out_up);
        slideOutUp.setAnimationListener(this);

        fadeOutAnim = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        fadeOutAnim.setDuration(500);
        fadeOutAnim.setAnimationListener(this);


        bottomScrimView.startAnimation(fadeInAnim);

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == fadeInAnim) {
            descriptionBox.setVisibility(View.VISIBLE);
            descriptionBox.setAnimation(slideDownAnim);
        }
        if (animation == slideDownAnim) {
            ingredientsBox.setVisibility(View.VISIBLE);
            ingredientsBox.setAnimation(slideUpAnim);
        }
        if (animation == slideUpAnim) {
            alsoKnowAsBox.setVisibility(View.VISIBLE);
            alsoKnowAsBox.setAnimation(slideUpAnimOne);
            enableBackBtn = true;
        }
        if (animation == slideOutDown) {
            alsoKnowAsBox.setVisibility(View.INVISIBLE);
            ingredientsBox.setAnimation(slideOutDownOne);
        }
        if (animation == slideOutDownOne) {
            ingredientsBox.setVisibility(View.INVISIBLE);
            descriptionBox.setAnimation(slideOutUp);
        }
        if (animation == slideOutUp) {
            descriptionBox.setVisibility(View.INVISIBLE);
            bottomScrimView.startAnimation(fadeOutAnim);
        }
        if (animation == fadeOutAnim) {
            bottomScrimView.setVisibility(View.INVISIBLE);
            super.onBackPressed();
        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
    @Override
    public void onBackPressed() {

        if (enableBackBtn) {
            alsoKnowAsBox.startAnimation(slideOutDown);
            enableBackBtn = false;
        }


    }

}
