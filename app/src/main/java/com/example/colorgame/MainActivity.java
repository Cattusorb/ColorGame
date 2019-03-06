package com.example.colorgame;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private Map<Integer, String> backgroundColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize our map between EditTexts and the background colors
        final String DEFAULT_BACKGROUND_COLOR = getString(R.string.default_map_value);
        backgroundColors = new HashMap<>();
        backgroundColors.put(R.id.colorbar_1, DEFAULT_BACKGROUND_COLOR);
        backgroundColors.put(R.id.colorbar_2, DEFAULT_BACKGROUND_COLOR);
        backgroundColors.put(R.id.colorbar_3, DEFAULT_BACKGROUND_COLOR);
        backgroundColors.put(R.id.colorbar_4, DEFAULT_BACKGROUND_COLOR);
        backgroundColors.put(R.id.colorbar_5, DEFAULT_BACKGROUND_COLOR);
        backgroundColors.put(R.id.colorbar_6, DEFAULT_BACKGROUND_COLOR);

        randomizeColors();

        addListeners();
    }

    /**
     * Adds a listener to the EditTexts to check the answers
     */
    private void addListeners() {
        EditText.OnEditorActionListener catListener = new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                checkAnswer(v);

                // Dismiss keyboard
                InputMethodManager imm =(InputMethodManager)
                        getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                return true;
            }
        };

        for(int ID : backgroundColors.keySet()) {
            EditText bar = findViewById(ID);
            bar.setOnEditorActionListener(catListener);
        }
    }

    /**
     * Checks if the text entered in the view matches the background color
     * @param view
     */
    private void checkAnswer(TextView view) {
        // Get the text entered by the user
        String answer = view.getText().toString();

        // Get the name of the background color
        String correct = backgroundColors.get(view.getId());

        // Compare and print out success or failure

        if(answer.equalsIgnoreCase(correct)) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            view.setEnabled(false);
        } else {
            Toast.makeText(this, "Try Again!", Toast.LENGTH_SHORT).show();
            view.setText("");
        }

    }

    /**
     * Pass through onClick called when the user wants to randomize the color bars
     * @param view
     */
    public void shuffle(View view) {
        randomizeColors();
    }

    /**
     * Randomizes the color bars that appear on the screen and updates the map storing the
     * background color names
     */
    private void randomizeColors() {
        // Read in our color names and randomize them
        String[] colorArray = getResources().getStringArray(R.array.background_colors);
        List<String> colorList = Arrays.asList(colorArray);
        Collections.shuffle(colorList);

        // Put the color names into the map
        int n = 0;
        for(int barID : backgroundColors.keySet()) {
            String colorName = colorList.get(n % colorList.size());
            backgroundColors.put(barID, colorName);
            n++;
        }

        // Use the color names in the map to actually set the background colors
        setBackgroundColorsFromMap();
    }

    private void setBackgroundColorsFromMap() {
        for (int barID : backgroundColors.keySet()) {
            EditText bar = findViewById(barID);
            String backgroundColorName = backgroundColors.get(barID);
            int colorID = getResources().getIdentifier(backgroundColorName, "color", this.getPackageName());
            int color = ContextCompat.getColor(this, colorID);
            bar.setBackgroundColor(color);
            bar.setEnabled(true);
        }
    }
}
