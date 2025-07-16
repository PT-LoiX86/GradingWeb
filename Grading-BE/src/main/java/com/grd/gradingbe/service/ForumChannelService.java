package com.grd.gradingbe.service;

import com.grd.gradingbe.dto.request.ForumChannelRequest;
import com.grd.gradingbe.dto.response.ChannelResponse;
import com.grd.gradingbe.dto.response.PageResponse;

public interface ForumChannelService
{
    PageResponse<ChannelResponse> getChannels(int page, int size, String sortBy, String sortDir, String search);

    ChannelResponse createChannel(ForumChannelRequest request);

    ChannelResponse updateChannel(Long id, ForumChannelRequest request);

    void deleteChannel(Long id);

    ChannelResponse getChannel(Long id);
}
