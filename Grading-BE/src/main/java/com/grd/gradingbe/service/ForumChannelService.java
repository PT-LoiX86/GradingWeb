package com.grd.gradingbe.service;

import com.grd.gradingbe.dto.response.ChannelResponse;
import com.grd.gradingbe.dto.response.PageResponse;

public interface ForumChannelService
{
    PageResponse<ChannelResponse> getChannels(int page, int size, String sortBy, String sortDir, String search);
}
