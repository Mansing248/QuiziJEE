package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.Activities.ScoreActivity;
import com.example.myapplication.Activities.SetsActivity;
import com.example.myapplication.Models.QuestionModel;
import com.example.myapplication.databinding.ActivityQuestionBinding;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {

    ArrayList<QuestionModel> list = new ArrayList<>();

    private int count = 0;
    private int position = 0;
    private int score = 0;
    CountDownTimer timer;

    ActivityQuestionBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        
        resetTimer();
        timer.start();

        String setName = getIntent().getStringExtra("set");

        if(setName.equals("SET - 1")){
            setOne();
            
        } else if (setName.equals("SET - 2")) {

            setTwo();
        }

        for (int i =0;i<4;i++){

            binding.optionContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    checkAnswer((Button) view);
                }
            });

        }

        playAnimation(binding.Question,0,list.get(position).getQuestion());

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (timer !=null){
                    timer.cancel();
                }

                timer.start();
                binding.btnNext.setEnabled(false);
                binding.btnNext.setAlpha((float) 0.3);
                enableOption(true);
                position ++;

                if (position ==list.size()){

                    Intent intent = new Intent(QuestionActivity.this, ScoreActivity.class);
                    intent.putExtra("score",score);
                    intent.putExtra("total",list.size());
                    startActivity(intent);;
                    finish();
                    return;

                }

                count = 0;
                playAnimation(binding.Question,0,list.get(position).getQuestion());
                

            }
        });

    }

    private void resetTimer() {



        timer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long l) {

                binding.timer.setText(String.valueOf(l/1000));
            }

            @Override
            public void onFinish() {

                Dialog dialog = new Dialog(QuestionActivity.this);
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.timeout_dialog);
                dialog.findViewById(R.id.tryAgain).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(QuestionActivity.this, SetsActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                dialog.show();
            }
        };
    }

    private void playAnimation(View view, int value, String data) {

        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(@NonNull Animator animator) {
                        
                        if (value ==0 && count <4){
                            
                            String option = "";
                            
                            if(count ==0){
                                
                                option = list.get(position).getOptionA();
                            } else if (count ==1) {

                                option = list.get(position).getOptionB();
                            } else if (count ==2) {

                                option = list.get(position).getOptionC();

                            } else if (count ==3) {
                                option = list.get(position).getOptionD();
                            }

                            playAnimation(binding.optionContainer.getChildAt(count),0,option);
                            count ++;
                        }

                    }

                    @Override
                    public void onAnimationEnd(@NonNull Animator animator) {

                        if (value==0){


                            try {
                                ((TextView)view).setText(data);
                                binding.totalQuestion.setText(position+1+"/"+list.size());

                            } catch (Exception e) {

                                ((Button)view).setText(data);
                            }

                            view.setTag(data);
                            playAnimation(view, 1,data);


                        }
                    }

                    @Override
                    public void onAnimationCancel(@NonNull Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(@NonNull Animator animator) {

                    }
                });

    }

    private void enableOption(boolean enable) {

        for (int i =0;i<4;i++){

            binding.optionContainer.getChildAt(i).setEnabled(enable);

            if(enable){

                binding.optionContainer.getChildAt(i).setBackgroundResource(R.drawable.btn_opt);
            }
        }
    }

    private void checkAnswer(Button selectedOption) {

        if(timer !=null){

            timer.cancel();
        }

        binding.btnNext.setEnabled(true);
        binding.btnNext.setAlpha(1);

        if (selectedOption.getText().toString().equals(list.get(position).getCorrectAnswer())){


            score ++;
            selectedOption.setBackgroundResource(R.drawable.right_ans);
        }
        else {
            selectedOption.setBackgroundResource(R.drawable.btn_share);

            Button correctOption = (Button) binding.optionContainer.findViewWithTag(list.get(position).getCorrectAnswer());
            correctOption.setBackgroundResource(R.drawable.right_ans);
        }



    }

    private void setTwo() {

        list.add(new QuestionModel("Which of the following correctly defines power?",
                "The rate at which work is done",
                "The amount of work done",
                "The force applied to do work",
                "The distance covered in doing work",
                "The rate at which work is done"));

        list.add(new QuestionModel("Which of the following statements about the refraction of light is correct?",
                "Light bends towards the normal when entering a denser medium",
                "Light bends away from the normal when entering a rarer medium",
                "The angle of refraction is always smaller than the angle of incidence",
                "The speed of light increases when it enters a denser medium",
                "Light bends towards the normal when entering a denser medium"));

        list.add(new QuestionModel("Which of the following statements about nuclear reactions is correct?",
                "Nuclear reactions involve changes in the electron configuration",
                "Nuclear reactions release energy through chemical bonds",
                "Nuclear reactions can be initiated by temperature changes",
                "Nuclear reactions involve changes in the nucleus of an atom",
                "Nuclear reactions involve changes in the nucleus of an atom"));

        list.add(new QuestionModel("Which of the following quantities remains constant for an object in uniform circular motion?",
                "Velocity",
                "Acceleration",
                "Net force",
                "Momentum",
                "Net force"));

        list.add(new QuestionModel("Which of the following statements about waves is correct?",
                "Transverse waves require a medium to propagate",
                "Longitudinal waves exhibit oscillations perpendicular to the direction of propagation",
                "Amplitude is the distance between two consecutive crests or troughs of a wave",
                "Frequency is the time taken for one complete oscillation of a wave",
                "Frequency is the time taken for one complete oscillation of a wave"));

    }

    private void setOne() {

        list.add(new QuestionModel("What is the SI unit of electric charge?",
                "Ampere",
                "Coulomb",
                "Ohm",
                "Volt",
                "Coulomb"));

        list.add(new QuestionModel("Which of the following is an example of simple harmonic motion?",
                "A pendulum swinging back and forth",
                "A ball rolling down a hill",
                "A car moving in a straight line",
                "A satellite orbiting the Earth",
                "A pendulum swinging back and forth"));

        list.add(new QuestionModel("Which of the following correctly represents Ohm's Law?",
                "V = IR",
                "R = VI",
                "I = VR",
                "I = RV",
                "V = IR"));

        list.add(new QuestionModel("Which of the following statements about the conservation of energy is correct?",
                "Energy can be created but not destroyed",
                "Energy is always conserved in any process",
                "Energy is conserved only in mechanical systems",
                "Energy is conserved only in closed systems",
                "Energy is always conserved in any process"));

        list.add(new QuestionModel("Which of the following electromagnetic waves has the highest frequency?",
                "Radio waves",
                "Microwaves",
                "X-rays",
                "Gamma rays",
                "Gamma rays"));


    }
}