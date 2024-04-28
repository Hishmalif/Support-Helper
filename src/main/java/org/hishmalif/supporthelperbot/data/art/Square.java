package org.hishmalif.supporthelperbot.data.art;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Square extends Figure {
    private final Point point;
    private String row;
    private String number;
}