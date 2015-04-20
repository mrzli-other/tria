package com.symbolplay.tria.game.rise;

import java.util.Comparator;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.symbolplay.gamelibrary.game.CameraData;
import com.symbolplay.gamelibrary.util.IntRange;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.debug.DebugData;
import com.symbolplay.tria.game.GameInput;
import com.symbolplay.tria.game.PlatformToCharCollisionData;
import com.symbolplay.tria.game.background.GameBackground;
import com.symbolplay.tria.game.background.GameForeground;
import com.symbolplay.tria.game.character.GameCharacter;
import com.symbolplay.tria.game.enemies.EnemyBase;
import com.symbolplay.tria.game.items.ItemBase;
import com.symbolplay.tria.game.platforms.Platform;
import com.symbolplay.tria.game.rise.generators.RiseGenerator;
import com.symbolplay.tria.game.rise.generators.RiseGeneratorData;
import com.symbolplay.tria.persistence.userdata.CareerData;
import com.symbolplay.tria.screens.playscore.PlayScoreAccessor;
import com.symbolplay.tria.screens.playscore.PlayScoreData;
import com.symbolplay.tria.screens.playscore.PlayScoreListener;

public final class Rise {
    
    private static final double VISIBLE_AREA_POSITION_STRIDE = 10000.0;
    private static final float ACTIVE_AREA_PADDING = 5.0f;
    private static final float CHARACTER_POSITION_AREA_FRACTION = 0.45f;
    private static final float VISIBLE_AREA_GENERATE_OFFSET = GameContainer.GAME_AREA_HEIGHT * 2.0f;
    
    private static final int HEIGHT_SCORE_MULTIPLIER = 40;
    
    private static final int PLATFORMS_CAPACITY = 200;
    private static final int ENEMIES_CAPACITY = 50;
    private static final int ITEMS_CAPACITY = 100;
    
    private static final Comparator<ScoreLineObject> SCORE_LINE_1_COMPARATOR;
    private static final Comparator<ScoreLineObject> SCORE_LINE_2_COMPARATOR;
    
    private static final int SPRITE_BATCH_SIZE = 100;
    
    private final AssetManager assetManager;
    
    private final PlayScoreAccessor playScoreAccessor;
    
    private final SpriteBatch batch;
    private final DebugData debugData;
    
    private final CameraData cameraData;
    private final BitmapFont gameAreaFont;
    
    private final RiseGenerator riseGenerator;
    private final RiseGeneratorData riseGeneratorData;
    private final RiseActiveData riseActiveData;
    
    private final Array<Platform> platforms;
    private final Array<EnemyBase> enemies;
    private final Array<ItemBase> items;
    
    private final GameCharacter character;
    
    private final GameBackground gameBackground;
    private final GameForeground gameForeground;
    private Array<ScoreLineObject> scoreLines;
    
    private final PlatformToCharCollisionData platformToCharCollisionData;
    
    private int visibleAreaStrideCount;
    private float visibleAreaRelativePosition;
    private float visibleAreaRelativePositionChange;
    
    private int heightScore;
    
    static {
        SCORE_LINE_1_COMPARATOR = new Comparator<ScoreLineObject>() {
            
            @Override
            public int compare(ScoreLineObject s1, ScoreLineObject s2) {
                return (int) -Math.signum(s1.getPositionY() - s2.getPositionY());
            }
        };
        
        SCORE_LINE_2_COMPARATOR = new Comparator<ScoreLineObject>() {
            
            @Override
            public int compare(ScoreLineObject s1, ScoreLineObject s2) {
                return (int) Math.signum(s1.getPositionY() - s2.getPositionY());
            }
        };
    }
    
