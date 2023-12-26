package send.bot.message.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import send.bot.message.config.BotConfig;
import send.bot.message.entitiy.Temporality;
import send.bot.message.entitiy.TypeItems;
import send.bot.message.entitiy.Users;
import send.bot.message.enume.Type;
import send.bot.message.repository.TemporalityRepository;
import send.bot.message.repository.TypeItemRepository;
import send.bot.message.repository.UserRepository;
import send.bot.message.util.InlineButtonUtil;


import java.time.LocalDate;
import java.util.*;

@EnableScheduling
@Service
@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig botConfig;
    public TelegramBot(BotConfig botConfig){
        this.botConfig = botConfig;
    }

    @Autowired
    UserRepository userRepository;
    @Autowired
    SchedulerTest schedulerTest;
    @Autowired
    TemporalityRepository temporalityRepository;

    TypeItems typeItems = new TypeItems();
    Map<Long, Type> typeItemsMap = new HashMap<>();


    @Override
    public  void  onUpdateReceived(Update update) {
//        TypeItems typeItems = new TypeItems();
        TypeItemRepository typeItemRepository = new TypeItemRepository();

        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();



        if (update.hasCallbackQuery()) {

//            Integer MessageId = message.getMessageId();
//            String text = message.getText();
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            message = callbackQuery.getMessage();
            User user = message.getFrom();

            if (data.equals("register") && callbackQuery.getFrom().getUserName() == null) {
                sendMessage.setText("User name tanlang @...");
                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);
                sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                        InlineButtonUtil.row(InlineButtonUtil.button("Ro'yxatdan o'tish / Зарегистрироваться \uD83D\uDCE5", "register")),
                        InlineButtonUtil.row(InlineButtonUtil.button("Holatnitekshirish / Проверить статус ✅❌", "view")),
                        InlineButtonUtil.row(InlineButtonUtil.button("Biz haqimizda / О нас \uD83D\uDCD1", "aboutUs")),
                        InlineButtonUtil.row(InlineButtonUtil.button("Kurslarimiz / Наши курсы \uD83D\uDCDA", "cours"))
                )));
                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);
            }


            if (data.equals("register") && callbackQuery.getFrom().getUserName() != null && userRepository.findById(callbackQuery.getFrom().getId()).isEmpty() && temporalityRepository.findAllByChatId(message.getChatId()).isEmpty()) {
//                TypeItems typeItems = (TypeItems) this.userRepository.findAllByChatId(message.getChatId());
                List<Users> usersList = userRepository.findAllByChatId(message.getChatId());
//                for (Users users : usersList) {
//                sendMessage.setText("iltimos ismingizni kiriting!\n" +
//                        "1-Hasan");
//                sendMessage.setChatId(message.getChatId());
//                Sendmsg(sendMessage);
                typeItemsMap.clear();
                typeItems.setType(Type.SEND_NAME);
//                typeItemRepository.add(message.getChatId(),typeItems.getType());
                typeItemsMap.put(message.getFrom().getId(),typeItems.getType());
//                typeItemRepository.add(message.getChatId(),typeItems.);

            }

            if (data.equals("register") && callbackQuery.getFrom().getUserName() != null && !temporalityRepository.findAllByChatId(message.getChatId()).isEmpty() && userRepository.findAllByChatId(message.getChatId()).isEmpty()) {
                sendMessage.setText("So'rov alaqachon yuborilgan!↗️");
                sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                            InlineButtonUtil.row(InlineButtonUtil.button("Holatnitekshirish / Проверить статус ✅❌", "view")),
                            InlineButtonUtil.row(InlineButtonUtil.button("Biz haqimizda / О нас \uD83D\uDCD1", "aboutUs")),
                            InlineButtonUtil.row(InlineButtonUtil.button("Kurslarimiz/Наши курсы \uD83D\uDCDA", "cours"))
                    )));
                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);
            }
            if (data.equals("register") && callbackQuery.getFrom().getUserName() != null && !userRepository.findAllByChatId(message.getChatId()).isEmpty()) {
                sendMessage.setText("Ro'yxatdan o'tib bo'lgansiz!\uD83E\uDDFE✅");
                sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                        InlineButtonUtil.row(InlineButtonUtil.button("Holatnitekshirish / Проверить статус ✅❌", "view")),
                        InlineButtonUtil.row(InlineButtonUtil.button("Biz haqimizda / О нас \uD83D\uDCD1", "aboutUs")),
                        InlineButtonUtil.row(InlineButtonUtil.button("Kurslarimiz / Наши курсы \uD83D\uDCDA", "cours"))
                )));
                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);
            }

            if (data.equals("yes")){
                sendMessage.setText("Enter the user \uD83C\uDD94 you want to add!");
                sendMessage.setChatId(message.getChatId());
                typeItemsMap.clear();
                typeItems.setType(Type.GET_USER);
                typeItemsMap.put(message.getFrom().getId(),typeItems.getType());
                Sendmsg(sendMessage);
            }

            if (data.equals("dell")) {
                if (!userRepository.findAll().isEmpty()){
                    sendMessage.setText("Dell userni userName ni kiriting! \n" +
                            "UserName❓");
                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);
                StringBuilder stringBuilder = new StringBuilder();
                List<Users> usersList = userRepository.findAll();
                for (Users users : usersList) {
                    stringBuilder.append("\uD83C\uDD94: " + users.getId());
                    stringBuilder.append("\n");
                    stringBuilder.append("FirstName: " + users.getFirstName());
                    stringBuilder.append("\n");
                    stringBuilder.append("Lastname: " + users.getLastName());
                    stringBuilder.append("\n");
                    stringBuilder.append("Username: " + users.getUserName());
                    stringBuilder.append("\n");
                    stringBuilder.append("Payment date: " + users.getRegistered_At());
                    stringBuilder.append("\n");
                }
                sendMessage.setText(stringBuilder.toString());
                typeItemsMap.remove(message.getFrom().getId(), typeItems.getType());
                typeItems.setType(Type.DELL_REP);
                typeItemsMap.put(message.getFrom().getId(), typeItems.getType());
                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);

            }
            } if (data.equals("dell")) {
                if (userRepository.findAll().isEmpty()){
                    sendMessage.setText("Users does not exist");
                    sendMessage.setChatId(message.getChatId());
                    Sendmsg(sendMessage);
                }
            }
                else if (data.equals("no")) {

                sendMessage.setText("Enter an \uD83C\uDD94 that is not accepted! ");
                sendMessage.setChatId(message.getChatId());
                typeItemsMap.clear();
                typeItems.setType(Type.DELL_USER);
                typeItemsMap.put(message.getFrom().getId(),typeItems.getType());
                Sendmsg(sendMessage);

            } else if (data.equals("view") && !message.getChatId().equals(botConfig.getOwnerId()) && !message.getChatId().equals(botConfig.getOwnerId2())) {
                sendMessage.setText(schedulerTest.PayDay(message.getChatId()));

                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);
                sendMessage.setText("O•P•S");

                sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                        InlineButtonUtil.row(InlineButtonUtil.button("MENU", "menuU"))
                )));
                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);

            } else if (data.equals("aboutUs") && !message.getChatId().equals(botConfig.getOwnerId()) && !message.getChatId().equals(botConfig.getOwnerId2())) {
                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setPhoto(new InputFile("AgACAgIAAxkBAAIMTGV0hJad7tI6pJqlKqyDVMlYb-0gAAK50zEbCz-pSx3MunuvihNhAQADAgADcwADMwQ"));
                sendPhoto.setCaption("O•P•S");
                sendPhoto.setChatId(message.getChatId());
                SendImageI(sendPhoto);
                sendMessage.setText("ORIENT PRIVATE SCHOOL - bu sifatli bilim va talabalar uchun qulaylik ustuvor bo'lgan ta'lim muassasasi\uD83D\uDD1D\n" +
                        "\n" +
                        "Darslarda har bir o‘quvchiga munosib e’tibor qaratiladi, tanqidiy fikrlashni o‘rganadi \uD83E\uDDE0 O‘qituvchilar talabalarni qo‘llab-quvvatlash va rag‘batlantirishga, ilg‘or texnologiyalardan foydalangan holda va interfaol tarzda dars o‘tishga harakat qiladilar\uD83D\uDE0D\n" +
                        "\n" +
                        "☎️Barcha savollar bo’yicha quyidagi raqamlarga telefon qiling:\n" +
                        "\n" +
                        "\uD83D\uDCDE (97)2222278  \n" +
                        "\uD83D\uDCDE (97)0922227 \n" +
                        "\uD83D\uDCDE (97)2883133\n" +
                        "\n" +
                        "➖➖➖➖➖➖\n" +
                        "\n" +
                        "ORIENT PRIVATE SCHOOL- учебное заведение, где в приоритете качественные знания и комфорт студентов\uD83D\uDD1D\n" +
                        "\n" +
                        "На занятиях каждый студент получает должное внимание, учится критически мыслить \uD83E\uDDE0 Преподаватели стараются поддержать и мотивировать студентов, проводят уроки используя передовые технологии и в интерактивной форме \uD83D\uDE0D\n" +
                        "\n" +
                        "\n" +
                        "☎️По всем интересующим вопросам звоните:\n" +
                        "\n" +
                        "\uD83D\uDCDE (97)2222278  \n" +
                        "\uD83D\uDCDE (97)0922227 \n" +
                        "\uD83D\uDCDE (97)2883133");

                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);
                sendMessage.setText("O•P•S");

                sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                        InlineButtonUtil.row(InlineButtonUtil.button("MENU", "menuU"))
                )));
                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);

            } else if (data.equals("cours") && !message.getChatId().equals(botConfig.getOwnerId()) && !message.getChatId().equals(botConfig.getOwnerId2()) ) {

                    sendMessage.setText("\uD83C\uDDFA\uD83C\uDDFFBir oyda 12 ta dars 1:30 daqidadan (bir soat o’ttiz daqiqa) \n" +
                            "\n" +
                            "\uD83C\uDDF7\uD83C\uDDFA12 уроков в месяц по 1:30 минут (час тридцать минут) \n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "\uD83D\uDCDA English / Ingliz tili —   250 000 UZS \n" +
                            "\uD83D\uDCDA Math / Matematika — 250 000 UZS\n" +
                            "\uD83D\uDCDA IELTS — 520 000 UZS \n" +
                            "\uD83D\uDCDACEFR — 420 000 UZS \n" +
                            "\uD83D\uDCDASAT — 520 000 UZS");
                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);
                sendMessage.setText("O•P•S");

                sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                        InlineButtonUtil.row(InlineButtonUtil.button("MENU", "menuU"))
                )));
                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);

            }


                ////Bot Owner Id
                else if (data.equals("aboutUs") && message.getChatId().equals(botConfig.getOwnerId()) && !message.getChatId().equals(botConfig.getOwnerId2())) {
                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setPhoto(new InputFile("AgACAgIAAxkBAAIMTGV0hJad7tI6pJqlKqyDVMlYb-0gAAK50zEbCz-pSx3MunuvihNhAQADAgADcwADMwQ"));
                sendPhoto.setCaption("O•P•S");
                sendPhoto.setChatId(message.getChatId());
                SendImageI(sendPhoto);
                sendMessage.setText("ORIENT PRIVATE SCHOOL - bu sifatli bilim va talabalar uchun qulaylik ustuvor bo'lgan ta'lim muassasasi\uD83D\uDD1D\n" +
                        "\n" +
                        "Darslarda har bir o‘quvchiga munosib e’tibor qaratiladi, tanqidiy fikrlashni o‘rganadi \uD83E\uDDE0 O‘qituvchilar talabalarni qo‘llab-quvvatlash va rag‘batlantirishga, ilg‘or texnologiyalardan foydalangan holda va interfaol tarzda dars o‘tishga harakat qiladilar\uD83D\uDE0D\n" +
                        "\n" +
                        "☎️Barcha savollar bo’yicha quyidagi raqamlarga telefon qiling:\n" +
                        "\n" +
                        "\uD83D\uDCDE (97)2222278  \n" +
                        "\uD83D\uDCDE (97)0922227 \n" +
                        "\uD83D\uDCDE (97)2883133\n" +
                        "\n" +
                        "➖➖➖➖➖➖\n" +
                        "\n" +
                        "ORIENT PRIVATE SCHOOL- учебное заведение, где в приоритете качественные знания и комфорт студентов\uD83D\uDD1D\n" +
                        "\n" +
                        "На занятиях каждый студент получает должное внимание, учится критически мыслить \uD83E\uDDE0 Преподаватели стараются поддержать и мотивировать студентов, проводят уроки используя передовые технологии и в интерактивной форме \uD83D\uDE0D\n" +
                        "\n" +
                        "\n" +
                        "☎️По всем интересующим вопросам звоните:\n" +
                        "\n" +
                        "\uD83D\uDCDE (97)2222278  \n" +
                        "\uD83D\uDCDE (97)0922227 \n" +
                        "\uD83D\uDCDE (97)2883133");

                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);
                sendMessage.setText("O•P•S");

                sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                        InlineButtonUtil.row(InlineButtonUtil.button("MENU", "menu"))
                )));
                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);

            } else if (data.equals("cours") && message.getChatId().equals(botConfig.getOwnerId()) && !message.getChatId().equals(botConfig.getOwnerId2()) ) {

                sendMessage.setText("\uD83C\uDDFA\uD83C\uDDFFBir oyda 12 ta dars 1:30 daqidadan (bir soat o’ttiz daqiqa) \n" +
                        "\n" +
                        "\uD83C\uDDF7\uD83C\uDDFA12 уроков в месяц по 1:30 минут (час тридцать минут) \n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "\uD83D\uDCDA English / Ingliz tili —   250 000 UZS \n" +
                        "\uD83D\uDCDA Math / Matematika — 250 000 UZS\n" +
                        "\uD83D\uDCDA IELTS — 520 000 UZS \n" +
                        "\uD83D\uDCDACEFR — 420 000 UZS \n" +
                        "\uD83D\uDCDASAT — 520 000 UZS");


                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);
                sendMessage.setText("O•P•S");

                sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                        InlineButtonUtil.row(InlineButtonUtil.button("MENU", "menu"))

                )));
