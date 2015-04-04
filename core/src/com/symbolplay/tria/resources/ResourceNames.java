package com.symbolplay.tria.resources;

import com.symbolplay.gamelibrary.resources.IndexedResourceName;

public final class ResourceNames {
    
    // user data
    private static final String USER_DATA_DIR = "userdata/";
    public static final String GAME_PREFERENCES_FILE = USER_DATA_DIR + "preferences";
    public static final String USER_DATA_FILE = USER_DATA_DIR + "userdata";
    
    // data
    private static final String DATA_DIR = "data/";
    public static final String CREDITS = DATA_DIR + "credits.txt";
    
    // graphics
    private static final String GRAPHICS_PACKED_DIR = "graphics/packed/";
    
    public static final String GRAPHICS_GUI_ATLAS = GRAPHICS_PACKED_DIR + "gui.atlas"; 
    
    public static final String GUI_WHITE_NINE_IMAGE_NAME = "whitenine";
    public static final String GUI_WAIT_ANIMATION_32_IMAGE_NAME = "waitanimation32";
    
    private static final String GRAPHICS_GUI_ATLAS_MAIN_MENU_PREFIX = "mainmenu/";
    public static final String GUI_MAIN_MENU_NEWS_BUTTON_IMAGE_NAME = GRAPHICS_GUI_ATLAS_MAIN_MENU_PREFIX + "newsbutton";
    public static final String GUI_MAIN_MENU_NEWS_BUTTON_HIGHLIGHT_IMAGE_NAME = GRAPHICS_GUI_ATLAS_MAIN_MENU_PREFIX + "newsbuttonhighlight";
    public static final String GUI_MAIN_MENU_CREDITS_BUTTON_IMAGE_NAME = GRAPHICS_GUI_ATLAS_MAIN_MENU_PREFIX + "creditsbutton";
    public static final String GUI_MAIN_MENU_INSTRUCTIONS_BUTTON_IMAGE_NAME = GRAPHICS_GUI_ATLAS_MAIN_MENU_PREFIX + "instructionsbutton";
    public static final String GUI_MAIN_MENU_INSTRUCTIONS_BUTTON_HIGHLIGHT_IMAGE_NAME = GRAPHICS_GUI_ATLAS_MAIN_MENU_PREFIX + "instructionsbuttonhighlight";
    
    private static final String GRAPHICS_GUI_ATLAS_NEWS_PREFIX = "news/";
    public static final String GUI_NEWS_NEWS_SEPARATOR_IMAGE_NAME = GRAPHICS_GUI_ATLAS_NEWS_PREFIX + "newsseparator";
    
    private static final String GRAPHICS_GUI_ATLAS_CREDITS_PREFIX = "credits/";
    public static final String GUI_CREDITS_COMPANY_LOGO_IMAGE_NAME = GRAPHICS_GUI_ATLAS_CREDITS_PREFIX + "companylogo";
    public static final String GUI_CREDITS_LIBGDX_LOGO_IMAGE_NAME = GRAPHICS_GUI_ATLAS_CREDITS_PREFIX + "libgdxlogo";
    
    private static final String GRAPHICS_GUI_ATLAS_PLAY_PREFIX = "play/";
    public static final String GUI_PLAY_TOP_BACKGROUND_IMAGE_NAME = GRAPHICS_GUI_ATLAS_PLAY_PREFIX + "topbackground";
    public static final String GUI_PLAY_TOP_LINE_IMAGE_NAME = GRAPHICS_GUI_ATLAS_PLAY_PREFIX + "topline";
    public static final String GUI_PLAY_PAUSE_IMAGE_NAME = GRAPHICS_GUI_ATLAS_PLAY_PREFIX + "pause";
    public static final String GUI_PLAY_LIVES_IMAGE_NAME = GRAPHICS_GUI_ATLAS_PLAY_PREFIX + "lives";
    public static final String GUI_PLAY_COINS_IMAGE_NAME = GRAPHICS_GUI_ATLAS_PLAY_PREFIX + "coins";
    
    private static final String GRAPHICS_GUI_ATLAS_UPGRADE_PREFIX = "upgrade/";
    public static final String GUI_UPGRADE_UPGRADE_ITEM_SELECTION_IMAGE_NAME = GRAPHICS_GUI_ATLAS_UPGRADE_PREFIX + "upgradeitemselection";
    public static final String GUI_UPGRADE_UPGRADE_ITEM_ROCKET_IMAGE_NAME = GRAPHICS_GUI_ATLAS_UPGRADE_PREFIX + "upgradeitemrocket";
    public static final String GUI_UPGRADE_UPGRADE_ITEM_ANTI_GRAVITY_IMAGE_NAME = GRAPHICS_GUI_ATLAS_UPGRADE_PREFIX + "upgradeitemantigravity";
    public static final String GUI_UPGRADE_UPGRADE_ITEM_LIFE_IMAGE_NAME = GRAPHICS_GUI_ATLAS_UPGRADE_PREFIX + "upgradeitemlife";
    public static final String GUI_UPGRADE_UPGRADE_LEVEL_EMPTY_IMAGE_NAME = GRAPHICS_GUI_ATLAS_UPGRADE_PREFIX + "upgradelevelempty";
    public static final String GUI_UPGRADE_UPGRADE_LEVEL_FULL_IMAGE_NAME = GRAPHICS_GUI_ATLAS_UPGRADE_PREFIX + "upgradelevelfull";
    
