package com.example.jerusalemnewsapp.rclass;

import com.example.jerusalemnewsapp.R;
import com.example.jerusalemnewsapp.rclass.Constant;

public class Methods {

    public int setColorTheme(int color) {

        //                        red(0xffE91E63),
//                                pink(0xffE91E63),
//                                Purple(0xff9C27B0),
//                                DeepPurple(0xff673AB7),
//                                Indigo(0xff3F51B5),
//                                Blue(0xff2196F3),
//                                LightBlue(0xff03A9F4),
//                                Cyan(0xff00BCD4),
//                                Teal(0xff009688),
//                                Green(0xff4CAF50),
//                                LightGreen(0xff8BC34A),
//                                Lime(0xffCDDC39),
//                                Yellow(0xffFFEB3B),
//                                Amber(0xffFFC107),
//                                Orange(0xffFF9800),
//                                DeepOrange(0xffFF5722),
//                                Brown(0xff795548),
//                                Grey(0xff9E9E9E),
//                                BlueGray(0xff607D8B),
//                                Black(0xff000000),
//                                White(0xffffffff);

        int appTheme;
        if (color == Constant.COLORS.red.value)
            appTheme = R.style.AppTheme_red;
        else if (color == Constant.COLORS.pink.value)
            appTheme = R.style.AppTheme_pink;
        else if (color == Constant.COLORS.Purple.value)
            appTheme = R.style.AppTheme_darpink;
        else if (color == Constant.COLORS.Purple.value)
            appTheme = R.style.AppTheme_violet;
        else if (color == Constant.COLORS.Purple.value)
            appTheme = R.style.AppTheme_darpink;
        else if (color == Constant.COLORS.Purple.value)
            appTheme = R.style.AppTheme_darpink;
        else if (color == Constant.COLORS.Purple.value)
            appTheme = R.style.AppTheme_darpink;
        else if (color == Constant.COLORS.Purple.value)
            appTheme = R.style.AppTheme_darpink;
        else if (color == Constant.COLORS.Purple.value)
            appTheme = R.style.AppTheme_darpink;
        else if (color == Constant.COLORS.Purple.value)
            appTheme = R.style.AppTheme_darpink;
        else
            appTheme = R.style.AppTheme_brown;

        return appTheme;
//        switch (color) {
//            case 0xffF44336:
//                Constant.theme = R.style.AppTheme_red;
//                break;
//            case 0xffE91E63:
//                Constant.theme = R.style.AppTheme_pink;
//                break;
//            case 0xff9C27B0:
//                Constant.theme = R.style.AppTheme_darpink;
//                break;
//            case 0xff673AB7:
//                Constant.theme = R.style.AppTheme_violet;
//                break;
//            case 0xff3F51B5:
//                Constant.theme = R.style.AppTheme_blue;
//                break;
//            case 0xff03A9F4:
//                Constant.theme = R.style.AppTheme_skyblue;
//                break;
//            case 0xff4CAF50:
//                Constant.theme = R.style.AppTheme_green;
//                break;
//            case 0xffFF9800:
//                Constant.theme = R.style.AppTheme;
//                break;
//            case 0xff9E9E9E:
//                Constant.theme = R.style.AppTheme_grey;
//                break;
//            case 0xff795548:
//                Constant.theme = R.style.AppTheme_brown;
//                break;
//            default:
//                Constant.theme = R.style.AppTheme;
//                break;
//        }
    }
}