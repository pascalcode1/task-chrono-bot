package ru.pascalcode.tasktracker.bot.updatehandler;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.pascalcode.tasktracker.bot.Emoji;
import ru.pascalcode.tasktracker.bot.CommandUtils;
import ru.pascalcode.tasktracker.bot.updatehandler.delete.DeleteUpdateHandler;
import ru.pascalcode.tasktracker.bot.updatehandler.edittasks.EditTaskConfirmUpdateHandler;
import ru.pascalcode.tasktracker.bot.updatehandler.edittasks.EditTaskUpdateHandler;
import ru.pascalcode.tasktracker.bot.updatehandler.edittimerecords.DeleteTimeRecordUpdateHandler;
import ru.pascalcode.tasktracker.bot.updatehandler.edittimerecords.EditTimeOfTimeRecordConfirmUpdateHandler;
import ru.pascalcode.tasktracker.bot.updatehandler.edittimerecords.EditTimeOfTimeRecordUpdateHandler;
import ru.pascalcode.tasktracker.bot.updatehandler.edittimerecords.EditTimeRecordUpdateHandler;
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
            case PINNED_TASKS -> getStaticTasksUpdateHandler(text);
            case EDIT_TASKS -> getEditUpdateHandler(text);
            case EDIT_CONFIRM -> getEditConfirmUpdateHandler(text);
            case EDIT_TIME_RECORDS_LIST -> getEditTimeRecordsUpdateHandler(text);
            case EDIT_TIME_RECORD -> getEditTimeRecordUpdateHandler(text);
            case EDIT_TIME_RECORD_CONFIRM -> getEditTimeRecordConfirmUpdateHandler(text);
        };
    }

    private UpdateHandler getHomeUpdateHandler(String text) {
        if (text.startsWith("/") && !CommandUtils.isCommand(text)){
            return (TaskRecordUpdateHandler) applicationContext.getBean("taskRecordUpdateHandler");
        }
        return switch (text) {
            case START -> (StartUpdateHandler) applicationContext.getBean("startUpdateHandler");
            case DELETE_TASKS -> (ToDeleteListUpdateHandler) applicationContext.getBean("toDeleteListUpdateHandler");
            case EDIT_TASKS -> (ToEditListUpdateHandler) applicationContext.getBean("toEditListUpdateHandler");
            case EDIT_TIME_RECORDS -> (ToEditTimeRecordsListUpdateHandler) applicationContext.getBean("toEditTimeRecordsListUpdateHandler");
            case PINNED_TASKS -> (ToStaticTasksSettingsUpdateHandler) applicationContext.getBean("toStaticTasksSettingsUpdateHandler");
            case SETTINGS -> (SettingsUpdateHandler) applicationContext.getBean("settingsUpdateHandler");
            case BREAK_BTN -> (BreakUpdateHandler) applicationContext.getBean("breakUpdateHandler");
            case TODAY_REPORT_BTN -> (TodayReportUpdateHandler) applicationContext.getBean("todayReportUpdateHandler");
            case YESTERDAY_REPORT_BTN -> (YesterdayReportUpdateHandler) applicationContext.getBean("yesterdayReportUpdateHandler");
            default -> (TaskRecordUpdateHandler) applicationContext.getBean("taskRecordUpdateHandler");
        };
    }

    private UpdateHandler getSettingsUpdateHandler(String text) {
        if (text.startsWith(Emoji.DAY)) {
            return (SetFirstDayOfWeekUpdateHandler) applicationContext.getBean("setFirstDayOfWeekUpdateHandler");
        } else if (text.startsWith(Emoji.HOUR)) {
            return (SetMinWeekHoursUpdateHandler) applicationContext.getBean("setMinWeekHoursUpdateHandler");
        }
        return switch (text) {
            case BACK_BTN -> (BackUpdateHandler) applicationContext.getBean("backUpdateHandler");
            case WEEK_HOURS_STATE_ON_BTN -> (EnableWeekProgressUpdateHandler) applicationContext.getBean("enableWeekProgressUpdateHandler");
            case WEEK_HOURS_STATE_OFF_BTN -> (DisableWeekProgressUpdateHandler) applicationContext.getBean("disableWeekProgressUpdateHandler");
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
            default -> (EditTaskUpdateHandler) applicationContext.getBean("editTaskUpdateHandler");
        };
    }

    private UpdateHandler getEditConfirmUpdateHandler(String text) {
        return switch (text) {
            case BACK_BTN -> (BackUpdateHandler) applicationContext.getBean("backUpdateHandler");
            default -> (EditTaskConfirmUpdateHandler) applicationContext.getBean("editTaskConfirmUpdateHandler");
        };
    }

    private UpdateHandler getStaticTasksUpdateHandler(String text) {
        if (text.startsWith(Emoji.DELETE)) {
            return (HideStaticTaskUpdateHandler) applicationContext.getBean("hideStaticTaskUpdateHandler");
        }
        return switch (text) {
            case BACK_BTN -> (BackUpdateHandler) applicationContext.getBean("backUpdateHandler");
            default -> (StaticTaskRecordUpdateHandler) applicationContext.getBean("staticTaskRecordUpdateHandler");
        };

    }

    private UpdateHandler getEditTimeRecordsUpdateHandler(String text) {
        if (text.startsWith(Emoji.EDIT)) {
            return (EditTimeRecordUpdateHandler) applicationContext.getBean("editTimeRecordUpdateHandler");
        }
        return switch (text) {
            case BACK_BTN -> (BackUpdateHandler) applicationContext.getBean("backUpdateHandler");
            default -> (ToEditTimeRecordsListUpdateHandler) applicationContext.getBean("toEditTimeRecordsListUpdateHandler");
        };
    }

    private UpdateHandler getEditTimeRecordUpdateHandler(String text) {
        if (text.startsWith(Emoji.EDIT)) {
            return (EditTimeOfTimeRecordUpdateHandler) applicationContext.getBean("editTimeOfTimeRecordUpdateHandler");
        }
        if (text.startsWith(Emoji.DELETE)) {
            return (DeleteTimeRecordUpdateHandler) applicationContext.getBean("deleteTimeRecordUpdateHandler");
        }
        return switch (text) {
            case BACK_BTN -> (BackUpdateHandler) applicationContext.getBean("backUpdateHandler");
            default -> (ToEditTimeRecordsListUpdateHandler) applicationContext.getBean("toEditTimeRecordsListUpdateHandler");
        };
    }

    private UpdateHandler getEditTimeRecordConfirmUpdateHandler(String text) {
        return switch (text) {
            case BACK_BTN -> (BackUpdateHandler) applicationContext.getBean("backUpdateHandler");
            default -> (EditTimeOfTimeRecordConfirmUpdateHandler) applicationContext.getBean("editTimeOfTimeRecordConfirmUpdateHandler");
        };
    }
}
