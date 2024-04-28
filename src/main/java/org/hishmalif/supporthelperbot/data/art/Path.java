package org.hishmalif.supporthelperbot.data.art;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.ArrayList;

@Data
@EqualsAndHashCode(callSuper = false)
public class Path extends Figure {
    private final List<Point> points = new ArrayList<>();
}