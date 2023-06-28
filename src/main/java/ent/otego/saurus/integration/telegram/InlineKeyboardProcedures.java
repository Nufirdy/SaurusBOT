package ent.otego.saurus.integration.telegram;

import ent.otego.saurus.core.model.*;
import java.util.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class InlineKeyboardProcedures {

    public static InlineKeyboardMarkup getChooseTrackCategoryKeyboard() {
        InlineKeyboardButton campaign = getCallbackButton("Official tracks", "campaigns");
        InlineKeyboardButton custom = getCallbackButton("Custom tracks", "customs");
        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(campaign, custom))
                .build();
    }

    /**
     * @param page номер страницы начиная от 0
     */
    static InlineKeyboardMarkup getCampaignsKeyboard(List<Campaign> campaigns, int page) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        for (int i = page * 4; i != campaigns.size() && i < page * 4 + 4; ++i) {
            Campaign campaign = campaigns.get(i);
            List<InlineKeyboardButton> campaignRow =
                    List.of(getCallbackButton(campaign.getName(), "campaign " + i));
            keyboard.add(campaignRow);
        }

        List<InlineKeyboardButton> backButtonRow =
                List.of(getCallbackButton("..", "track_categories"));
        keyboard.add(backButtonRow);

        List<InlineKeyboardButton> pagesButtonsRow = new ArrayList<>();
        if (campaigns.size() / 4 == page + 1) {
            pagesButtonsRow.add(getPrevPageButton(page - 1));
        } else if (page == 0) {
            pagesButtonsRow.add(getNextPageButton(page + 1));
        } else {
            pagesButtonsRow.add(getPrevPageButton(page - 1));
            pagesButtonsRow.add(getNextPageButton(page + 1));
        }
        keyboard.add(pagesButtonsRow);

        return InlineKeyboardMarkup.builder()
                .keyboard(keyboard)
                .build();
    }

    static InlineKeyboardMarkup getCampaignTracksKeyboard(Campaign campaign) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        //считается по-умолчанию, что это официальная кампания с 25 трассами
        for (int i = 0; i < 5; i++) {
            keyboard.add(new ArrayList<>());
        }
        for (int i = 0; i < 25; i++) {
            MapInfo map = campaign.getPlaylist().get(i);
            InlineKeyboardButton trackButton =
                    getCallbackButton(getOfficialTrackNumber(map), "track " + map.getId());
            keyboard.get((i) % 5).add(trackButton);
        }

        keyboard.add(List.of(getCallbackButton("..", "campaigns")));

        return InlineKeyboardMarkup.builder()
                .keyboard(keyboard)
                .build();
    }

    private static String getOfficialTrackNumber(MapInfo mapInfo) {
        String campaignName = mapInfo.getCampaign().getName();
        String mapName = mapInfo.getName();
        return mapName.replace(campaignName + " - ", "");
    }

    static InlineKeyboardButton getNextPageButton(int destPage) {
        return getCallbackButton(">>>", "page " + destPage);
    }

    static InlineKeyboardButton getPrevPageButton(int destPage) {
        return getCallbackButton("<<<", "page " + destPage);
    }

    static InlineKeyboardButton getCallbackButton(String text, String callback) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callback)
                .build();
    }
}
