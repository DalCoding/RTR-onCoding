package com.example.rotory.Interface;

public interface OnUserActItemClickListener {
    //activity로 변경.... 매번 다시 짜줘야함
    // onclick만 사용!
    public void OnStarClicked();

    public void OnLikeClicked(String contentsId, String userId);

    public void OnFlagClicked();

    public void OnLinkClicked();
}