//            EditMessage(editMessageText);
//                    Sendmsg(sendMessage);
//                } catch (Exception e) {
//                    sendMessage.setText(" user ro'yxatdan o'tib bo'lgan ");
//                    sendMessage.setChatId(message.getChatId());
                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);

            } else if (data.equals("menu")) {
                    EditMessageText editMessageText = new EditMessageText();
                    editMessageText.setText("menu");

                editMessageText.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                        InlineButtonUtil.row(InlineButtonUtil.button("Barcha userlarni ko'rish\uD83D\uDDD3", "All_view")),
                        InlineButtonUtil.row(InlineButtonUtil.button("Biz haqimizda / О нас \uD83D\uDCD1", "aboutUs")),
                        InlineButtonUtil.row(InlineButtonUtil.button("Kurslarimiz / Наши курсы \uD83D\uDCDA", "cours")),
                        InlineButtonUtil.row(InlineButtonUtil.button("Delete_user\uD83D\uDDD1", "dell"))

                )));
                editMessageText.setChatId(message.getChatId());
                editMessageText.setMessageId(message.getMessageId());
                EditMessage(editMessageText);

            } else if (data.equals("menuU")) {
                EditMessageText editMessageText = new EditMessageText();
                editMessageText.setText("Menu");
                editMessageText.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                        InlineButtonUtil.row(InlineButtonUtil.button("Ro'yxatdan o'tish / Зарегистрироваться \uD83D\uDCE5", "register")),
                        InlineButtonUtil.row(InlineButtonUtil.button("Holatnitekshirish / Проверить статус ✅❌", "view")),
                        InlineButtonUtil.row(InlineButtonUtil.button("Biz haqimizda / О нас \uD83D\uDCD1", "aboutUs")),
                        InlineButtonUtil.row(InlineButtonUtil.button("Kurslarimiz / Наши курсы \uD83D\uDCDA", "cours"))
                )));
                editMessageText.setChatId(message.getChatId());
                editMessageText.setMessageId(message.getMessageId());
                EditMessage(editMessageText);
            }

