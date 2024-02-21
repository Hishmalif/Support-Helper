package org.hishmalif.supporthelperbot.controller.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Answers {
    START("""
            \uD83D\uDC4B Greetings, dear friend! \uD83D\uDC4B
            I can carry out various instructions, such as:
            Drawing in SVG;
            Formatting, transformation and extraction of data;
            Ready to remember your answers in key-value format;
            I can convert coordinates into addresses and vice versa;
            And much more!
            \uD83D\uDC40 Go to the menu and see my capabilities! \uD83D\uDC40"""),
    ABOUT("""
            Support Helper is an open source bot.
            Which is available on GitHub at:
            https://github.com/Hishmalif/Support-Helper
            The bot is necessary for faster resolution of support requests using the built-in functionality.
            Developed on the personal initiative of the author.""");
    private final String value;
}