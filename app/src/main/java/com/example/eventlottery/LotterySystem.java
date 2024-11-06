package com.example.eventlottery;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 *
 */
public class LotterySystem {

    private int end;
    private int num;


    LotterySystem(int end, int num){
        this.end = end-1; // since indexing from 0
        this.num = num;
    }

    /**
     *
     * This method runs an algorithm that creates an array of randomly generated number which is the
     * index of the people from the waitlist that "won" the lottery
     * @return ArrayList<Integer> of index of users that "won" the lottery
     */
    public ArrayList<Integer> getwinnersList(){
        ArrayList<Integer> winnerList = new ArrayList<Integer>(num);
        for (int i = 0; i < num; ){
            int temp_rand =(int)(Math.random() * end);
            if(!winnerList.contains(temp_rand)){
                // If random num isn't in the list then we +1 out of end needed numbers
                winnerList.add(temp_rand);
                ++i;
            }
        }


        return winnerList;
    }



}