////bot owner2
            else if (data.equals("aboutUs") && !message.getChatId().equals(botConfig.getOwnerId()) && message.getChatId().equals(botConfig.getOwnerId2())) {
                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setPhoto(new InputFile("AgACAgIAAxkBAAIMTGV0hJad7tI6pJqlKqyDVMlYb-0gAAK50zEbCz-pSx3MunuvihNhAQADAgADcwADMwQ"));
                sendPhoto.setCaption("O•P•S");
                sendPhoto.setChatId(message.getChatId());
                SendImageI(sendPhoto);
                sendMessage.setText("ORIENT PRIVATE SCHOOL - bu sifatli bilim va talabalar uchun qulaylik ustuvor bo'lgan ta'lim muassasasi\uD83D\uDD1D\n" +
                        "\n" +
                        "Darslarda har bir o‘quvchiga munosib e’tibor qaratiladi, tanqidiy fikrlashni o‘rganadi \uD83E\uDDE0 O‘qituvchilar talabalarni qo‘llab-quvvatlash va rag‘batlantirishga, ilg‘or texnologiyalardan foydalangan holda va interfaol tarzda dars o‘tishga harakat qiladilar\uD83D\uDE0D\n" +
                        "\n" +
                        "☎️Barcha savollar bo’yicha quyidagi raqamlarga telefon qiling:\n" +
                        "\n" +
                        "\uD83D\uDCDE (97)2222278  \n" +
                        "\uD83D\uDCDE (97)0922227 \n" +
                        "\uD83D\uDCDE (97)2883133\n" +
                        "\n" +
                        "➖➖➖➖➖➖\n" +
                        "\n" +
                        "ORIENT PRIVATE SCHOOL- учебное заведение, где в приоритете качественные знания и комфорт студентов\uD83D\uDD1D\n" +
                        "\n" +
                        "На занятиях каждый студент получает должное внимание, учится критически мыслить \uD83E\uDDE0 Преподаватели стараются поддержать и мотивировать студентов, проводят уроки используя передовые технологии и в интерактивной форме \uD83D\uDE0D\n" +
                        "\n" +
                        "\n" +
                        "☎️По всем интересующим вопросам звоните:\n" +
                        "\n" +
                        "\uD83D\uDCDE (97)2222278  \n" +
                        "\uD83D\uDCDE (97)0922227 \n" +
                        "\uD83D\uDCDE (97)2883133");

                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);
                sendMessage.setText("O•P•S");

                sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                        InlineButtonUtil.row(InlineButtonUtil.button("MENU", "menu"))
                )));
                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);

            } else if (data.equals("cours") && !message.getChatId().equals(botConfig.getOwnerId()) && message.getChatId().equals(botConfig.getOwnerId2()) ) {

                sendMessage.setText("\uD83C\uDDFA\uD83C\uDDFFBir oyda 12 ta dars 1:30 daqidadan (bir soat o’ttiz daqiqa) \n" +
                        "\n" +
                        "\uD83C\uDDF7\uD83C\uDDFA12 уроков в месяц по 1:30 минут (час тридцать минут) \n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "\uD83D\uDCDA English / Ingliz tili —   250 000 UZS \n" +
                        "\uD83D\uDCDA Math / Matematika — 250 000 UZS\n" +
                        "\uD83D\uDCDA IELTS — 520 000 UZS \n" +
                        "\uD83D\uDCDACEFR — 420 000 UZS \n" +
                        "\uD83D\uDCDASAT — 520 000 UZS");


                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);
                sendMessage.setText("O•P•S");

                sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                        InlineButtonUtil.row(InlineButtonUtil.button("MENU", "menu"))

                )));
//            EditMessage(editMessageText);
//                    Sendmsg(sendMessage);
//                } catch (Exception e) {
//                    sendMessage.setText(" user ro'yxatdan o'tib bo'lgan ");
//                    sendMessage.setChatId(message.getChatId());
                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);

            }





            if (data.equals("All_view")) {
                if (!userRepository.findAll().isEmpty()) {

                StringBuilder stringBuilder = new StringBuilder();
                List<Users> usersList = userRepository.findAll();
//                typeItems.setType(Type.VIEW_ALL);
//                typeItemsMap.put(message.getFrom().getId(),typeItems.getType());
                for (Users users : usersList) {
                    stringBuilder.append("\uD83C\uDD94: " + users.getId());
                    stringBuilder.append("\n");
                    stringBuilder.append("FirstName: " + users.getFirstName());
                    stringBuilder.append("\n");
                    stringBuilder.append("Lastname: " + users.getLastName());
                    stringBuilder.append("\n");
                    stringBuilder.append("Username: " + users.getUserName());
                    stringBuilder.append("\n");
                    stringBuilder.append("Payment date: " + users.getRegistered_At());
                    stringBuilder.append("\n");
                    if (users.isPayment_verification() == false) {
                        stringBuilder.append("Payment_verification: " + users.isPayment_verification());
                        stringBuilder.append(" /payment");
                        stringBuilder.append("\n\n");
                    } else if (users.isPayment_verification() == true) {
                        stringBuilder.append("Payment_verification: " + users.isPayment_verification());
                        stringBuilder.append("\n\n");
                    }
                }
                    sendMessage.setText(stringBuilder.toString());
                    sendMessage.setChatId(message.getChatId());
                    Sendmsg(sendMessage);
                    sendMessage.setText("O•P•S");

                sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                        InlineButtonUtil.row(InlineButtonUtil.button("MENU", "menu"))
                )));

                sendMessage.setChatId(message.getChatId());
                try {
                    Sendmsg(sendMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
            } if (data.equals("All_view")) {
                if (userRepository.findAll().isEmpty()){
                    sendMessage.setText("Users: null");
                    sendMessage.setChatId(message.getChatId());
                    Sendmsg(sendMessage);

                }
            }

            else if (data.equals("paid")) {
                List<Users> usersList = userRepository.findAll();
                EditMessageText editMessageText = new EditMessageText();
                editMessageText.setMessageId(message.getMessageId());
                editMessageText.setChatId(message.getChatId());
                for (Users users : usersList) {
                    users.setPayment_verification(true);
                }

            }

        }

