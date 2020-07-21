import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

public class Bot extends TelegramLongPollingBot {
    public static void main(String[] args) {

        ApiContextInitializer.init();
        TelegramBotsApi tba = new TelegramBotsApi();

        try {
            tba.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sndMsg(Message message, String text){
    SendMessage sm = new SendMessage();
    sm.enableMarkdown(true);
    sm.setChatId(message.getChatId().toString());
    sm.setReplyToMessageId(message.getMessageId());
    sm.setText(text);
    //sendMessage(sm);
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sndDocument(Message message){
        String fileName = message.getText().replaceAll(" ", "") + message.getChatId().toString();
        String pathS = ".\\\\src\\\\main\\\\resources\\\\DocumentForSend\\\\" + fileName + ".docx".replaceAll("\\\\", "/");
        String pathForFileCopy = ".\\src\\main\\resources\\Examples\\ExampleOfContract.docx".replaceAll("\\\\", "/");
        Path p = Paths.get(pathS);
        if(!Files.exists(p)){
            try {
                Files.createFile(p);
                WordEditor.editorMsWord(pathForFileCopy, pathS, message.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        SendDocument sd = new SendDocument();
        sd.setChatId(message.getChatId().toString());
        sd.setDocument(new File(pathS));

        try {
            execute(sd);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        SendMessage sendMessage= new SendMessage();
        if(message!=null && message.hasText()){
            switch(message.getText()){
            case "/help":
                sndMsg(message, "Введити через пробел Имя и Фамилию для подготовки и получения договора");
                //execute(sendMessage.setText("Введити через пробел Имя и Фамилию для подготовки и получения договора"));
                break;

            case "Привет":
                sndMsg(message, "Введите /help");
                //execute(sendMessage.setText("Введите \"/help\""));
                break;
            default:
                String s = message.getText();
                if(!Pattern.matches("^[А-Я]{1}[а-я]{1,30}\\s[А-Я]{1}[а-я]{1,30}",s)) sndMsg(message, "Неверно указаны Имя или Фамилия");
                else{
                    sndDocument(message);
                }

            }

        }
    }

    public String getBotUsername() {
        return "ZuffTestBot";
    }

    public String getBotToken() {
        return "1072788162:AAEWQcwL7-j7Lm8h0fjcWA7o_5QHrA0xAfQ";
    }
}
