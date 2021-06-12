package com.example.jerusalemnewsapp.rclass;

import com.example.jerusalemnewsapp.R;

public class Constant {

    public static String POST = "Post";
    public static String POST_IMAGES = "PostsImages";

    public static int nav_clicked = 0;
    public static Boolean isNavClicked = false;

    public static Boolean isToggle = true;
    public static int color = 0xff3b5998;
    public static int theme = R.style.AppTheme;
    public static float SMALL_FONT = 0.7f;
    public static float NORMAL_FONT = 1.0f;
    public static float LARGE_FONT = 1.5f;

    public enum COLORS {
        red(0xffF44336),
        pink(0xffE91E63),
        Purple(0xff9C27B0),
        DeepPurple(0xff673AB7),
        Indigo(0xff3F51B5),
        Blue(0xff2196F3),
        LightBlue(0xff03A9F4),
        Cyan(0xff00BCD4),
        Teal(0xff009688),
        Green(0xff4CAF50),
        LightGreen(0xff8BC34A),
        Lime(0xffCDDC39),
        Yellow(0xffFFEB3B),
        Amber(0xffFFC107),
        Orange(0xffFF9800),
        DeepOrange(0xffFF5722),
        Brown(0xff795548),
        Grey(0xff9E9E9E),
        BlueGray(0xff607D8B),
        Black(0xff000000),
        White(0xffffffff);

        public final int value;

        COLORS(int value) {
            this.value = value;
        }
    }
}