//        if (message.getText().startsWith("id") && typeItemsMap.containsValue(Type.GET_USER) && !typeItemsMap.containsValue(Type.PAY)) {
        if (typeItemsMap.containsValue(Type.GET_USER) && !typeItemsMap.containsValue(Type.PAY) && !update.hasCallbackQuery() && !message.getText().equals("/start") && !message.getText().equals("/help") && !typeItemsMap.containsValue(Type.SEND_NAME) &&  message.getText().matches("\\d+")) {
//            String id = message.getText().substring(2);
            typeItemsMap.clear();
            String id = message.getText();

            Optional<Temporality> temporality = temporalityRepository.findById(Long.valueOf(id));
            Optional<Users> usersList1 = userRepository.findById(Long.valueOf(id));
            if (!userRepository.findAll().isEmpty()) {
//            if (userRepository.findById(usersList1.get().getChatId()).isPresent()) {
//                sendMessage.setText("User contact has been added");
//                sendMessage.setChatId(message.getChatId());
//                Sendmsg(sendMessage);
//
////            Optional<Users> users = userRepository.findById(Long.valueOf(id));
//                Users users = usersList1.get();
//            }
                if (temporality.isPresent() && userRepository.findAllByChatId(temporality.get().getChatId()).isEmpty()) {
//                try {
                    RegisterUser(message, Long.valueOf(id));
                    Optional<Temporality> usersList = temporalityRepository.findById(Long.valueOf(id));
                    if (usersList.isPresent()){
                        sendMessage.setText("Ro'yxatdan o'tdingiz!\uD83D\uDCCB✅ ");
                        sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                                InlineButtonUtil.row(InlineButtonUtil.button("Holatnitekshirish / Проверить статус ✅❌", "view")),
                                InlineButtonUtil.row(InlineButtonUtil.button("Biz haqimizda / О нас \uD83D\uDCD1", "aboutUs")),
                                InlineButtonUtil.row(InlineButtonUtil.button("Kurslarimiz / Наши курсы \uD83D\uDCDA", "cours"))
                        )));
//                    for (Temporality users1 : usersList) {
                        sendMessage.setChatId(usersList.get().getChatId());
                        Sendmsg(sendMessage);

                    }


                    sendMessage.setText("User added!\uD83D\uDCCB✅ ");
                    sendMessage.setChatId(message.getChatId());
                    sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                            InlineButtonUtil.row(InlineButtonUtil.button("MENU", "menu"))
                    )));
                    Sendmsg(sendMessage);

                }else if (temporality.isEmpty()) {
                    sendMessage.setText("User does not exist!❌ ");
                    sendMessage.setChatId(message.getChatId());
                    sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                            InlineButtonUtil.row(InlineButtonUtil.button("MENU", "menu"))
                    )));
                    Sendmsg(sendMessage);
                } else if (!userRepository.findAllByChatId(temporality.get().getChatId()).isEmpty()) {
                    sendMessage.setText("User contact has been added");
                    sendMessage.setChatId(message.getChatId());
                    Sendmsg(sendMessage);
                    sendMessage.setText("O•P•S");
                    sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                            InlineButtonUtil.row(InlineButtonUtil.button("MENU", "menu"))
                    )));
//                    sendMessage.setChatId(message.getChatId());
                    Sendmsg(sendMessage);
                }
            } else if (userRepository.findAll().isEmpty() &&  temporalityRepository.findById(Long.valueOf(id)).isPresent()) {

                RegisterUser(message, Long.valueOf(id));

                sendMessage.setText("Ro'yxatdan o'tdingiz!\uD83D\uDCCB✅ ");
                sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                        InlineButtonUtil.row(InlineButtonUtil.button("Holatnitekshirish / Проверить статус ✅❌", "view")),
                        InlineButtonUtil.row(InlineButtonUtil.button("Biz haqimizda / О нас \uD83D\uDCD1", "aboutUs")),
                        InlineButtonUtil.row(InlineButtonUtil.button("Kurslarimiz / Наши курсы \uD83D\uDCDA", "cours"))
                )));
//                    editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
//                    System.out.println("user-1"+userRepository.findAll().get(-1));
                Optional<Temporality> usersList = temporalityRepository.findById(Long.valueOf(id));
//                    for (Temporality users1 : usersList) {
                sendMessage.setChatId(usersList.get().getChatId());
                Sendmsg(sendMessage);
//                    }



//                sendMessage.setChatId(message.getChatId());

                sendMessage.setText("User added!\uD83D\uDCCB✅ ");
//                sendMessage.setChatId(message.getChatId());
                sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                        InlineButtonUtil.row(InlineButtonUtil.button("MENU", "menu"))
                )));
                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);
            } else if (temporalityRepository.findById(Long.valueOf(id)).isEmpty()) {
                sendMessage.setText("User does not exist!❌ ");
                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);
                sendMessage.setText("O•P•S");
                sendMessage.setChatId(message.getChatId());
                sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                        InlineButtonUtil.row(InlineButtonUtil.button("MENU", "menu"))
                )));
                Sendmsg(sendMessage);
            } else if (!userRepository.findAllByChatId(usersList1.get().getChatId()).isEmpty()) {
                sendMessage.setText("User contact has been added");
                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);
                sendMessage.setText("O•P•S");

                sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                        InlineButtonUtil.row(InlineButtonUtil.button("MENU", "menu"))
                )));
                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);
            }

        }else if (typeItemsMap.containsValue(Type.GET_USER) && !typeItemsMap.containsValue(Type.PAY) && !update.hasCallbackQuery() && !message.getText().equals("/start") && !message.getText().equals("/help") && !typeItemsMap.containsValue(Type.SEND_NAME) &&  !message.getText().matches("\\d+") && message.getChatId().equals(botConfig.getOwnerId2())){
            sendMessage.setText(" enter a number ");
            sendMessage.setChatId(message.getChatId());
            Sendmsg(sendMessage);
        }
        if (typeItemsMap.containsValue(Type.DELL_USER) && !typeItemsMap.containsValue(Type.PAY
        ) && !update.hasCallbackQuery() && !message.getText().equals("/start") && !message.getText().equals("/help") && message.getText().matches("\\d+") && message.getChatId().equals(botConfig.getOwnerId2())) {
//            String id = message.getText().substring(2);
            typeItemsMap.clear();
            String id = message.getText();
            Optional<Temporality> temporality = temporalityRepository.findById(Long.valueOf(id));
            Optional<Users> users = userRepository.findById(Long.valueOf(id));
            if (temporalityRepository.findById(Long.valueOf(id)).isPresent() && userRepository.findAllByChatId(temporality.get().getChatId()).isEmpty()){
                sendMessage.setText("So'rov qabul qilinmadi\uD83D\uDEAB ");
                Optional<Temporality> usersList = temporalityRepository.findById(Long.valueOf(id));
//                for (Temporality users1 : usersList) {
                    sendMessage.setChatId(usersList.get().getChatId());
//                }
                Sendmsg(sendMessage);
//                try {
                temporalityRepository.deleteById(Long.valueOf(id));

                sendMessage.setText("User deleted\uD83D\uDDD1");
                sendMessage.setChatId(message.getChatId());
            } else if (userRepository.findById(Long.valueOf(id)).isPresent()){
                sendMessage.setText("no");
                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);
                sendMessage.setText("O•P•S");
                sendMessage.setChatId(message.getChatId());
            }else if (temporalityRepository.findById(Long.valueOf(id)).isEmpty()){
                sendMessage.setText("no");
                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);
                sendMessage.setText("O•P•S");
                sendMessage.setChatId(message.getChatId());
            }