    private static final String GRAPHICS_GUI_ATLAS_HOW_TO_PLAY_PREFIX = "howtoplay/";
    public static final String GUI_HOW_TO_PLAY_CHARACTER_CONTROL_IMAGE_NAME = GRAPHICS_GUI_ATLAS_HOW_TO_PLAY_PREFIX + "charactercontrol";
    public static final String GUI_HOW_TO_PLAY_GO_THROUGH_SIDES_IMAGE_NAME = GRAPHICS_GUI_ATLAS_HOW_TO_PLAY_PREFIX + "gothroughsides";
    public static final String GUI_HOW_TO_PLAY_ITEMS_IMAGE_NAME = GRAPHICS_GUI_ATLAS_HOW_TO_PLAY_PREFIX + "items";
    public static final String GUI_HOW_TO_PLAY_ENEMIES_IMAGE_NAME = GRAPHICS_GUI_ATLAS_HOW_TO_PLAY_PREFIX + "enemies";
    
    public static final String PLATFORMS_ATLAS = GRAPHICS_PACKED_DIR + "platforms.atlas";
    public static final String PLATFORM_NORMAL_IMAGE_NAME = "platformnormal";
    public static final String PLATFORM_CRUMBLE_IMAGE_NAME = "platformcrumble";
    public static final String PLATFORM_MOVEMENT_HORIZONTAL_IMAGE_NAME = "movementhorizontal";
    public static final String PLATFORM_MOVEMENT_REPOSITION_IMAGE_NAME = "movementreposition";
    public static final String PLATFORM_FEATURE_VISIBLE_ON_JUMP_FOREGROUND_IMAGE_NAME = "featurevisibleonjumpforeground";
    public static final String PLATFORM_FEATURE_REVEAL_ON_JUMP_FOREGROUND_IMAGE_NAME = "featurerevealonjumpforeground";
    public static final String PLATFORM_FEATURE_TIMED_SPIKES_ACTIVE_COUNTDOWN_IMAGE_NAME = "featuretimedspikesactivecountdown";
    public static final String PLATFORM_FEATURE_TIMED_SPIKES_INACTIVE_COUNTDOWN_IMAGE_NAME = "featuretimedspikesinactivecountdown";
    public static final String PLATFORM_FEATURE_TOGGLE_SPIKES_ACTIVE_FOREGROUND_IMAGE_NAME = "featuretogglespikesactiveforeground";
    public static final String PLATFORM_FEATURE_TOGGLE_SPIKES_INACTIVE_FOREGROUND_IMAGE_NAME = "featuretogglespikesinactiveforeground";
    public static final String PLATFORM_JUMP_BOOST_SMALL_EXTENDED_IMAGE_NAME = "jumpboostsmallextended";
    public static final String PLATFORM_JUMP_BOOST_SMALL_CONTRACTED_IMAGE_NAME = "jumpboostsmallcontracted";
    public static final String PLATFORM_JUMP_BOOST_MEDIUM_EXTENDED_IMAGE_NAME = "jumpboostmediumextended";
    public static final String PLATFORM_JUMP_BOOST_MEDIUM_CONTRACTED_IMAGE_NAME = "jumpboostmediumcontracted";
    public static final String PLATFORM_JUMP_BOOST_LARGE_EXTENDED_IMAGE_NAME = "jumpboostlargeextended";
    public static final String PLATFORM_JUMP_BOOST_LARGE_CONTRACTED_IMAGE_NAME = "jumpboostlargecontracted";
    public static final String PLATFORM_BOTTOM_SPIKES_IMAGE_NAME = "bottomspikes";
    public static final String PLATFORM_LEFT_SPIKES_IMAGE_NAME = "leftspikes";
    public static final String PLATFORM_RIGHT_SPIKES_IMAGE_NAME = "rightspikes";
    public static final String PLATFORM_SPIKES_IMAGE_NAME = "spikes";
    