    public Rise(CameraData cameraData, CareerData careerData, BitmapFont gameAreaFont, PlayScoreAccessor playScoreAccessor, AssetManager assetManager) {
        
        this.assetManager = assetManager;
        
        this.playScoreAccessor = playScoreAccessor;
        playScoreAccessor.setPlayScoreListener(new PlayScoreListener() {
            
            @Override
            public void scoreReceived(Array<PlayScoreData> playScores) {
                scoreLines = getScoreLines(playScores);
            }
        });
        
        batch = new SpriteBatch(SPRITE_BATCH_SIZE);
        debugData = new DebugData();
        
        this.cameraData = cameraData;
        this.gameAreaFont = gameAreaFont;
        
        riseGenerator = new RiseGenerator(assetManager);
        riseGeneratorData = new RiseGeneratorData();
        riseActiveData = new RiseActiveData();
        
        platforms = new Array<Platform>(true, PLATFORMS_CAPACITY);
        enemies = new Array<EnemyBase>(true, ENEMIES_CAPACITY);
        items = new Array<ItemBase>(true, ITEMS_CAPACITY);
        
        character = new GameCharacter(careerData, assetManager);
        
        gameBackground = new GameBackground(assetManager);
        gameForeground = new GameForeground(assetManager);
        
        platformToCharCollisionData = new PlatformToCharCollisionData();
        
        scoreLines = createEmptyScoreLines();
    }
    
    public void reset() {
        riseGenerator.reset();
        riseGeneratorData.resetComplete();
        
        gameBackground.reset();
        
        platforms.clear();
        enemies.clear();
        items.clear();
        
        character.reset();
        
        visibleAreaStrideCount = 0;
        visibleAreaRelativePosition = 0.0f;
        visibleAreaRelativePositionChange = 0.0f;
        
        heightScore = 0;
        
        scoreLines = createEmptyScoreLines();
        playScoreAccessor.requestScores();
    }
    
    public void update(float delta) {
        if (isFinished()) {
            return;
        }
        
        float horizontalSpeed = GameInput.getHorizontalSpeed();
        
        if (visibleAreaRelativePosition >= VISIBLE_AREA_POSITION_STRIDE) {
            updateVisibleAreaStrideCount();
        }
        
        gameBackground.update(delta, visibleAreaRelativePosition);
        updateRise();
        
        updateScoreLines(delta);
        updatePlatforms(delta);
        updateEnemies(delta);
        updateItems(delta);
        
        character.update(
                horizontalSpeed,
                platformToCharCollisionData,
                platforms,
                enemies,
                items,
                riseActiveData,
                visibleAreaRelativePosition,
                delta);
        
        float charPositionY = character.getPosition().y;
        
        float charVisibleAreaPosition = charPositionY - GameContainer.GAME_AREA_HEIGHT * CHARACTER_POSITION_AREA_FRACTION;
        float newVisibleAreaRelativePosition = Math.max(charVisibleAreaPosition, visibleAreaRelativePosition);
        visibleAreaRelativePositionChange = newVisibleAreaRelativePosition - visibleAreaRelativePosition;
        visibleAreaRelativePosition = newVisibleAreaRelativePosition;
        
        int currentHeightScore = (int) ((charPositionY + visibleAreaStrideCount * VISIBLE_AREA_POSITION_STRIDE) * HEIGHT_SCORE_MULTIPLIER);
        heightScore = Math.max(currentHeightScore, heightScore);
    }
    
    public void render() {
        cameraData.setGameAreaPosition(0.0f, visibleAreaRelativePosition);
        
        debugData.initializeFrameData();
        
        startGameAreaBatch();
        gameBackground.render(batch, visibleAreaRelativePosition, visibleAreaRelativePositionChange);
        endBatch();
        
        startGameAreaBatch();
        renderRiseObjects();
        renderScoreLines();
        endBatch();
        
        startGuiBatch();
        renderScoreLinesText();
        renderItemsText();
        endBatch();
        
        startGameAreaBatch();
        character.render(batch);
        gameForeground.render(batch, visibleAreaRelativePosition);
        endBatch();
    }
    
    private void renderScoreLines() {
        int minActiveIndex = riseActiveData.minActiveScoreLineIndex;
        int maxActiveIndex = riseActiveData.maxActiveScoreLineIndex;
        
        for (int i = minActiveIndex; i <= maxActiveIndex; i++) {
            ScoreLineObject scoreLine = scoreLines.get(i);
            scoreLine.render(batch);
        }
    }
    
