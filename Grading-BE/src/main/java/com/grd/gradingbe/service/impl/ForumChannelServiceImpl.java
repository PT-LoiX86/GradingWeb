package com.grd.gradingbe.service.impl;

import com.grd.gradingbe.dto.response.ChannelResponse;
import com.grd.gradingbe.dto.response.PageResponse;
import com.grd.gradingbe.model.ForumChannel;
import com.grd.gradingbe.repository.ForumChannelRepository;
import com.grd.gradingbe.service.ForumChannelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForumChannelServiceImpl implements ForumChannelService
{

    private final ForumChannelRepository forumChannelRepository;

    public ForumChannelServiceImpl(ForumChannelRepository forumChannelRepository) {
        this.forumChannelRepository = forumChannelRepository;
    }

    public PageResponse<ChannelResponse> getChannels(int page, int size, String sortBy, String sortDir, String search)
    {
        Sort sorter = sortBy.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sorter);
        Page<ForumChannel> channelPages = forumChannelRepository.findAll(pageable);
        List<ForumChannel> channelContents = channelPages.getContent();

        return PageResponse.<ChannelResponse>builder()
                .content(responseMapping(channelContents))
                .page(channelPages.getNumber())
                .size(channelPages.getSize())
                .totalElements((int) channelPages.getTotalElements())
                .totalPages(channelPages.getTotalPages())
                .last(channelPages.isLast())
                .build();
    }

    private List<ChannelResponse> responseMapping(List<ForumChannel> channelContents) {
        return channelContents.stream()
                .map(this::responseMapping)
                .toList();
    }

    private ChannelResponse responseMapping(ForumChannel forumChannel) {
        return ChannelResponse.builder()
                .id(forumChannel.getId())
                .name(forumChannel.getName())
                .description(forumChannel.getDescription())
                .slug(forumChannel.getSlug())
                .is_active(forumChannel.getIs_active())
                .createdAt(String.valueOf(forumChannel.getCreatedAt()))
                .createdBy(forumChannel.getCreatedBy())
                .updatedAt(String.valueOf(forumChannel.getUpdatedAt()))
                .updatedBy(forumChannel.getUpdatedBy())
                .build();
    }
}