package com.example.rotory.Interface;

import java.util.Map;

public interface OnUserActItemClickListener {
    //activity로 변경.... 매번 다시 짜줘야함
    // onclick만 사용!
    public void OnStarClicked(String savedUserId, String myUserId);

    public void OnLikeClicked(String contentsId, Map<String, Object> contentsList, String userId);

    public void OnFlagClicked(String contentsId, Map<String, Object> contentsList, String userId);

    public void OnLinkClicked();
}
