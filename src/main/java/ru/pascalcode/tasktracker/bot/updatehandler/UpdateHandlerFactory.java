package ru.pascalcode.tasktracker.bot.updatehandler;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.pascalcode.tasktracker.bot.PrefixEmoji;
import ru.pascalcode.tasktracker.bot.updatehandler.impl.*;
import ru.pascalcode.tasktracker.bot.updatehandler.report.TodayReportUpdateHandler;
import ru.pascalcode.tasktracker.bot.updatehandler.report.YesterdayReportUpdateHandler;
import ru.pascalcode.tasktracker.bot.updatehandler.setting.*;
import ru.pascalcode.tasktracker.service.UserService;

import static ru.pascalcode.tasktracker.bot.Buttons.*;
import static ru.pascalcode.tasktracker.bot.Commands.*;

@Component
public class UpdateHandlerFactory {

    private final UserService userService;

    private final ApplicationContext applicationContext;

    public UpdateHandlerFactory(ApplicationContext applicationContext, UserService userService) {
        this.applicationContext = applicationContext;
        this.userService = userService;
    }

    public UpdateHandler getUpdateHandler(Update update) {
        switch (update.getMessage()
                      .getText()) {
            case START:
                return (StartUpdateHandler) applicationContext.getBean("startUpdateHandler");
            case COMPLETE:
                return (ToCompleteListUpdateHandler) applicationContext.getBean("toCompleteListUpdateHandler");
            case DELETE:
                return (ToDeleteListUpdateHandler) applicationContext.getBean("toDeleteListUpdateHandler");
            case SETTINGS:
                return (SettingsUpdateHandler) applicationContext.getBean("settingsUpdateHandler");

            case BREAK_BTN:
                return (ChillUpdateHandler) applicationContext.getBean("chillUpdateHandler");
            case TODAY_REPORT_BTN:
                return (TodayReportUpdateHandler) applicationContext.getBean("todayReportUpdateHandler");
            case YESTERDAY_REPORT_BTN:
                return (YesterdayReportUpdateHandler) applicationContext.getBean("yesterdayReportUpdateHandler");
            case BACK_BTN:
                return (BackUpdateHandler) applicationContext.getBean("backUpdateHandler");

            case WEEK_HOURS_STATE_ON_BTN:
                return (EnableWeekProgressUpdateHandler) applicationContext.getBean("enableWeekProgressUpdateHandler");
            case WEEK_HOURS_STATE_OFF_BTN:
                return (DisableWeekProgressUpdateHandler) applicationContext.getBean("disableWeekProgressUpdateHandler");
            case ADD_NEW_TASKS_TO_BAR_ON_BTN:
                return (EnableAddNewTaskToBarUpdateHandler) applicationContext.getBean("enableAddNewTaskToBarUpdateHandler");
            case ADD_NEW_TASKS_TO_BAR_OFF_BTN:
                return (DisableAddNewTaskToBarUpdateHandler) applicationContext.getBean("disableAddNewTaskToBarUpdateHandler");
            case FIRST_DAY_OF_WEEK_BTN:
                return (ChooseFirstDayOfWeekUpdateHandler) applicationContext.getBean("chooseFirstDayOfWeekUpdateHandler");
            case MIN_WEEK_HOURS_BTN:
                return (ChooseMinWeekHoursUpdateHandler) applicationContext.getBean("chooseMinWeekHoursUpdateHandler");

            default:
                String text = update.getMessage()
                                    .getText();
                if (text.startsWith(PrefixEmoji.COMPLETE)) {
                    return (CompleteUpdateHandler) applicationContext.getBean("completeUpdateHandler");
                } else if (text.startsWith(PrefixEmoji.DELETE)) {
                    return (DeleteUpdateHandler) applicationContext.getBean("deleteUpdateHandler");
                } else if (text.startsWith(PrefixEmoji.DAY)) {
                    return (SetFirstDayOfWeekUpdateHandler) applicationContext.getBean("setFirstDayOfWeekUpdateHandler");
                } else if (text.startsWith(PrefixEmoji.HOUR)) {
                    return (SetMinWeekHoursUpdateHandler) applicationContext.getBean("setMinWeekHoursUpdateHandler");
                }
                return (TaskRecordUpdateHandler) applicationContext.getBean("taskRecordUpdateHandler");
        }
    }
}
