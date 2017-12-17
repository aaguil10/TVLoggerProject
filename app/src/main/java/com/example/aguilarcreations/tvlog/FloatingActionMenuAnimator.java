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


    private static final int ANIMATION_DISTANCE = 200;
    private static final int ANIMATION_DURATION = 200; //2 ms



    public FloatingActionMenuAnimator(){
    }

    static public FloatingActionMenuAnimator build(View v, BtnData[] data){
        instance.actionButton = v.findViewById(R.id.showdetails_action_btn);
        instance.actionButton.setVisibility(View.VISIBLE);

        instance.actionMenu = v.findViewById(R.id.showdetails_floating_action_button_menu);


        instance.buttons = new ArrayList<>();
        int counter = 1;
        for(BtnData btn_data:data) {
            ViewHolder button = new ViewHolder(counter);
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
                button.card.setTranslationY(button.card.getY()-(ANIMATION_DISTANCE*counter));
                button.text.setText(btn_data.label);
            }

            instance.buttons.add(button);


            counter++;
        }
        instance.actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (instance.actionMenu.getVisibility() == View.GONE) {
                    instance.open();
                } else {
                    instance.close();
                }
            }
        });
        return instance;
    }


    private void open(){
        for (final ViewHolder button:buttons) {
            actionMenu.setVisibility(View.VISIBLE);
            ObjectAnimator animation = ObjectAnimator.ofFloat(button.btn, "translationY", -ANIMATION_DISTANCE * button.index);
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


    }

    private void close(){
        for (final ViewHolder button:buttons) {
            button.card.setVisibility(View.GONE);
            ObjectAnimator animation = ObjectAnimator.ofFloat(button.btn, "translationY", 1);
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
    }


    static private class ViewHolder{
        int index;
        FloatingActionButton btn;
        CardView card;
        TextView text;

        public ViewHolder(int index){
            this.index = index;
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