//            sendMessage.setMessageId(message.getMessageId());
            sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                    InlineButtonUtil.row(InlineButtonUtil.button("MENU", "menu"))

            )));

                    Sendmsg(sendMessage);

        } if (typeItemsMap.containsValue(Type.DELL_USER) && !typeItemsMap.containsValue(Type.PAY) && !update.hasCallbackQuery() && !message.getText().equals("/start") && !message.getText().equals("/help") && !message.getText().matches("\\d+")
                && message.getChatId().equals(botConfig.getOwnerId2())){
            typeItemsMap.clear();
            sendMessage.setText(" enter a number ");
            sendMessage.setChatId(message.getChatId());
            Sendmsg(sendMessage);
        }
        if (typeItemsMap.containsValue(Type.DELL_REP)  && !update.hasCallbackQuery() && !message.getText().equals("/help") && !message.getText().matches("\\d+")
                && message.getChatId().equals(botConfig.getOwnerId2())){
//                String userName = message.getText().substring(1);
            typeItemsMap.clear();
            String userName = message.getText();
//                Optional<Temporality> temporality = temporalityRepository.findById(Long.valueOf(id));
//                Optional<Users> users = userRepository.findById(Long.valueOf(id));

            if (userRepository.findByUserName(userName) != null) {
                userRepository.delete(userRepository.findByUserName(userName));
                temporalityRepository.delete(temporalityRepository.findByUserName(userName));
                sendMessage.setText("The user will be permanently deleted");
                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);
                sendMessage.setText("O•P•S");
                sendMessage.setChatId(message.getChatId());
                sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                        InlineButtonUtil.row(InlineButtonUtil.button("MENU", "menu"))

                )));

                Sendmsg(sendMessage);
//                }
            }
        }else if (typeItemsMap.containsValue(Type.DELL_REP) && !update.hasCallbackQuery() && !message.getText().equals("/start") && !message.getText().equals("/help") && message.getText().matches("\\d+") && message.getChatId().equals(botConfig.getOwnerId2())){
            sendMessage.setText(" enter username ");
            sendMessage.setChatId(message.getChatId());
            Sendmsg(sendMessage);
        }else if (typeItemsMap.containsValue(Type.DELL_REP) && !update.hasCallbackQuery() && !message.getText().equals("/start") && !message.getText().equals("/help") && !message.getText().matches("\\d+") && message.getChatId().equals(botConfig.getOwnerId2())){
            typeItemsMap.clear();
            sendMessage.setText("  username not found ");
            sendMessage.setChatId(message.getChatId());
            Sendmsg(sendMessage);}
        if (typeItemsMap.containsValue(Type.PAY) && !update.hasCallbackQuery() && !message.getText().equals("/start") && !message.getText().equals("/help") && message.getText().matches("\\d+")
                && message.getChatId().equals(botConfig.getOwnerId2())){
//            String id = message.getText().substring(2);
            typeItemsMap.clear();
            String id = message.getText();
//            Optional<Users> users = userRepository.findById(Long.valueOf(id));
               Optional<Users> users =userRepository.findById(Long.valueOf(id));
               if (users.isPresent()){
               Users users1 = users.get();
//            users1.setRegistered_At(users1.getRegistered_At().plusDays(28));
            users1.setPayment_verification(true);
            userRepository.save(users1);
            sendMessage.setText(users.get().getFirstName() + " paymentverification-" + users.get().isPayment_verification());
                   sendMessage.setChatId(message.getChatId());
                   Sendmsg(sendMessage);
                   sendMessage.setText("O•P•S");
//                   sendMessage.setChatId(sendMessage.getChatId());
//                   Sendmsg(sendMessage);
//
                   sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                           InlineButtonUtil.row(InlineButtonUtil.button("MENU", "menu"))

                   )));
                   sendMessage.setChatId(message.getChatId());
                   Sendmsg(sendMessage);


               }else if (users.isEmpty()) {
                   sendMessage.setText(" not found ");
//                   sendMessage.setChatId(message.getChatId());
//                   Sendmsg(sendMessage);
                   sendMessage.setChatId(message.getChatId());
                   Sendmsg(sendMessage);
                   sendMessage.setText("O•P•S");
//            users.ifPresent(value -> value.setPayment_verification(true));
//            users.ifPresent(value -> value.setRegistered_At(users.get().getRegistered_At().plusDays(+28)));

                   sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                           InlineButtonUtil.row(InlineButtonUtil.button("MENU", "menu"))

                   )));
                   sendMessage.setChatId(message.getChatId());
                   Sendmsg(sendMessage);
               }

        }else if (typeItemsMap.containsValue(Type.PAY) && typeItemsMap.containsKey(message.getChatId()) && !update.hasCallbackQuery() && !message.getText().equals("/start") && !message.getText().equals("/help") && !message.getText().equals("/payment") && !message.getChatId().equals(botConfig.getOwnerId()) && !message.getText().matches("\\d+")
                && message.getChatId().equals(botConfig.getOwnerId2())){
            sendMessage.setText(" enter a number ");
            sendMessage.setChatId(message.getChatId());
            Sendmsg(sendMessage);
        }



        /////222222



        else if (typeItemsMap.containsValue(Type.GET_USER) && !typeItemsMap.containsValue(Type.PAY) && !update.hasCallbackQuery() && !message.getText().equals("/start") && !message.getText().equals("/help") && !typeItemsMap.containsValue(Type.SEND_NAME) &&  !message.getText().matches("\\d+") && message.getChatId().equals(botConfig.getOwnerId())){
            sendMessage.setText(" enter a number ");
            sendMessage.setChatId(message.getChatId());
            Sendmsg(sendMessage);
        }
        if (typeItemsMap.containsValue(Type.DELL_USER) && !typeItemsMap.containsValue(Type.PAY
        ) && !update.hasCallbackQuery() && !message.getText().equals("/start") && !message.getText().equals("/help") && message.getText().matches("\\d+") && message.getChatId().equals(botConfig.getOwnerId())) {
//            String id = message.getText().substring(2);
            typeItemsMap.clear();
            String id = message.getText();
            Optional<Temporality> temporality = temporalityRepository.findById(Long.valueOf(id));
            Optional<Users> users = userRepository.findById(Long.valueOf(id));
            if (temporalityRepository.findById(Long.valueOf(id)).isPresent() && userRepository.findAllByChatId(temporality.get().getChatId()).isEmpty()){
                sendMessage.setText("So'rov qabul qilinmadi\uD83D\uDEAB ");
                Optional<Temporality> usersList = temporalityRepository.findById(Long.valueOf(id));
//                for (Temporality users1 : usersList) {
                sendMessage.setChatId(usersList.get().getChatId());
                Sendmsg(sendMessage);
                sendMessage.setText("O•P•S");

                sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                        InlineButtonUtil.row(InlineButtonUtil.button("MENU", "menuU"))
                )));
                sendMessage.setChatId(usersList.get().getChatId());
