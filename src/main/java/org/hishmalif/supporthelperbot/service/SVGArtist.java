package org.hishmalif.supporthelperbot.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SVGArtist extends Artist {

    private String path = "/Levels"; //TODO Сделать ручную подстановку по возможным совпадениям (автоопределение содержимого объектов)

    public SVGArtist() {
        super(8, 8);
    }

    public SVGArtist(Integer width, Integer height) {
        super(width, height);
    }

    public String getSchema(String json, String origin, String params) {


        return null;
    }
}