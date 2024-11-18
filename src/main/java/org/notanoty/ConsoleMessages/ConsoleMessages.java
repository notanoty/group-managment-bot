package org.notanoty.ConsoleMessages;

import org.notanoty.Colors;

public class ConsoleMessages
{
    public static void printInfo(String message)
    {
        printConsoleMessage("Info", message, Colors.GREEN);
    }

    public static void printSuccess(String prefix, String message)
    {
        printConsoleMessage(prefix, message, Colors.GREEN);
    }

    public static void printError(String message)
    {
        printConsoleMessage("Error", message, Colors.RED);
    }

    public static void printError(String prefix, String message)
    {
        printConsoleMessage(prefix, message, Colors.RED);
    }

    public static void printWarning(String message)
    {
        printConsoleMessage("Warning", message, Colors.YELLOW);
    }

    public static void printWarning(String prefix, String message)
    {
        printConsoleMessage(prefix, message, Colors.YELLOW);
    }

    private static void printConsoleMessage(String prefix, String message, String color)
    {
        System.out.println(color + prefix + Colors.RESET + ": " + message + Colors.RESET);
    }

}
