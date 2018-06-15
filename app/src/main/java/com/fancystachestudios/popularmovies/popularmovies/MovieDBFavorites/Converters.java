package com.fancystachestudios.popularmovies.popularmovies.MovieDBFavorites;

import android.arch.persistence.room.TypeConverter;

/**
 * Created by napuk on 6/14/2018.
 */

public class Converters {

    @TypeConverter
    public static int[] fromStringToIntArray(String arrayString){
        String[] splitString = arrayString.split(",");
        int[] newIntArray = new int[splitString.length];
        for(int i = 0; i < splitString.length; i++){
            newIntArray[i] = Integer.valueOf(splitString[i]);
        }
        return newIntArray;
    }

    @TypeConverter
    public static String fromIntArrayToString(int[] intArray){
        String newString = "";
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < intArray.length; i++){
            if(i == 0){
                stringBuilder.append(String.valueOf(intArray[i]));
            }else{
                stringBuilder.append(",").append(String.valueOf(intArray[i]));
            }
        }
        return stringBuilder.toString();
    }
}
