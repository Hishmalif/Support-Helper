package org.hishmalif.supporthelperbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public abstract class Artist {
    private final int width;
    private final int height;
}