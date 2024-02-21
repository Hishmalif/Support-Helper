package org.hishmalif.supporthelperbot.controller.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Answers {
    START("\uD83D\uDC4B Greetings, dear friend! \uD83D\uDC4B\n" +
            "I can carry out various instructions, such as:\n" +
            "Drawing in SVG;\n" +
            "Formatting, transformation and extraction of data;\n" +
            "Ready to remember your answers in key-value format;\n" +
            "I can convert coordinates into addresses and vice versa;\n" +
            "And much more!\n" +
            "\uD83D\uDC40 Go to the menu and see my capabilities! \uD83D\uDC40"),
    ABOUT("");
    private final String value;
}