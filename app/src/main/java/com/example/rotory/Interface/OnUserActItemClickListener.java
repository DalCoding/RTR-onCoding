package com.example.rotory.Interface;

import java.util.Map;

public interface OnUserActItemClickListener {

    public void OnStarClicked(String savedUserId, String myUserId);

    public void OnLikeClicked(String contentsId, Map<String, Object> contentsList, String userId);

    public void OnFlagClicked(String contentsId, Map<String, Object> contentsList, String userId);

    public void OnLinkClicked();
}