//                }
                Sendmsg(sendMessage);
//                try {
                temporalityRepository.deleteById(Long.valueOf(id));

                sendMessage.setText("User deleted\uD83D\uDDD1");
                sendMessage.setChatId(message.getChatId());
            } else if (temporalityRepository.findById(Long.valueOf(id)).isEmpty()){
                sendMessage.setText("no");
                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);
                sendMessage.setText("O•P•S");
                sendMessage.setChatId(message.getChatId());
            }else if (userRepository.findById(Long.valueOf(id)).isPresent()){
                sendMessage.setText("no");
                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);
                sendMessage.setText("O•P•S");
                sendMessage.setChatId(message.getChatId());
            }

//            sendMessage.setMessageId(message.getMessageId());
            sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                    InlineButtonUtil.row(InlineButtonUtil.button("MENU", "menu"))

            )));

            Sendmsg(sendMessage);

        } if (typeItemsMap.containsValue(Type.DELL_USER) && !typeItemsMap.containsValue(Type.PAY) && !update.hasCallbackQuery() && !message.getText().equals("/start") && !message.getText().equals("/help") && !message.getText().matches("\\d+")
                && message.getChatId().equals(botConfig.getOwnerId())){
            typeItemsMap.clear();
            sendMessage.setText(" enter a number ");
            sendMessage.setChatId(message.getChatId());
            Sendmsg(sendMessage);
        }
        if (typeItemsMap.containsValue(Type.DELL_REP)  && !update.hasCallbackQuery() && !message.getText().equals("/help") && !message.getText().matches("\\d+")
                && message.getChatId().equals(botConfig.getOwnerId())){
//                String userName = message.getText().substring(1);
            typeItemsMap.clear();
            String userName = message.getText();
//                Optional<Temporality> temporality = temporalityRepository.findById(Long.valueOf(id));
//                Optional<Users> users = userRepository.findById(Long.valueOf(id));

            if (userRepository.findByUserName(userName) != null) {
                userRepository.delete(userRepository.findByUserName(userName));
                temporalityRepository.delete(temporalityRepository.findByUserName(userName));
                sendMessage.setText("The user will be permanently deleted");
                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);
                sendMessage.setText("O•P•S");
                sendMessage.setChatId(message.getChatId());
                sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                        InlineButtonUtil.row(InlineButtonUtil.button("MENU", "menu"))

                )));

                Sendmsg(sendMessage);
//                }
            }
        }else if (typeItemsMap.containsValue(Type.DELL_REP) && !update.hasCallbackQuery() && !message.getText().equals("/start") && !message.getText().equals("/help") && message.getText().matches("\\d+") && message.getChatId().equals(botConfig.getOwnerId())){
            sendMessage.setText(" enter username ");
            sendMessage.setChatId(message.getChatId());
            Sendmsg(sendMessage);
        }else if (typeItemsMap.containsValue(Type.DELL_REP) && !update.hasCallbackQuery() && !message.getText().equals("/start") && !message.getText().equals("/help") && !message.getText().matches("\\d+") && message.getChatId().equals(botConfig.getOwnerId())){
            typeItemsMap.clear();
            sendMessage.setText("  username not found ");
            sendMessage.setChatId(message.getChatId());
            Sendmsg(sendMessage);}
        if (typeItemsMap.containsValue(Type.PAY) && !update.hasCallbackQuery() && !message.getText().equals("/start") && !message.getText().equals("/help") && message.getText().matches("\\d+")
                && message.getChatId().equals(botConfig.getOwnerId())){
//            String id = message.getText().substring(2);
            typeItemsMap.clear();
            String id = message.getText();
//            Optional<Users> users = userRepository.findById(Long.valueOf(id));
            Optional<Users> users =userRepository.findById(Long.valueOf(id));
            if (users.isPresent()){
                Users users1 = users.get();
//            users1.setRegistered_At(users1.getRegistered_At().plusDays(28));//
                users1.setPayment_verification(true);
                userRepository.save(users1);
                sendMessage.setText(users.get().getFirstName() + " paymentverification-" + users.get().isPayment_verification());
                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);
                sendMessage.setText("O•P•S");
//                   sendMessage.setChatId(sendMessage.getChatId());
//                   Sendmsg(sendMessage);
//
                sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                        InlineButtonUtil.row(InlineButtonUtil.button("MENU", "menu"))

                )));
                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);


            }else if (users.isEmpty()) {
                sendMessage.setText(" not found ");
//                   sendMessage.setChatId(message.getChatId());
//                   Sendmsg(sendMessage);
                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);
                sendMessage.setText("O•P•S");
//            users.ifPresent(value -> value.setPayment_verification(true));
//            users.ifPresent(value -> value.setRegistered_At(users.get().getRegistered_At().plusDays(+28)));

                sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                        InlineButtonUtil.row(InlineButtonUtil.button("MENU", "menu"))

                )));
                sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);
            }

        }else if (typeItemsMap.containsValue(Type.PAY) && typeItemsMap.containsKey(message.getChatId()) && !update.hasCallbackQuery() && !message.getText().equals("/start") && !message.getText().equals("/help") && !message.getText().equals("/payment") && !message.getChatId().equals(botConfig.getOwnerId()) && !message.getText().matches("\\d+")
                && message.getChatId().equals(botConfig.getOwnerId())){
            sendMessage.setText(" enter a number ");
            sendMessage.setChatId(message.getChatId());
            Sendmsg(sendMessage);
        }



//        if (message.getText().startsWith("1-") && userRepository.findAllByChatId(message.getChatId()).isEmpty() && typeItemsMap.containsValue(Type.SEND_NAME)){
        if (userRepository.findAllByChatId(message.getChatId()).isEmpty() && typeItemsMap.containsValue(Type.SEND_NAME) && !update.hasCallbackQuery() && !message.getText().equals("/start") && !message.getText().equals("/help") && !message.getChatId().equals(botConfig.getOwnerId()) && !message.getChatId().equals(botConfig.getOwnerId2()) && !message.getText().matches("\\d+")){
            if (!message.getText().matches("\\d+")){
                typeItemsMap.clear();
//            if (typeItems.getType().equals(Type.SEND_NAME)) {
                sendMessage.setText("So'rov yuborildi iltimos kuting!↗️");
            sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                    InlineButtonUtil.row(InlineButtonUtil.button("Biz haqimizda / О нас \uD83D\uDCD1", "aboutUs")),
                    InlineButtonUtil.row(InlineButtonUtil.button("Kurslarimiz  / Наши курсы \uD83D\uDCDA", "cours"))
                            )));
            sendMessage.setChatId(message.getChatId());
                Sendmsg(sendMessage);