    public static final String CHARACTER_ATLAS = GRAPHICS_PACKED_DIR + "character.atlas";
    public static final String CHARACTER_BODY_IMAGE_NAME = "body";
    public static final String CHARACTER_ARM_IMAGE_NAME = "arm";
    public static final String CHARACTER_LEGS_IMAGE_NAME = "legs";
    public static final String CHARACTER_SHIELD_EFFECT_IMAGE_NAME = "shieldeffect";
    public static final String CHARACTER_ANTI_GRAVITY_ENGINE_IMAGE_NAME = "antigravityengine";
    public static final IndexedResourceName CHARACTER_ANTI_GRAVITY_EXHAUST_ELEMENT_RESOURCE_NAMES;
    public static final String CHARACTER_ROCKET_ENGINE_IMAGE_NAME = "rocketengine";
    public static final String CHARACTER_ROCKET_EXHAUST_IMAGE_NAME = "rocketexhaust";
    public static final String CHARACTER_UPGRADE_DOTS_IMAGE_NAME = "upgradedots";
    public static final String CHARACTER_AUTO_PICK_UP_LINE_IMAGE_NAME = "autopickupline";
    
    public static final String ENEMIES_ATLAS = GRAPHICS_PACKED_DIR + "enemies.atlas";
    public static final String ENEMY_SAW_IMAGE_NAME = "saw";
    public static final String ENEMY_SINE_PATROLLER_IMAGE_NAME = "sinepatroller";
    public static final String ENEMY_EASE_PATROLLER_IMAGE_NAME = "easepatroller";
    public static final String ENEMY_ORBITER_IMAGE_NAME = "orbiter";
    public static final String ENEMY_EVIL_TWIN_IMAGE_NAME = "eviltwin";
    public static final String ENEMY_PLATFORM_PATROLLER_IMAGE_NAME = "platformpatroller";
    public static final IndexedResourceName ENEMY_STATIC_RESOURCE_NAMES;
    
    public static final String ITEMS_ATLAS = GRAPHICS_PACKED_DIR + "items.atlas";
    public static final String ITEM_SHIELD_IMAGE_NAME = "shield";
    public static final String ITEM_LIFE_IMAGE_NAME = "life";
    public static final String ITEM_ANTI_GRAVITY_IMAGE_NAME = "antigravity";
    public static final String ITEM_ROCKET_IMAGE_NAME = "rocket";
    public static final String ITEM_COIN_1_IMAGE_NAME = "coin1";
    public static final String ITEM_COIN_5_IMAGE_NAME = "coin5";
    public static final String ITEM_COIN_10_IMAGE_NAME = "coin10";
    
    public static final String BACKGROUND_ATLAS = GRAPHICS_PACKED_DIR + "background.atlas";
    public static final String BACKGROUND_IMAGE_NAME = "background";
    public static final String BACKGROUND_PLATFORM_IMAGE_NAME = "backgroundplatform";
    public static final String BACKGROUND_FOREGROUND_SIDE_LEFT_IMAGE_NAME = "foregroundsideleft";
    public static final String BACKGROUND_FOREGROUND_SIDE_RIGHT_IMAGE_NAME = "foregroundsideright";
    public static final String BACKGROUND_SCORE_LINE_IMAGE_NAME = "scoreline";
    
    // gui skin
    private static final String GUI_DIR = "gui/";
    public static final String GUI_SKIN = GUI_DIR + "guiskin.json";
    public static final String GUI_SKIN_ATLAS = GRAPHICS_PACKED_DIR + "guiskin.atlas"; 
    
    // fonts
    private static final String FONTS_DIR = "fonts/";
    public static final String FONT_TTF = FONTS_DIR + "OpenSans-Regular.ttf";
    public static final String FONT_SKIN_NAME_DEFAULT = "font-default";
    public static final String FONT_SKIN_NAME_18 = "font-18";
    public static final String FONT_SKIN_NAME_24 = "font-24";
    public static final String FONT_SKIN_NAME_32 = "font-32";
    public static final String FONT_SKIN_NAME_40 = "font-40";
    
    // sounds
    private static final String SOUNDS_DIR = "sounds/";
    public static final String SOUND_JUMP = SOUNDS_DIR + "jump.wav";
    public static final String SOUND_JUMP_BOOST = SOUNDS_DIR + "jumpboost.wav";
    public static final String SOUND_COIN = SOUNDS_DIR + "coin.wav";
    public static final String SOUND_ITEM = SOUNDS_DIR + "item.wav";
    public static final String SOUND_OUCH = SOUNDS_DIR + "ouch.wav";
    public static final String SOUND_SAW = SOUNDS_DIR + "saw.wav";
    public static final String SOUND_SPIKES = SOUNDS_DIR + "spikes.wav";
    public static final String SOUND_FALL = SOUNDS_DIR + "fall.wav";
    public static final String SOUND_ROCKET = SOUNDS_DIR + "rocket.wav";
    public static final String SOUND_ANTI_GRAVITY = SOUNDS_DIR + "antigravity.wav";
    
    static {
        CHARACTER_ANTI_GRAVITY_EXHAUST_ELEMENT_RESOURCE_NAMES = new IndexedResourceName("antigravityexhaustelement", "", 3);
        ENEMY_STATIC_RESOURCE_NAMES = new IndexedResourceName("static", "", 4);
    }
}
