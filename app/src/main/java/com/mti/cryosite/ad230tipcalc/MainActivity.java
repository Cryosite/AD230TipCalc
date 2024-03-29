package com.mti.cryosite.ad230tipcalc;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

//main Activity class for the TipCalculator
public class MainActivity extends Activity
{
    //constants used when saving/restoring state
    private static final String BILL_TOTAL = "BILL_TOTAL";
    private static final String CUSTOM_PERCENT = "CUSTOM_PERCENT";

    private double currentBillTotal; //bill amount entered by the user
    private int currentCustomPercent; //tip % set with the SeekBar
    private EditText tip10EditText; //displays 10% tip
    private EditText total10EditText; //displays total with 10% tip
    private EditText tip15EditText; //displays 15% tip
    private EditText total15EditText; //displays total with 15% tip
    private EditText billEditText; //accepts user input for bill total
    private EditText tip20EditText; //displays 20% tip
    private EditText total20EditText;  //displays total with 20% tip
    private TextView customTipTextView; //displays custom tip percentage
    private EditText tipCustomEditText; //displays custom tip amount
    private EditText totalCustomEditText; //displays total with custom tip

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check if app just started or is being restored from memory
        //the app just started running
        if (savedInstanceState == null) {
            currentBillTotal = 0.0; //initialize the bill amount to zero
            currentCustomPercent = 18; //initialize the custom tip to 18%
        }//endif - (savedInstanceState == null)

        //app is being restored from memory, not executed from scratch
        else {
            //initialize the bill amount to saved amount
            currentBillTotal = savedInstanceState.getDouble(BILL_TOTAL);

            //initialize the custom tip to saved tip percent
            currentCustomPercent =
                    savedInstanceState.getInt(CUSTOM_PERCENT);
        }//endelse - (savedInstanceState == null)

        //get references to the 10%, 15% and 20% tip and total EditTexts
        tip10EditText = (EditText) findViewById(R.id.tip10EditText);
        total10EditText = (EditText) findViewById(R.id.total10EditText);
        tip15EditText = (EditText) findViewById(R.id.tip15EditText);
        total15EditText = (EditText) findViewById(R.id.total15EditText);
        tip20EditText = (EditText) findViewById(R.id.tip20EditText);
        total20EditText = (EditText) findViewById(R.id.total20EditText);

        //get the TextView displaying the custom tip percentage
        customTipTextView = (TextView) findViewById(R.id.customTipTextView);

        //get the custom tip and total EditTexts
        tipCustomEditText = (EditText) findViewById(R.id.tipCustomEditText);
        totalCustomEditText =
                (EditText) findViewById(R.id.totalCustomEditText);

        //get the billEditText
        billEditText = (EditText) findViewById(R.id.billEditText);

        //billEditTextWatcher handles billEditText's onTextChanged event
        billEditText.addTextChangedListener(billEditTextWatcher);

        //get the SeekBar used to set the custom tip amount
        SeekBar customSeekBar = (SeekBar) findViewById(R.id.customSeekBar);
        customSeekBar.setOnSeekBarChangeListener(customSeekBarListener);
    }//onCreate(Bundle savedInstanceState)

    //updates 10, 15 and 20 percent tip EditTexts
    private void updateStandard() {
        //calculate bill total with a ten percent tip
        double tenPercentTip = currentBillTotal * .1;
        double tenPercentTotal = currentBillTotal + tenPercentTip;

        //set tipTenEditText's text to tenPercentTip
        tip10EditText.setText(String.format("%.02f", tenPercentTip));

        //set totalTenEditText's text to tenPercentTotal
        total10EditText.setText(String.format("%.02f", tenPercentTotal));

        //calculate bill total with a fifteen percent tip
        double fifteenPercentTip = currentBillTotal * .15;
        double fifteenPercentTotal = currentBillTotal + fifteenPercentTip;

        //set tipFifteenEditText's text to fifteenPercentTip
        tip15EditText.setText(String.format("%.02f", fifteenPercentTip));

        //set totalFifteenEditText's text to fifteenPercentTotal
        total15EditText.setText(
                String.format("%.02f", fifteenPercentTotal));

        //calculate bill total with a twenty percent tip
        double twentyPercentTip = currentBillTotal * .20;
        double twentyPercentTotal = currentBillTotal + twentyPercentTip;

        //set tipTwentyEditText's text to twentyPercentTip
        tip20EditText.setText(String.format("%.02f", twentyPercentTip));

        //set totalTwentyEditText's text to twentyPercentTotal
        total20EditText.setText(String.format("%.02f", twentyPercentTotal));
    }//updateStandard(

    //updates the custom tip and total EditTexts
    private void updateCustom() {
        //set customTipTextView's text to match the position of the SeekBar
        customTipTextView.setText(currentCustomPercent + "%");

        //calculate the custom tip amount
        double customTipAmount =
                currentBillTotal * currentCustomPercent * .01;

        //calculate the total bill, including the custom tip
        double customTotalAmount = currentBillTotal + customTipAmount;

        //display the tip and total bill amounts
        tipCustomEditText.setText(String.format("%.02f", customTipAmount));
        totalCustomEditText.setText(
                String.format("%.02f", customTotalAmount));
    }//updateCustom()

    //save values of billEditText and customSeekBar
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putDouble(BILL_TOTAL, currentBillTotal);
        outState.putInt(CUSTOM_PERCENT, currentCustomPercent);
    }//onSaveInstanceState(Bundle outState)

    //called when the user changes the position of SeekBar
    private OnSeekBarChangeListener customSeekBarListener =
            new OnSeekBarChangeListener() {
                //update currentCustomPercent, then call updateCustom
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    //sets currentCustomPercent to position of the SeekBar's thumb
                    currentCustomPercent = seekBar.getProgress();
                    updateCustom(); //update EditTexts for custom tip and total
                }//onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    //stub
                }//onStartTrackingTouch(SeekBar seekBar)

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    //stub
                }//onStopTrackingTouch(SeekBar seekBar)
            };//customSeekBarListener

    //event-handling object that responds to billEditText's events
    private TextWatcher billEditTextWatcher = new TextWatcher() {
        //called when the user enters a number
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //convert billEditText's text to a double
            try {
                currentBillTotal = Double.parseDouble(s.toString());
            }//endtry
            catch (NumberFormatException e) {
                currentBillTotal = 0.0; //default if an exception occurs
            }//endcatch - (NumberFormatException e)

            //update the standard and custom tip EditTexts
            updateStandard(); //update the 10, 15 and 20% EditTexts
            updateCustom(); //update the custom tip EditTexts
        }//onTextChanged(CharSequence s, int start, int before, int count)

        @Override
        public void afterTextChanged(Editable s) {
            //stub
        }//afterTextChanged(Editable s)

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //stub
        }//beforeTextChanged(CharSequence s, int start, int count, int after)
    };//billEditTextWatcher
}//class

/*
        Modified from source code provided by Deitel & Associates, Inc. and
        Pearson Education, Inc.
*/