//                if (message.getText().startsWith("1-") && userRepository.findAllByChatId(message.getChatId()).isEmpty()) {
                    String name = message.getText();
                    Temporality temporality = new Temporality();
                    temporality.setFirstName(name);
                    temporality.setChatId(message.getChatId());
                    temporality.setUserName(message.getFrom().getUserName());
                    temporality.setLastName(message.getFrom().getFirstName());
                    temporalityRepository.save(temporality);


                    if (!message.getChatId().equals(botConfig.getOwnerId()) && userRepository.findAllByChatId(message.getChatId()).isEmpty() && !message.getChatId().equals(botConfig.getOwnerId2())) {
                        List<Temporality> usersList = temporalityRepository.findAll();
                        for (Temporality users : usersList) {
//                            sendMessage.setText("change true or false");
                            sendMessage.setText(name + " is asking for permission to join the bot chatId " + users.getId());
                        }
                        sendMessage.setChatId(botConfig.getOwnerId2());

                        sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                                InlineButtonUtil.row(InlineButtonUtil.button("Yes✅", "yes")),
                                InlineButtonUtil.row(InlineButtonUtil.button("NO\uD83D\uDEAB", "no"))
                        )));
//                        Sendmsg(sendMessage);
                        Sendmsg(sendMessage);



//                    }
                }if (!message.getChatId().equals(botConfig.getOwnerId()) && userRepository.findAllByChatId(message.getChatId()).isEmpty() && !message.getChatId().equals(botConfig.getOwnerId2())) {
                List<Temporality> usersList = temporalityRepository.findAll();
                for (Temporality users : usersList) {
                    typeItemsMap.clear();
//                            sendMessage.setText("change true or false");
                    sendMessage.setText(name + " is asking for permission to join the bot chatId " + users.getId());
//                            sendMessage.setChatId(botConfig.getOwnerId());
//                            sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
//                                    InlineButtonUtil.row(InlineButtonUtil.button("Yes", "yes")),
//                                    InlineButtonUtil.row(InlineButtonUtil.button("NO", "no"))
//                            )));
//                            Sendmsg(sendMessage);

                }
//                        sendMessage.setText("change true or false");
//                        sendMessage.setText(name + " is asking for permission to join the bot chatId " + message.getFrom().getUserName());
                sendMessage.setChatId(botConfig.getOwnerId());
//                sendMessage.setChatId(botConfig.getOwnerId2());

                sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                        InlineButtonUtil.row(InlineButtonUtil.button("Yes✅", "yes")),
                        InlineButtonUtil.row(InlineButtonUtil.button("NO\uD83D\uDEAB", "no"))
                )));
                Sendmsg(sendMessage);
//                Sendmsg(sendMessage);



//                    }
            }
                    }
        }else if (typeItemsMap.containsValue(Type.SEND_NAME) && temporalityRepository.findAllByChatId(message.getChatId()).isEmpty()  && !message.getText().equals("/start") && !message.getText().equals("/help") && !message.getChatId().equals(botConfig.getOwnerId()) && !message.getChatId().equals(botConfig.getOwnerId())) {
            sendMessage.setText("⬇️Iltimos ismingizni harflarda kiriting!");
            sendMessage.setChatId(message.getChatId());
            Sendmsg(sendMessage);

//                }
        }else if (userRepository.findAllByChatId(message.getChatId()).isEmpty() && typeItemsMap.containsValue(Type.SEND_NAME) && !update.hasCallbackQuery() && !message.getText().equals("/start") && !message.getText().equals("/help") && !message.getChatId().equals(botConfig.getOwnerId()) && message.getText().matches("\\d+")){
            sendMessage.setText(" Iltimos ismingizni harflarda kiriting\uD83C\uDD70️ ");
            sendMessage.setChatId(message.getChatId());
            Sendmsg(sendMessage);
        }

//        else if (!userRepository.findAllByChatId(message.getChatId()).isEmpty()) {
//            sendMessage.setText("Ro'yxatdan o'tib bo'lgansiz!");
//        }
        else if (message.getText() != null) {
            Integer MessageId = message.getMessageId();
            sendMessage.setChatId(message.getChatId());
            String text = message.getText();
            if (text != null){

                sendMessage.setChatId(message.getChatId());
                if (text.equals("/start") && !message.getChatId().equals(botConfig.getOwnerId()) && !message.getChatId().equals(botConfig.getOwnerId2()) ) {
                    List<Temporality> tempList = temporalityRepository.findAll();
//                    for (Temporality tempor : tempList) {
                        if (temporalityRepository.findAllByChatId(message.getChatId()).isEmpty() && userRepository.findAllByChatId(message.getChatId()).isEmpty()) {
                                try {
                                    sendMessage.setText("Assalomu alaykum *ORIENT PRIVATE SCHOOL * \uD83E\uDD16 ga xush kelibsiz");
                                    sendMessage.setParseMode("Markdown");
                                    sendMessage.setChatId(message.getChatId());

                                    sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                                            InlineButtonUtil.row(InlineButtonUtil.button("Ro'yxatdan o'tish / Зарегистрироваться \uD83D\uDCE5", "register")),
                                            InlineButtonUtil.row(InlineButtonUtil.button("Holatnitekshirish / Проверить статус ✅❌", "view")),
                                            InlineButtonUtil.row(InlineButtonUtil.button("Biz haqimizda / О нас \uD83D\uDCD1", "aboutUs")),
                                            InlineButtonUtil.row(InlineButtonUtil.button("Kurslarimiz / Наши курсы \uD83D\uDCDA", "cours"))
                                    )));

                                    Sendmsg(sendMessage);
                                } catch (Exception e) {
                                    sendMessage.setText(message.getFrom().getUserName() + " siz ro'yxatdan ");
                                    sendMessage.setChatId(message.getChatId());
                                    Sendmsg(sendMessage);
                                }

                            }if (!userRepository.findAllByChatId(message.getChatId()).isEmpty()) {
                                sendMessage.setText("Assalomu alaykum *ORIENT PRIVATE SCHOOL * \uD83E\uDD16 ga xush kelibsiz");
                                sendMessage.setParseMode("Markdown");
                                sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                                        InlineButtonUtil.row(InlineButtonUtil.button("Ro'yxatdan o'tish / Зарегистрироваться \uD83D\uDCE5", "register")),
                                        InlineButtonUtil.row(InlineButtonUtil.button("Holatnitekshirish / Проверить статус ✅❌", "view")),
                                        InlineButtonUtil.row(InlineButtonUtil.button("Biz haqimizda / О нас \uD83D\uDCD1", "aboutUs")),
                                        InlineButtonUtil.row(InlineButtonUtil.button("Kurslarimiz / Наши курсы \uD83D\uDCDA", "cours"))
                                )));
                                sendMessage.setChatId(message.getChatId());
                                Sendmsg(sendMessage);