    private void renderScoreLinesText() {
        int minActiveIndex = riseActiveData.minActiveScoreLineIndex;
        int maxActiveIndex = riseActiveData.maxActiveScoreLineIndex;
        
        for (int i = minActiveIndex; i <= maxActiveIndex; i++) {
            ScoreLineObject scoreLine = scoreLines.get(i);
            scoreLine.renderText(batch, visibleAreaRelativePosition);
        }
    }
    
    private void renderRiseObjects() {
        int minActiveIndex;
        int maxActiveIndex;
        
        minActiveIndex = riseActiveData.minActivePlatformIndex;
        maxActiveIndex = riseActiveData.maxActivePlatformIndex;
        for (int i = minActiveIndex; i <= maxActiveIndex; i++) {
            Platform platform = platforms.get(i);
            platform.render(batch);
        }
        
        minActiveIndex = riseActiveData.minActiveItemIndex;
        maxActiveIndex = riseActiveData.maxActiveItemIndex;
        for (int i = minActiveIndex; i <= maxActiveIndex; i++) {
            ItemBase item = items.get(i);
            item.render(batch);
        }
        
        minActiveIndex = riseActiveData.minActiveEnemyIndex;
        maxActiveIndex = riseActiveData.maxActiveEnemyIndex;
        for (int i = minActiveIndex; i <= maxActiveIndex; i++) {
            EnemyBase enemy = enemies.get(i);
            enemy.render(batch);
        }
    }
    
    public void renderItemsText() {
        int minActiveIndex = riseActiveData.minActiveItemIndex;
        int maxActiveIndex = riseActiveData.maxActiveItemIndex;
        
        for (int i = minActiveIndex; i <= maxActiveIndex; i++) {
            ItemBase item = items.get(i);
            item.renderText(batch, visibleAreaRelativePosition, gameAreaFont);
        }
    }
    
    private void startGameAreaBatch() {
        batch.setProjectionMatrix(cameraData.getGameAreaMatrix());
        batch.begin();
    }
    
    private void startGuiBatch() {
        batch.setProjectionMatrix(cameraData.getGuiMatrix());
        batch.begin();
    }
    
    private void endBatch() {
        batch.end();
        debugData.updateForBatch(batch);
    }
    
    // ensures that float values for positions are in reasonable range (up to around VISIBLE_AREA_POSITION_STRIDE) to prevent any numerical issues
    private void updateVisibleAreaStrideCount() {
        visibleAreaStrideCount++;
        visibleAreaRelativePosition -= VISIBLE_AREA_POSITION_STRIDE;
        riseGeneratorData.relativeHeight -= VISIBLE_AREA_POSITION_STRIDE;
        
        float positionChange = (float) -VISIBLE_AREA_POSITION_STRIDE;
        changeGameObjectPositionsY(platforms, positionChange);
        changeGameObjectPositionsY(enemies, positionChange);
        changeGameObjectPositionsY(items, positionChange);
        gameBackground.changeObjectPositions(positionChange);
        changeGameObjectPositionsY(scoreLines, positionChange);
        
        character.getPosition().y += positionChange;
    }
    
    private static void changeGameObjectPositionsY(Array<? extends RiseObject> gameObjects, float change) {
        for (RiseObject gameObject : gameObjects) {
            gameObject.offsetPositionY(change);
        }
    }
    
