package cz.chali.jdep

import org.vafer.jdeb.Console

class ConsolePrinter implements Console {
    @Override
    void debug(String message) {
        println "DEBUG $message"
    }

    @Override
    void info(String message) {
        println "INFO $message"
    }

    @Override
    void warn(String message) {
        println "WARB $message"
    }
}