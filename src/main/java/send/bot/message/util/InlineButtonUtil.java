package send.bot.message.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class InlineButtonUtil {
    public static InlineKeyboardButton button(String text,String callbackData){
        InlineKeyboardButton secondRow = new InlineKeyboardButton();
        secondRow.setText(text);
        secondRow.setCallbackData(callbackData);
        return secondRow;
    }


    public static List<InlineKeyboardButton> row(InlineKeyboardButton... inlineKeyboardButtons){
        List<InlineKeyboardButton> row = new LinkedList<>();
        row.addAll(Arrays.asList(inlineKeyboardButtons));
        return row;
    }
    public static List<List<InlineKeyboardButton>>collection(List<InlineKeyboardButton>... rows){
        List<List<InlineKeyboardButton>>collection = new LinkedList<>();
        collection.addAll(Arrays.asList(rows));
        return collection;
    }

    public static InlineKeyboardMarkup keyboard(List<List<InlineKeyboardButton>> collection){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(collection);
        return inlineKeyboardMarkup;
    }
}
