package ru.pascalcode.tasktracker.bot.updatehandler;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.pascalcode.tasktracker.bot.PrefixEmoji;
import ru.pascalcode.tasktracker.bot.CommandUtils;
import ru.pascalcode.tasktracker.bot.updatehandler.delete.DeleteUpdateHandler;
import ru.pascalcode.tasktracker.bot.updatehandler.edit.EditConfirmUpdateHandler;
import ru.pascalcode.tasktracker.bot.updatehandler.edit.EditUpdateHandler;
import ru.pascalcode.tasktracker.bot.updatehandler.home.*;
import ru.pascalcode.tasktracker.bot.updatehandler.home.report.TodayReportUpdateHandler;
import ru.pascalcode.tasktracker.bot.updatehandler.home.report.YesterdayReportUpdateHandler;
import ru.pascalcode.tasktracker.bot.updatehandler.setting.*;
import ru.pascalcode.tasktracker.bot.updatehandler.statictasks.HideStaticTaskUpdateHandler;
import ru.pascalcode.tasktracker.bot.updatehandler.statictasks.StaticTaskRecordUpdateHandler;
import ru.pascalcode.tasktracker.model.State;
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
        State state = userService.getStateOfUser(update);
        String text = update.getMessage().getText();
        if (CommandUtils.isCommand(text)) {
            state = State.HOME;
        }
        return switch (state) {
            case HOME -> getHomeUpdateHandler(text);
            case SETTINGS -> getSettingsUpdateHandler(text);
            case DELETE -> getDeleteUpdateHandler(text);
            case STATIC_TASKS -> getStaticTasksUpdateHandler(text);
            case EDIT -> getEditUpdateHandler(text);
            case EDIT_CONFIRM -> getEditConfirmUpdateHandler(text);
        };
    }

    private UpdateHandler getHomeUpdateHandler(String text) {
        if (text.startsWith("/") && !CommandUtils.isCommand(text)){
            return (TaskRecordUpdateHandler) applicationContext.getBean("taskRecordUpdateHandler");
        }
        return switch (text) {
            case START -> (StartUpdateHandler) applicationContext.getBean("startUpdateHandler");
            case DELETE -> (ToDeleteListUpdateHandler) applicationContext.getBean("toDeleteListUpdateHandler");
            case EDIT -> (ToEditListUpdateHandler) applicationContext.getBean("toEditListUpdateHandler");
            case STATIC_TASKS -> (ToStaticTasksSettingsUpdateHandler) applicationContext.getBean("toStaticTasksSettingsUpdateHandler");
            case SETTINGS -> (SettingsUpdateHandler) applicationContext.getBean("settingsUpdateHandler");
            case BREAK_BTN -> (BreakUpdateHandler) applicationContext.getBean("breakUpdateHandler");
            case TODAY_REPORT_BTN -> (TodayReportUpdateHandler) applicationContext.getBean("todayReportUpdateHandler");
            case YESTERDAY_REPORT_BTN -> (YesterdayReportUpdateHandler) applicationContext.getBean("yesterdayReportUpdateHandler");
            default -> (TaskRecordUpdateHandler) applicationContext.getBean("taskRecordUpdateHandler");
        };
    }

    private UpdateHandler getSettingsUpdateHandler(String text) {
        if (text.startsWith(PrefixEmoji.DAY)) {
            return (SetFirstDayOfWeekUpdateHandler) applicationContext.getBean("setFirstDayOfWeekUpdateHandler");
        } else if (text.startsWith(PrefixEmoji.HOUR)) {
            return (SetMinWeekHoursUpdateHandler) applicationContext.getBean("setMinWeekHoursUpdateHandler");
        }
        return switch (text) {
            case BACK_BTN -> (BackUpdateHandler) applicationContext.getBean("backUpdateHandler");
            case WEEK_HOURS_STATE_ON_BTN -> (EnableWeekProgressUpdateHandler) applicationContext.getBean("enableWeekProgressUpdateHandler");
            case WEEK_HOURS_STATE_OFF_BTN -> (DisableWeekProgressUpdateHandler) applicationContext.getBean("disableWeekProgressUpdateHandler");
            case ADD_NEW_TASKS_TO_BAR_ON_BTN -> (EnableAddNewTaskToBarUpdateHandler) applicationContext.getBean("enableAddNewTaskToBarUpdateHandler");
            case ADD_NEW_TASKS_TO_BAR_OFF_BTN -> (DisableAddNewTaskToBarUpdateHandler) applicationContext.getBean("disableAddNewTaskToBarUpdateHandler");
            case FIRST_DAY_OF_WEEK_BTN -> (ChooseFirstDayOfWeekUpdateHandler) applicationContext.getBean("chooseFirstDayOfWeekUpdateHandler");
            case MIN_WEEK_HOURS_BTN -> (ChooseMinWeekHoursUpdateHandler) applicationContext.getBean("chooseMinWeekHoursUpdateHandler");
            default -> (SettingsUpdateHandler) applicationContext.getBean("settingsUpdateHandler");
        };
    }

    private UpdateHandler getDeleteUpdateHandler(String text) {
        return switch (text) {
            case BACK_BTN -> (BackUpdateHandler) applicationContext.getBean("backUpdateHandler");
            default -> (DeleteUpdateHandler) applicationContext.getBean("deleteUpdateHandler");
        };
    }

    private UpdateHandler getEditUpdateHandler(String text) {
        return switch (text) {
            case BACK_BTN -> (BackUpdateHandler) applicationContext.getBean("backUpdateHandler");
            default -> (EditUpdateHandler) applicationContext.getBean("editUpdateHandler");
        };
    }

    private UpdateHandler getEditConfirmUpdateHandler(String text) {
        return switch (text) {
            case BACK_BTN -> (BackUpdateHandler) applicationContext.getBean("backUpdateHandler");
            default -> (EditConfirmUpdateHandler) applicationContext.getBean("editConfirmUpdateHandler");
        };
    }

    private UpdateHandler getStaticTasksUpdateHandler(String text) {

        if (text.startsWith(PrefixEmoji.DELETE)) {
            return (HideStaticTaskUpdateHandler) applicationContext.getBean("hideStaticTaskUpdateHandler");
        }
        return switch (text) {
            case BACK_BTN -> (BackUpdateHandler) applicationContext.getBean("backUpdateHandler");
            default -> (StaticTaskRecordUpdateHandler) applicationContext.getBean("staticTaskRecordUpdateHandler");
        };

    }
}