    private void updateRise() {
        
        float minActivityPosition = visibleAreaRelativePosition - ACTIVE_AREA_PADDING;
        float maxActivityPosition = visibleAreaRelativePosition + GameContainer.GAME_AREA_HEIGHT + ACTIVE_AREA_PADDING;
        
        IntRange activeIndexRange = Pools.obtain(IntRange.class);
        
        getActiveIndexRange(scoreLines, minActivityPosition, maxActivityPosition, activeIndexRange);
        riseActiveData.minActiveScoreLineIndex = activeIndexRange.min;
        riseActiveData.maxActiveScoreLineIndex = activeIndexRange.max;
        
        getActiveIndexRange(platforms, minActivityPosition, maxActivityPosition, activeIndexRange);
        riseActiveData.minActivePlatformIndex = activeIndexRange.min;
        riseActiveData.maxActivePlatformIndex = activeIndexRange.max;
        
        getActiveIndexRange(enemies, minActivityPosition, maxActivityPosition, activeIndexRange);
        riseActiveData.minActiveEnemyIndex = activeIndexRange.min;
        riseActiveData.maxActiveEnemyIndex = activeIndexRange.max;
        
        getActiveIndexRange(items, minActivityPosition, maxActivityPosition, activeIndexRange);
        riseActiveData.minActiveItemIndex = activeIndexRange.min;
        riseActiveData.maxActiveItemIndex = activeIndexRange.max;
        
        Pools.free(activeIndexRange);
        
        if (visibleAreaRelativePosition + VISIBLE_AREA_GENERATE_OFFSET >= riseGeneratorData.relativeHeight) {
            prepareRiseGeneratorData();
            
            riseGenerator.generateNext(riseGeneratorData);
            
            int minActiveIndex;
            
            minActiveIndex = riseActiveData.minActivePlatformIndex;
            if (minActiveIndex > 0) {
                platforms.removeRange(0, minActiveIndex - 1);
                riseActiveData.minActivePlatformIndex -= minActiveIndex;
                riseActiveData.maxActivePlatformIndex -= minActiveIndex;
            }
            if (riseGeneratorData.platforms.size > 0) {
                platforms.addAll(riseGeneratorData.platforms);
            }
            
            minActiveIndex = riseActiveData.minActiveEnemyIndex;
            if (minActiveIndex > 0) {
                enemies.removeRange(0, minActiveIndex - 1);
                riseActiveData.minActiveEnemyIndex -= minActiveIndex;
                riseActiveData.maxActiveEnemyIndex -= minActiveIndex;
            }
            if (riseGeneratorData.enemies.size > 0) {
                enemies.addAll(riseGeneratorData.enemies);
            }
            
            minActiveIndex = riseActiveData.minActiveItemIndex;
            if (minActiveIndex > 0) {
                items.removeRange(0, minActiveIndex - 1);
                riseActiveData.minActiveItemIndex -= minActiveIndex;
                riseActiveData.maxActiveItemIndex -= minActiveIndex;
            }
            if (riseGeneratorData.items.size > 0) {
                items.addAll(riseGeneratorData.items);
            }
        }
    }
    
    private void prepareRiseGeneratorData() {
        riseGeneratorData.resetForNextGeneration();
        riseGeneratorData.absoluteHeight = riseGeneratorData.relativeHeight + visibleAreaStrideCount * VISIBLE_AREA_POSITION_STRIDE;
    }
    
    private static void getActiveIndexRange(Array<? extends RiseObject> gameObjects, float minActivityPosition, float maxActivityPosition, IntRange activeIndexRange) {
        boolean isBeforeFirstActive = true;
        int numObjects = gameObjects.size;
        int minActiveIndex = 0;
        int maxActiveIndex = numObjects - 1;
        
        int prevGroupId = -1;
        
        for (int i = 0; i < numObjects; i++) {
            RiseObject obj = gameObjects.get(i);
            int groupId = obj.getGroupId();
            float objY = obj.getPositionY();
            if (isBeforeFirstActive) {
                if (objY >= minActivityPosition) {
                    minActiveIndex = i;
                    isBeforeFirstActive = false;
                }
            } else {
                // to make sure object groups are updated in atomic manner
                // for example, toggle spikes on same step (slightly different height)
                // should all be updated, or none of them should be
                // if groupId is -1 this rule is ignored
                boolean isObjGroupChanged = groupId == -1 || groupId != prevGroupId;
                
                if (objY > maxActivityPosition && isObjGroupChanged) {
                    maxActiveIndex = i - 1;
                    break;
                }
            }
            
            prevGroupId = groupId;
        }
        
        activeIndexRange.min = minActiveIndex;
        activeIndexRange.max = maxActiveIndex;
    }
    
    private void updateScoreLines(float delta) {
        int minActiveIndex = riseActiveData.minActiveScoreLineIndex;
        int maxActiveIndex = riseActiveData.maxActiveScoreLineIndex;
        
        for (int i = minActiveIndex; i <= maxActiveIndex; i++) {
            ScoreLineObject scoreLine = scoreLines.get(i);
            scoreLine.update(delta);
        }
    }
    
