package com.example.aguilarcreations.tvlog;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Work on 12/13/17.
 */

public class FloatingActionMenuAnimator {
    View actionMenu;
    ArrayList<ViewHolder> buttons;
    FloatingActionButton actionButton;
    private static final FloatingActionMenuAnimator instance = new FloatingActionMenuAnimator();


    int ANIMATION_DISTANCE = 200;
    int ANIMATION_DURATION = 200; //2 ms



    public FloatingActionMenuAnimator(){
    }

    static public FloatingActionMenuAnimator build(View v, BtnData[] data){
        instance.actionButton = v.findViewById(R.id.showdetails_action_btn);
        instance.actionButton.setVisibility(View.VISIBLE);

        instance.actionMenu = v.findViewById(R.id.showdetails_floating_action_button_menu);


        instance.buttons = new ArrayList<>();
        int counter = 1;
        for(BtnData btn_data:data) {
            ViewHolder button = new ViewHolder();
            int btn_resID = instance.actionMenu.getResources().getIdentifier("floting_action_btn"+ counter,
                    "id", v.getContext().getPackageName());
            button.btn = instance.actionMenu.findViewById(btn_resID);

            int card_resID = instance.actionMenu.getResources().getIdentifier("floting_action_btn"+ counter+"_card",
                    "id", v.getContext().getPackageName());
            button.card = instance.actionMenu.findViewById(card_resID);

            int text_resID = instance.actionMenu.getResources().getIdentifier("floting_action_btn"+ counter+"_text",
                    "id", v.getContext().getPackageName());
            button.text = instance.actionMenu.findViewById(text_resID);

            if(btn_data.icon != -1) {
                button.btn.setImageResource(btn_data.icon);
            }
            if(btn_data.label != null){
                button.card.setTranslationY(button.card.getY()-200);
                button.text.setText(btn_data.label);
            }

            instance.buttons.add(button);
            counter++;
        }

        instance.actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (ViewHolder button : instance.buttons) {
                    if (instance.actionMenu.getVisibility() == View.GONE) {
                        instance.open(button);
                    } else {
                        instance.close(button);
                    }
                }
            }
        });
        return instance;
    }


    private void open(final ViewHolder button){
        actionMenu.setVisibility(View.VISIBLE);
        ObjectAnimator animation = ObjectAnimator.ofFloat(button.btn, "translationY", -200f);
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                button.card.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animation.setDuration(ANIMATION_DURATION);
        animation.start();




    }

    private void close(final ViewHolder button){
        button.card.setVisibility(View.GONE);
        ObjectAnimator animation = ObjectAnimator.ofFloat(button.btn, "translationY", 1f);
        animation.setDuration(ANIMATION_DURATION);
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                actionMenu.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animation.start();
    }


    static private class ViewHolder{
        FloatingActionButton btn;
        CardView card;
        TextView text;

        public ViewHolder(){

        }

    }

    static public class BtnData{
        public String label = null;
        public int icon = -1;

        public BtnData(String label, int icon){
            this.label = label;
            this.icon = icon;
        }
    }
}
