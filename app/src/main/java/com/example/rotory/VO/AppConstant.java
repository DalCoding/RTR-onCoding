package com.example.rotory.VO;

import com.example.rotory.R;

public class AppConstant {
    public static final int loginCode = 3000;
    public static final int themeCode = 2000;
    public static final int mainCode = 1000;
    public static final int searchCode = 1100;
    public static final int profileEditCode = 4120;
    public static final int findWithPhoneCode = 3221;

    public static final int MILLISINFUTURE = 120 * 1000; //총 시간
    public static final int COUNT_DOWN_INTERVAL = 1000; //onTick 메소드를 호출할 간격 (1초)

    public static final String REGEX_PATTERN = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$";
    public static final String REGEX_NUMBER = "^(?=.*[0-9])[0-9]{9,12}$";

    public AppConstant() {
    }

    public int getUserLevelImage(String userLevel) {
        switch (userLevel){
            case "어린다람쥐":
                return R.drawable.level2;
            case "학생다람쥐":
                return R.drawable.level3;
            case "어른다람쥐" :
                return R.drawable.level4;
            case "박사다람쥐" :
                return R.drawable.level5;
            case "다람쥐의 신":
                return R.drawable.level6;
            default:
                return R.drawable.level1;
        }
    }
}