    private void updatePlatforms(float delta) {
        Vector2 c1 = Pools.obtain(Vector2.class);
        Vector2 c2 = Pools.obtain(Vector2.class);
        platformToCharCollisionData.reset();
        
        Vector2 charPosition = character.getPosition();
        
        c1.set(charPosition.x + GameCharacter.COLLISION_WIDTH_OFFSET - Platform.WIDTH, charPosition.y);
        c2.set(charPosition.x + GameCharacter.COLLISION_LINE_LENGTH, charPosition.y);
        
        // only check for collision when character is going down
        platformToCharCollisionData.isEnabled = character.getSpeed().y < 0.0f;
        
        int minActiveIndex = riseActiveData.minActivePlatformIndex;
        int maxActiveIndex = riseActiveData.maxActivePlatformIndex;
        
        for (int i = minActiveIndex; i <= maxActiveIndex; i++) {
            Platform platform = platforms.get(i);
            platform.update(delta, c1, c2, platformToCharCollisionData);
        }
        
        Pools.free(c1);
        Pools.free(c2);
    }
    
    private void updateEnemies(float delta) {
        int minActiveIndex = riseActiveData.minActiveEnemyIndex;
        int maxActiveIndex = riseActiveData.maxActiveEnemyIndex;
        
        for (int i = minActiveIndex; i <= maxActiveIndex; i++) {
            EnemyBase enemy = enemies.get(i);
            enemy.update(delta);
        }
    }
    
    private void updateItems(float delta) {
        int minActiveIndex = riseActiveData.minActiveItemIndex;
        int maxActiveIndex = riseActiveData.maxActiveItemIndex;
        
        for (int i = minActiveIndex; i <= maxActiveIndex; i++) {
            ItemBase item = items.get(i);
            item.update(delta);
        }
    }
    
    private Array<ScoreLineObject> getScoreLines(Array<PlayScoreData> playScores) {
        if (playScores == null) {
            return createEmptyScoreLines();
        }
        
        Array<ScoreLineObject> scoreLines = new Array<ScoreLineObject>(true, playScores.size);
        for (PlayScoreData playScore : playScores) {
            float y = (float) ((float) playScore.getScore() / (float) HEIGHT_SCORE_MULTIPLIER - visibleAreaStrideCount * VISIBLE_AREA_POSITION_STRIDE);
            ScoreLineObject scoreLine = new ScoreLineObject(y, playScore.getRank(), playScore.getName(), playScore.getScore(), gameAreaFont, assetManager);
            scoreLines.add(scoreLine);
        }
        
        if (scoreLines.size < 1) {
            return scoreLines;
        }
        
        scoreLines.sort(SCORE_LINE_1_COMPARATOR);
        
        Array<ScoreLineObject> scoreLinesFiltered = new Array<ScoreLineObject>(true, playScores.size);
        scoreLinesFiltered.add(scoreLines.first());
        
        for (int i = 1; i < scoreLines.size; i++) {
            ScoreLineObject curr = scoreLines.get(i);
            float y = curr.getPositionY();
            float lastY = scoreLinesFiltered.peek().getPositionY();
            float diffY = lastY - y;
            if (diffY > 5.0f || y > 1000.0f && diffY > 1.0f) {
                scoreLinesFiltered.add(curr);
            }
        }
        
        scoreLinesFiltered.sort(SCORE_LINE_2_COMPARATOR);
        
        return scoreLinesFiltered;
    }
    
    private static Array<ScoreLineObject> createEmptyScoreLines() {
        return new Array<ScoreLineObject>(true, 0);
    }
    
    public float getVisibleAreaRelativePosition() {
        return visibleAreaRelativePosition;
    }
    
    public boolean isFinished() {
        return character.isFinished();
    }
    
    public int getScore() {
        return heightScore + character.getScore();
    }
    
    public int getLives() {
        return character.getLives();
    }
    
    public int getCoins() {
        return character.getCoins();
    }
    
    public DebugData getDebugData() {
        return debugData;
    }
}