//                            }
                    }

//

                    }
                else if (text.equals("/payment") && !message.getText().equals("/start") && !message.getText().equals("/help")) {
//                String ids = text.split("/payment")[1];
                    StringBuilder stringBuilder = new StringBuilder();
                    List<Users> usersList = userRepository.findAll();
                    for (Users users : usersList) {
                        stringBuilder.append("\uD83C\uDD94: " + users.getId());
                        stringBuilder.append("\n");
                        stringBuilder.append("FirstName: " + users.getFirstName());
                        stringBuilder.append("\n");
                        stringBuilder.append("Lastname: " + users.getLastName());
                        stringBuilder.append("\n");
                        stringBuilder.append("Username: " + users.getUserName());
                        stringBuilder.append("\n");
                        stringBuilder.append("Payment date: " + users.getRegistered_At());
                        stringBuilder.append("\n");
                        stringBuilder.append("Payment_verification: "+ users.isPayment_verification());
                        stringBuilder.append("\n\n");
                    }
                    sendMessage.setText("Enter the Paying User \uD83C\uDD94  \n" +
                            "\uD83C\uDD94?");
                    sendMessage.setChatId(message.getChatId());
                    Sendmsg(sendMessage);
                    sendMessage.setText(stringBuilder.toString());
                    typeItemsMap.clear();
                    typeItems.setType(Type.PAY);
                    typeItemsMap.put(message.getFrom().getId(),typeItems.getType());
                    sendMessage.setChatId(message.getChatId());
                    Sendmsg(sendMessage);
                }
                    else if (text.equals("/start") && message.getChatId().equals(botConfig.getOwnerId()) && !message.getChatId().equals(botConfig.getOwnerId2())) {
                            sendMessage.setText("Assalomu alaykum *ORIENT PRIVATE SCHOOL * \uD83E\uDD16 ga xush kelibsiz ");
                            sendMessage.setParseMode("Markdown");
                            sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                                    InlineButtonUtil.row(InlineButtonUtil.button("barcha userlarni ko'rish\uD83D\uDDD3", "All_view")),
                                    InlineButtonUtil.row(InlineButtonUtil.button("Biz haqimizda / О нас \uD83D\uDCD1", "aboutUs")),
                                    InlineButtonUtil.row(InlineButtonUtil.button("Kurslarimiz / Наши курсы \uD83D\uDCDA", "cours")),
                                    InlineButtonUtil.row(InlineButtonUtil.button("delete_user\uD83D\uDDD1", "dell"))

                            )));
                            sendMessage.setChatId(message.getChatId());
                            Sendmsg(sendMessage);

                }  else if (text.equals("/start") && !message.getChatId().equals(botConfig.getOwnerId()) && message.getChatId().equals(botConfig.getOwnerId2())) {
                    sendMessage.setText("Assalomu alaykum *ORIENT PRIVATE SCHOOL * \uD83E\uDD16 ga xush kelibsiz ");
                    sendMessage.setParseMode("Markdown");
                    sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                            InlineButtonUtil.row(InlineButtonUtil.button("barcha userlarni ko'rish\uD83D\uDDD3", "All_view")),
                            InlineButtonUtil.row(InlineButtonUtil.button("Biz haqimizda / О нас \uD83D\uDCD1", "aboutUs")),
                            InlineButtonUtil.row(InlineButtonUtil.button("Kurslarimiz / Наши курсы \uD83D\uDCDA", "cours")),
                            InlineButtonUtil.row(InlineButtonUtil.button("delete_user\uD83D\uDDD1", "dell"))

                    )));
                    sendMessage.setChatId(message.getChatId());
                    Sendmsg(sendMessage);

                }
                    else if (text.equals("/help")) {
                    sendMessage.setText("Админ\n" +
                            "Admin\n" +
                            "@Sani_Math" +
                            "@a0922227");
                    sendMessage.setChatId(message.getChatId());
                    Sendmsg(sendMessage);
                }

                }

            }else {
            sendMessage.setText("\uD83E\uDD37\u200D♂️");
            sendMessage.setChatId(message.getChatId());
            Sendmsg(sendMessage);
        }
        }



//        System.out.println("1 "+user.getUserName());
    @Scheduled(cron = "00 07 * * * * ")
//    @Scheduled(cron = "2 * * * * *")
    public void scheduler(){

         List<Users> userRepositories = userRepository.findAllRegistredDateIsEquals(LocalDate.now());


        SendMessage sendMessage = new SendMessage();
        for (Users users : userRepositories) {
            if (users.getRegistered_At().equals(LocalDate.now())&& users.isPayment_verification()==true){
                users.setPayment_verification(false);
                userRepository.save(users);
            }
            if (users.getRegistered_At().equals(LocalDate.now())&& users.isPayment_verification() == false) {
                prepareSendMessage(users.getChatId());
//                var chatId = users.getChatId();
////                var chat = users.getChat();
//
//                users.setChatId(chatId);
//                users.setUserName(users.getUserName());
//                users.setFirstName(users.getFirstName());
//                users.setLastName(users.getLastName());
//                users.setRegistered_At(users.getRegistered_At().plusDays(28));
//                userRepository.save(users);
//                log.info("user saved: "+users);

            }
        }
    }
    public void prepareSendMessage(Long chtId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chtId);
        sendMessage.setText("To'lov vaqti keldi to'lov qilishingizni so'raymiz" +
                "Пришло время платить Мы просим вас заплатить");
        Sendmsg(sendMessage);
    }
    public void Sendmsg(SendMessage sendMessage){
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void EditMessage(EditMessageText editMessageText){
        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void SendImageI(SendPhoto sendPhoto){
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String RegisterUser(Message msg,Long id){
        Optional<Users> usersList = userRepository.findById(id);
        Optional<Temporality> tempList = temporalityRepository.findById(id);
//        for (Temporality temp : tempList) {


//        if (userRepository.findById(usersList.get().getChatId()).isEmpty()) {
//                var chatId = msg.getChatId();
//                var chat = msg.getChat();

            Users users = new Users();

            users.setChatId(tempList.get().getChatId());
            users.setUserName(tempList.get().getUserName());
            users.setFirstName(tempList.get().getFirstName());
            users.setLastName(tempList.get().getLastName());
            userRepository.save(users);
            log.info("user saved: " + users);
//        }else {
//            return "Siz ro'yxatdan o'tgansiz";
//            }
//        }
        return null;
    }

    private void RegisterTemp(Message msge){
        if (temporalityRepository.findById(msge.getChatId()).isEmpty()){
            Long chatId = msge.getChatId();
            var chat = msge.getChat();


            Temporality temporality = new Temporality();

            temporality.setChatId(chatId);
            temporality.setUserName(chat.getUserName());
            temporality.setFirstName(chat.getFirstName());
//            temporality.setLastName(chat.getLastName());
            temporalityRepository.save(temporality);
            log.info("User saved: "+temporality);

        }

    }


    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken(){
        return botConfig.getToke();
    }



}
